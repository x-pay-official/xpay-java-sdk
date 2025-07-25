package io.xpay.sdk.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Signed Request Wrapper
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SignedRequest {
    /**
     * Signature
     */
    private String sign;
    
    /**
     * Timestamp
     */
    private Long timestamp;
    
    /**
     * Nonce
     */
    private String nonce;
    
    /**
     * Request data
     */
    private Object data;
}