package io.xpay.sdk;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.xpay.sdk.client.ApiClient;
import io.xpay.sdk.exception.XPayApiException;
import io.xpay.sdk.model.request.CollectionRequest;
import io.xpay.sdk.model.request.PayoutRequest;
import io.xpay.sdk.model.request.SignedRequest;
import io.xpay.sdk.model.response.ApiResponse;
import io.xpay.sdk.model.response.CollectionData;
import io.xpay.sdk.model.response.OrderDetails;
import io.xpay.sdk.model.response.PayoutData;
import io.xpay.sdk.model.response.SupportedSymbol;
import io.xpay.sdk.model.webhook.CollectWebhookData;
import io.xpay.sdk.model.webhook.OrderWebhookData;
import io.xpay.sdk.model.webhook.WebhookEvent;
import io.xpay.sdk.util.SignatureUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * X-Pay SDK for Java
 *
 * Official SDK for integrating with the X-Pay cryptocurrency payment gateway
 */
public class XPay {
    private final XPayConfig config;
    private final ApiClient apiClient;
    private final String apiSecret;
    private final ObjectMapper objectMapper;

    /**
     * Create a new XPay SDK instance
     * @param config - Configuration options
     */
    public XPay(XPayConfig config) {
        this.config = config;
        this.apiSecret = config.getApiSecret();
        this.apiClient = new ApiClient(config);
        
        // Configure ObjectMapper to be more lenient with unknown properties
        this.objectMapper = new ObjectMapper();
        objectMapper.configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    /**
     * Create a new payout order
     * @param request - Payout request data
     * @return Payout response with order details
     * @throws XPayApiException if the API returns an error
     * @throws Exception if there is a network or parsing error
     */
    public ApiResponse<PayoutData> createPayout(PayoutRequest request) throws Exception {
        SignedRequest signedRequest = generateSignature(request);
        return apiClient.post("/v1/order/createPayout", signedRequest, new TypeReference<ApiResponse<PayoutData>>() {});
    }

    /**
     * Create a new collection order
     * @param request - Collection request data
     * @return Collection response with order details
     * @throws XPayApiException if the API returns an error
     * @throws Exception if there is a network or parsing error
     */
    public ApiResponse<CollectionData> createCollection(CollectionRequest request) throws Exception {
        SignedRequest signedRequest = generateSignature(request);
        return apiClient.post("/v1/order/createCollection", signedRequest, new TypeReference<ApiResponse<CollectionData>>() {});
    }

    /**
     * Get order status by ID
     * @param orderId - Order ID
     * @return Order details information
     * @throws XPayApiException if the API returns an error
     * @throws Exception if there is a network or parsing error
     */
    public ApiResponse<OrderDetails> getOrderStatus(String orderId) throws Exception {
        return apiClient.get("/v1/order/status/" + orderId, null, new TypeReference<ApiResponse<OrderDetails>>() {});
    }

    /**
     * Get supported symbols
     * @return List of supported symbols
     * @throws XPayApiException if the API returns an error
     * @throws Exception if there is a network or parsing error
     */
    public ApiResponse<List<SupportedSymbol>> getSupportedSymbols() throws Exception {
        return getSupportedSymbols(null, null);
    }

    /**
     * Get supported symbols with filtering
     * @param chain - Optional blockchain network
     * @param symbol - Optional symbol
     * @return List of supported symbols
     * @throws XPayApiException if the API returns an error
     * @throws Exception if there is a network or parsing error
     */
    public ApiResponse<List<SupportedSymbol>> getSupportedSymbols(String chain, String symbol) throws Exception {
        return apiClient.get("/v1/symbol/supportSymbols", 
                SignatureUtil.buildQueryParams(chain, symbol), 
                new TypeReference<ApiResponse<List<SupportedSymbol>>>() {});
    }

    /**
     * Verify webhook signature to ensure it came from X-Pay
     * @param body - Raw webhook request body
     * @param signature - Signature from webhook
     * @param timestamp - Timestamp from webhook
     * @return True if signature is valid
     */
    public boolean verifyWebhook(String body, String signature, String timestamp) {
        try {
            // Parse the webhook body
            WebhookEvent event = objectMapper.readValue(body, WebhookEvent.class);
            
            // Get the data, nonce, and notifyType from the webhook data
            Object data = event.getData();
            String nonce = event.getNonce();
            String notifyType = event.getNotifyType().name();
            
            // Create parameters map for signature generation
            Map<String, Object> params = new HashMap<>();
            params.put("data", SignatureUtil.convertDataToMap(data));
            params.put("nonce", nonce);
            params.put("notifyType", notifyType);
            params.put("timestamp", Long.parseLong(timestamp));
            
            // Generate the expected signature
            String expectedSignature = SignatureUtil.generateSignature(params, config.getApiSecret());
            
            // Check if the timestamp is within 30 seconds
            long currentTime = System.currentTimeMillis() / 1000;
            long webhookTime = Long.parseLong(timestamp);
            if (Math.abs(currentTime - webhookTime) > 30) {
                return false; // Timestamp is too old or in the future
            }
            
            // Compare the signatures
            return expectedSignature.equals(signature);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Parse webhook event data
     * @param body - Webhook request body as string
     * @param signature - Signature from webhook
     * @param timestamp - Timestamp from webhook
     * @return Parsed webhook event or null if invalid
     */
    public WebhookEvent parseWebhook(String body, String signature, String timestamp) {
        try {
            if (!verifyWebhook(body, signature, timestamp)) {
                return null;
            }
            
            WebhookEvent event = objectMapper.readValue(body, WebhookEvent.class);
            
            // Convert the data field to the appropriate type based on notifyType
            if (event.getNotifyType().name().startsWith("ORDER_")) {
                OrderWebhookData orderData = objectMapper.convertValue(event.getData(), OrderWebhookData.class);
                event.setData(orderData);
            } else if (event.getNotifyType().name().startsWith("COLLECT_")) {
                CollectWebhookData collectData = objectMapper.convertValue(event.getData(), CollectWebhookData.class);
                event.setData(collectData);
            }
            
            return event;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Generate a random nonce string
     * @return Random nonce string
     */
    private String generateNonce() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * Generate signature for request according to the API specification
     * @param params - Request parameters to sign
     * @return Request object with signature, timestamp, nonce, and data
     */
    private SignedRequest generateSignature(Object params) {
        // Generate timestamp and nonce
        long timestamp = System.currentTimeMillis() / 1000;
        String nonce = generateNonce();

        // Create parameters map for signature generation
        Map<String, Object> signParams = new HashMap<>();
        signParams.put("data", SignatureUtil.convertDataToMap(params));
        signParams.put("nonce", nonce);
        signParams.put("timestamp", timestamp);

        // Calculate HMAC-SHA256 signature
        String signature = SignatureUtil.generateSignature(signParams, apiSecret);

        // Return the request object with signature, timestamp, nonce, and data
        return SignedRequest.builder()
                .sign(signature)
                .timestamp(timestamp)
                .nonce(nonce)
                .data(params)
                .build();
    }
}