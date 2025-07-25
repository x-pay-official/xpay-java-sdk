package io.xpay.sdk.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.xpay.sdk.model.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Order Details
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderDetails {
    /**
     * Order ID
     */
    private String orderId;
    
    /**
     * Order type
     */
    private String orderType;
    
    /**
     * Order status
     */
    private OrderStatus status;
    
    /**
     * Reason for failure (if any)
     */
    private String reason;
    
    /**
     * Transaction details
     */
    private Transaction transaction;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Transaction {
        /**
         * Blockchain network
         */
        private String chain;
        
        /**
         * Currency symbol
         */
        private String symbol;
        
        /**
         * Block number
         */
        private Long blockNum;
        
        /**
         * Transaction ID
         */
        private String txid;
        
        /**
         * Contract address
         */
        private String contractAddress;
        
        /**
         * From address
         */
        private String from;
        
        /**
         * To address
         */
        private String to;
        
        /**
         * Amount
         */
        private String amount;
        
        /**
         * Transaction timestamp
         */
        private Long timestamp;
        
        /**
         * Gas fee
         */
        private String txGas;
        
        /**
         * Number of confirmations
         */
        private Integer confirmedNum;
        
        /**
         * Transaction status
         */
        private String status;
    }
}