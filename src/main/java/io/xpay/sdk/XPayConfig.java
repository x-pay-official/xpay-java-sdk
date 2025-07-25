package io.xpay.sdk;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * X-Pay SDK Configuration
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class XPayConfig {
    /**
     * API Key for authentication
     */
    private String apiKey;
    
    /**
     * API Secret for authentication
     */
    private String apiSecret;
    
    /**
     * Base URL for API requests
     */
    @Builder.Default
    private String baseUrl = "https://api.x-pay.fun";
    
    /**
     * Connection timeout in milliseconds
     */
    @Builder.Default
    private int connectTimeout = 30000;
    
    /**
     * Read timeout in milliseconds
     */
    @Builder.Default
    private int readTimeout = 30000;
}