package io.xpay.sdk.model.webhook;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Collect Webhook Data
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CollectWebhookData {
    /**
     * Collected amount
     */
    private Double collectAmount;
    
    /**
     * Fee amount
     */
    private Double fee;
    
    /**
     * Fee ratio
     */
    private Double feeRatio;
    
    /**
     * Reason for failure (if any)
     */
    private String reason;
    
    /**
     * Transaction details
     */
    private WebhookTransaction transaction;
}