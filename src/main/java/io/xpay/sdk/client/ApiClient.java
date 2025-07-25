package io.xpay.sdk.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.xpay.sdk.XPayConfig;
import io.xpay.sdk.exception.XPayApiException;
import io.xpay.sdk.model.response.ApiResponse;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * API Client for making HTTP requests to the X-Pay API
 */
public class ApiClient {
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private final OkHttpClient client;
    private final String baseUrl;
    private final String apiKey;
    private final ObjectMapper objectMapper;

    /**
     * Create a new API client
     * @param config - Configuration options
     */
    public ApiClient(XPayConfig config) {
        this.apiKey = config.getApiKey();
        this.baseUrl = config.getBaseUrl();
        
        // Configure ObjectMapper to be more lenient with unknown properties
        this.objectMapper = new ObjectMapper();
        objectMapper.configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        
        this.client = new OkHttpClient.Builder()
                .connectTimeout(config.getConnectTimeout(), TimeUnit.MILLISECONDS)
                .readTimeout(config.getReadTimeout(), TimeUnit.MILLISECONDS)
                .build();
    }

    /**
     * Make a GET request to the API
     * @param path - API endpoint path
     * @param queryParams - Query parameters
     * @param responseType - Type reference for response deserialization
     * @return Deserialized response
     * @throws XPayApiException if the API returns an error
     * @throws Exception if there is a network or parsing error
     */
    public <T> T get(String path, Map<String, String> queryParams, TypeReference<T> responseType) throws Exception {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(baseUrl + path).newBuilder();
        
        if (queryParams != null) {
            for (Map.Entry<String, String> entry : queryParams.entrySet()) {
                if (entry.getValue() != null) {
                    urlBuilder.addQueryParameter(entry.getKey(), entry.getValue());
                }
            }
        }
        
        Request request = new Request.Builder()
                .url(urlBuilder.build())
                .header("X-API-TOKEN", apiKey)
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .get()
                .build();
        
        return executeRequest(request, responseType);
    }

    /**
     * Make a POST request to the API
     * @param path - API endpoint path
     * @param body - Request body
     * @param responseType - Type reference for response deserialization
     * @return Deserialized response
     * @throws XPayApiException if the API returns an error
     * @throws Exception if there is a network or parsing error
     */
    public <T> T post(String path, Object body, TypeReference<T> responseType) throws Exception {
        String jsonBody = objectMapper.writeValueAsString(body);
        
        RequestBody requestBody = RequestBody.create(jsonBody, JSON);
        
        Request request = new Request.Builder()
                .url(baseUrl + path)
                .header("X-API-TOKEN", apiKey)
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .post(requestBody)
                .build();
        
        return executeRequest(request, responseType);
    }

    /**
     * Execute an HTTP request and handle the response
     * @param request - OkHttp request
     * @param responseType - Type reference for response deserialization
     * @return Deserialized response
     * @throws XPayApiException if the API returns an error
     * @throws Exception if there is a network or parsing error
     */
    private <T> T executeRequest(Request request, TypeReference<T> responseType) throws Exception {
        try (Response response = client.newCall(request).execute()) {
            String responseBody = response.body() != null ? response.body().string() : null;
            
            if (!response.isSuccessful()) {
                handleErrorResponse(response, responseBody);
            }
            
            try {
                return objectMapper.readValue(responseBody, responseType);
            } catch (Exception e) {
                throw new Exception("Error parsing response: " + e.getMessage() + "\nResponse body: " + responseBody, e);
            }
        } catch (IOException e) {
            throw new Exception("Network error: " + e.getMessage(), e);
        }
    }

    /**
     * Handle error responses from the API
     * @param response - OkHttp response
     * @param responseBody - Response body as string
     * @throws XPayApiException with details about the error
     */
    private void handleErrorResponse(Response response, String responseBody) throws XPayApiException {
        int statusCode = response.code();
        String errorMessage = "API error";
        int errorCode = 0;
        Object errorData = null;
        
        try {
            ApiResponse<?> errorResponse = objectMapper.readValue(responseBody, new TypeReference<ApiResponse<?>>() {});
            errorMessage = errorResponse.getMsg();
            errorCode = errorResponse.getCode();
            errorData = errorResponse.getData();
        } catch (Exception e) {
            // If we can't parse the error response, just use the status code and message
            errorMessage = "API error: " + response.message();
        }
        
        throw new XPayApiException(errorMessage, statusCode, errorCode, errorData);
    }
}