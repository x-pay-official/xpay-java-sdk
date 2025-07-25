package io.xpay.sdk.model.webhook;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.xpay.sdk.model.WebhookNotifyType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Webhook Event
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class WebhookEvent {
    /**
     * Signature for verification
     */
    private String sign;
    
    /**
     * Timestamp of the event
     */
    private Long timestamp;
    
    /**
     * Nonce for signature verification
     */
    private String nonce;
    
    /**
     * Notification type
     */
    private WebhookNotifyType notifyType;
    
    /**
     * Event data
     */
    private Object data;
}