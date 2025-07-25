package io.xpay.sdk.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Payout Request - Merchant initiates a payout via API
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PayoutRequest {
    /**
     * Payment amount
     */
    private Double amount;
    
    /**
     * Currency symbol (e.g., USDT)
     */
    private String symbol;
    
    /**
     * Blockchain network (e.g., TRON, ETH, BTC)
     */
    private String chain;
    
    /**
     * Your internal order ID (optional)
     */
    private String orderId;
    
    /**
     * User ID (required)
     */
    private String uid;
    
    /**
     * Receiving address
     */
    private String receiveAddress;
}