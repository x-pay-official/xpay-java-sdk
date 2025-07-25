package io.xpay.sdk.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Supported Symbol
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SupportedSymbol {
    /**
     * Symbol name (e.g., USDT)
     */
    private String symbol;
    
    /**
     * Blockchain network (e.g., TRON)
     */
    private String chain;
    
    /**
     * Decimal places
     */
    private Integer decimals;
    
    /**
     * Contract address
     * API may return this as either "contract" or "contractAddress"
     */
    @JsonProperty(value = "contract")
    private String contract;
    
    /**
     * Contract address alternative field
     */
    @JsonProperty(value = "contractAddress")
    public void setContractAddress(String contractAddress) {
        this.contract = contractAddress;
    }
    
    /**
     * Minimum amount
     */
    private Double minAmount;
    
    /**
     * Maximum amount
     */
    private Double maxAmount;
}
