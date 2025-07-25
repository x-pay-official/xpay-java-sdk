package io.xpay.sdk.model.webhook;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Webhook Transaction Details
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class WebhookTransaction {
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
    private Double amount;
    
    /**
     * Transaction timestamp
     */
    private Long timestamp;
    
    /**
     * Gas fee
     */
    private Double txGas;
    
    /**
     * Number of confirmations
     */
    private Integer confirmedNum;
    
    /**
     * Transaction status
     */
    private String status;
}