package io.xpay.sdk.model.webhook;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.xpay.sdk.model.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Order Webhook Data
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderWebhookData {
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
    private WebhookTransaction transaction;
}