package io.xpay.sdk.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Collection Response Data
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CollectionData {
    /**
     * Payment address
     */
    private String address;
    
    /**
     * Payment amount
     */
    private String amount;
    
    /**
     * Currency symbol
     */
    private String symbol;
    
    /**
     * Blockchain network
     */
    private String chain;
    
    /**
     * User ID
     */
    private String uid;
    
    /**
     * Order ID
     */
    private String orderId;
    
    /**
     * Expiration time
     */
    private Long expiredTime;
}