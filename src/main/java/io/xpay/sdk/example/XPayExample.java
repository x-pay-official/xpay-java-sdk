package io.xpay.sdk.example;

import io.xpay.sdk.XPay;
import io.xpay.sdk.XPayConfig;
import io.xpay.sdk.model.request.CollectionRequest;
import io.xpay.sdk.model.request.PayoutRequest;
import io.xpay.sdk.model.response.ApiResponse;
import io.xpay.sdk.model.response.CollectionData;
import io.xpay.sdk.model.response.OrderDetails;
import io.xpay.sdk.model.response.PayoutData;
import io.xpay.sdk.model.response.SupportedSymbol;
import io.xpay.sdk.model.webhook.WebhookEvent;

import java.util.List;

/**
 * X-Pay SDK Example
 */
public class XPayExample {

    public static void main(String[] args) {
        // Initialize the SDK with your API credentials
        XPay xpay = new XPay(XPayConfig.builder()
                .apiKey("")
                .apiSecret("")
                .baseUrl("https://api.x-pay.fun") // Optional, defaults to production API
                .build());

        try {
            // Create a payout order (merchant sends crypto to user)
            createPayoutExample(xpay);

            // Create a collection order (merchant receives crypto from user)
            createCollectionExample(xpay);

            // Check order status
            checkOrderStatusExample(xpay, "order-123");

            // Get supported symbols
            getSupportedSymbolsExample(xpay);

            // Process webhook
            processWebhookExample(xpay);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Create a payout order example
     */
    private static void createPayoutExample(XPay xpay) throws Exception {
        System.out.println("Creating a new payout order...");

        PayoutRequest request = PayoutRequest.builder()
                .amount(100.0)
                .symbol("USDT")
                .chain("TRON")
//                .orderId("order-" + System.currentTimeMillis()) // Optional Generate a unique order ID
                .uid("user123") // Required user ID
                .receiveAddress("TXmVthgn6yT1kANGJHTHcbEGEKYDLLGJGp") // User's wallet address
                .build();

        ApiResponse<PayoutData> response = xpay.createPayout(request);

        System.out.println("Payout created successfully:");
        System.out.println("- Code: " + response.getCode());
        System.out.println("- Message: " + response.getMsg());

        if (response.getData() != null) {
            PayoutData data = response.getData();
            System.out.println("- Order ID: " + data.getOrderId());
            System.out.println("- Status: " + data.getStatus());
            System.out.println("- Amount: " + data.getAmount() + " " + data.getSymbol());
            System.out.println("- Chain: " + data.getChain());
            System.out.println("- Receiving Address: " + data.getReceiveAddress());
        }
    }

    /**
     * Create a collection order example
     */
    private static void createCollectionExample(XPay xpay) throws Exception {
        System.out.println("\nCreating a new collection order...");

        CollectionRequest request = CollectionRequest.builder()
                .amount(50.0)
                .symbol("USDT")
                .chain("TRON")
//                .orderId("order-" + System.currentTimeMillis()) // Optional Generate a unique order ID
                .uid("user123") // Required user ID
                .build();

        ApiResponse<CollectionData> response = xpay.createCollection(request);

        System.out.println("Collection created successfully:");
        System.out.println("- Code: " + response.getCode());
        System.out.println("- Message: " + response.getMsg());

        if (response.getData() != null) {
            CollectionData data = response.getData();
            System.out.println("- Order ID: " + data.getOrderId());
            System.out.println("- Address: " + data.getAddress());
            System.out.println("- Amount: " + data.getAmount() + " " + data.getSymbol());
            System.out.println("- Chain: " + data.getChain());
            System.out.println("- Expires at: " + new java.util.Date(data.getExpiredTime() * 1000));
        }
    }

    /**
     * Check order status example
     */
    private static void checkOrderStatusExample(XPay xpay, String orderId) throws Exception {
        System.out.println("\nChecking status of order " + orderId + "...");

        ApiResponse<OrderDetails> response = xpay.getOrderStatus(orderId);

        System.out.println("Order details:");
        System.out.println("- Code: " + response.getCode());
        System.out.println("- Message: " + response.getMsg());

        if (response.getData() != null) {
            OrderDetails data = response.getData();
            System.out.println("- Order ID: " + data.getOrderId());
            System.out.println("- Order Type: " + data.getOrderType());
            System.out.println("- Status: " + data.getStatus());

            // Transaction details
            if (data.getTransaction() != null) {
                OrderDetails.Transaction tx = data.getTransaction();
                System.out.println("- Transaction:");
                System.out.println("  - Amount: " + tx.getAmount());
                System.out.println("  - To Address: " + tx.getTo());

                if (tx.getChain() != null) {
                    System.out.println("  - Chain: " + tx.getChain());
                }

                if (tx.getSymbol() != null) {
                    System.out.println("  - Symbol: " + tx.getSymbol());
                }

                if (tx.getTxid() != null) {
                    System.out.println("  - Transaction Hash: " + tx.getTxid());
                }

                System.out.println("  - Confirmations: " + tx.getConfirmedNum());
            }
        }
    }

    /**
     * Get supported symbols example
     */
    private static void getSupportedSymbolsExample(XPay xpay) throws Exception {
        System.out.println("\nGetting supported symbols...");

        ApiResponse<List<SupportedSymbol>> response = xpay.getSupportedSymbols();

        System.out.println("Supported symbols:");
        if (response.getData() != null) {
            for (SupportedSymbol symbol : response.getData()) {
                System.out.println("- " + symbol.getSymbol() + " on " + symbol.getChain() + " (decimals: " + symbol.getDecimals() + ")");
                if (symbol.getContract() != null) {
                    System.out.println("  Contract: " + symbol.getContract());
                }
                if (symbol.getMinAmount() != null) {
                    System.out.println("  Min: " + symbol.getMinAmount() + ", Max: " + (symbol.getMaxAmount() != null ? symbol.getMaxAmount() : "unlimited"));
                }
            }
        }
    }

    /**
     * Process webhook example
     */
    private static void processWebhookExample(XPay xpay) {
        System.out.println("\nProcessing webhook...");

        // This is an example webhook payload that would be sent by X-Pay
        // In a real application, this would come from the request body
        String webhookBody = "{\"sign\":\"971c801d2b3fecc74c40925d1ed8b679a7495c550f6ae449c92df4180f1986e0\",\"timestamp\":1753455837,\"nonce\":\"8d677f6a1c1b4004916d7cbb6fe76163\",\"notifyType\":\"ORDER_SUCCESS\",\"data\":{\"orderId\":\"20240101111111011\",\"orderType\":\"COLLECTION\",\"reason\":null,\"status\":\"SUCCESS\",\"transaction\":{\"chain\":\"TRON\",\"symbol\":\"USDT\",\"blockNum\":73971843,\"txid\":\"938d4d20f049bfe45f429f1c3cb62de7c57d3f7505ae691b79aa9a024f23ef87\",\"contractAddress\":\"TR7NHqjeKQxGTCi8q8ZY4pL8otSzgjLj6t\",\"from\":\"TGyjjt1esfqJWrPncpygq3QA43epY46V8D\",\"to\":\"TW8ArYLg5PuwYugmYM8QSux5oXxfUbXA8c\",\"amount\":1.5,\"timestamp\":1752573867000,\"txGas\":27.35985,\"confirmedNum\":196573,\"status\":\"SUCCESS\"}}}";

        try {
            // Extract signature and timestamp from the webhook body
            com.fasterxml.jackson.databind.JsonNode jsonNode = new com.fasterxml.jackson.databind.ObjectMapper().readTree(webhookBody);
            String signature = jsonNode.get("sign").asText();
            String timestamp = jsonNode.get("timestamp").asText();
            
            // Parse the webhook body to get the components
            WebhookEvent event = xpay.parseWebhook(webhookBody, signature, timestamp);

            if (event != null) {
                System.out.println("Webhook verified successfully");
                System.out.println("- Notify Type: " + event.getNotifyType());
                System.out.println("- Timestamp: " + event.getTimestamp());

                // Handle different notification types
                switch (event.getNotifyType()) {
                    case ORDER_PENDING:
                        System.out.println("Order is pending");
                        break;
                    case ORDER_PENDING_CONFIRMATION:
                        System.out.println("Order is pending confirmation");
                        break;
                    case ORDER_SUCCESS:
                        System.out.println("Order has been completed successfully");
                        break;
                    case ORDER_FAILED:
                        System.out.println("Order has failed");
                        break;
                    case ORDER_EXPIRED:
                        System.out.println("Order has expired");
                        break;
                    case COLLECT_PENDING:
                        System.out.println("Collection is pending");
                        break;
                    case COLLECT_SUCCESS:
                        System.out.println("Collection has been completed successfully");
                        break;
                    case COLLECT_FAILED:
                        System.out.println("Collection has failed");
                        break;
                    default:
                        System.out.println("Unknown notification type");
                }
            } else {
                System.out.println("Invalid webhook signature or timestamp expired");
            }
        } catch (Exception e) {
            System.out.println("Error processing webhook: " + e.getMessage());
        }
    }
}