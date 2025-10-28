package io.xpay.sdk.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response payload containing merchant balance information for a specific symbol.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class MerchantBalanceData {

    private Long id;

    /**
     * Merchant identifier
     */
    private Long merchantId;

    /**
     * Currency symbol such as USDT or BTC
     */
    private String symbol;

    /**
     * Available balance amount
     */
    private String balance;

    /**
     * Frozen balance amount
     */
    private String frozenBalance;

    /**
     * Total balance amount (redundant reference)
     */
    private String totalBalance;
}
