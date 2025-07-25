package io.xpay.sdk.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.xpay.sdk.model.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Payout Response Data
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PayoutData {
    /**
     * Order ID
     */
    private String orderId;
    
    /**
     * Order status
     */
    private OrderStatus status;
    
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
     * Receiving address
     */
    private String receiveAddress;
}