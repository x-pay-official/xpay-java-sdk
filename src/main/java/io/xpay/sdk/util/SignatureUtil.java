package io.xpay.sdk.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Utility class for generating and verifying signatures
 */
public class SignatureUtil {
    private static final String HMAC_SHA256 = "HmacSHA256";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Generate signature for API requests and webhook verification
     * @param params - Parameters to include in the signature
     * @param apiSecret - API secret key
     * @return HMAC-SHA256 signature
     */
    public static String generateSignature(Map<String, Object> params, String apiSecret) {
        try {
            Map<String, Object> sortedParams = sortMapRecursively(params);

            String signatureString = sortedParams.entrySet().stream()
                .map(entry -> formatParameter(entry.getKey(),  entry.getValue()))
                .collect(Collectors.joining("&"));

            // Calculate HMAC-SHA256 signature
            return hmacSha256(signatureString, apiSecret);
        } catch (Exception e) {
            throw new RuntimeException("Error generating signature", e);
        }
    }

    private static Map<String, Object> sortMapRecursively(Map<String, Object> map) {
        Map<String, Object> sortedMap = new TreeMap<>();
        map.forEach((key,  value) -> {
            if (value instanceof Map) {
                sortedMap.put(key,  sortMapRecursively((Map<String, Object>) value));
            } else if (value instanceof Collection) {
                List<Object> sortedList = ((Collection<?>) value).stream()
                    .map(item -> item instanceof Map ?
                        sortMapRecursively((Map<String, Object>) item) : item)
                    .collect(Collectors.toList());
                sortedMap.put(key,  sortedList);
            } else {
                sortedMap.put(key,  value);
            }
        });
        return sortedMap;
    }

    private static String formatParameter(String key, Object value) {
        if (value == null) {
            return key + "=";
        }
        return key + "=" + (value instanceof Map ? "{" + formatMap((Map<?, ?>) value) + "}" : value);
    }

    private static String formatMap(Map<?, ?> map) {
        return map.entrySet().stream()
            .sorted(Comparator.comparing(e -> String.valueOf(e.getKey())))  // Sort by key as string
            .map(e -> {
                Object value = e.getValue();
                if (value instanceof Map) {
                    return e.getKey() + "={" + formatMap((Map<?, ?>) value) + "}";
                } else if (value instanceof Collection) {
                    return e.getKey() + "=[" + formatCollection((Collection<?>) value) + "]";
                } else {
                    return e.getKey() + "=" + value;
                }
            })
            .collect(Collectors.joining(","));
    }
    
    private static String formatCollection(Collection<?> collection) {
        return collection.stream()
            .map(item -> {
                if (item instanceof Map) {
                    return "{" + formatMap((Map<?, ?>) item) + "}";
                } else {
                    return String.valueOf(item);
                }
            })
            .collect(Collectors.joining(","));
    }



    /**
     * Convert data object to map for signature generation
     * @param data - Data object
     * @return Map of key-value pairs
     */
    public static Map<String, Object> convertDataToMap(Object data) {
        if (data == null) {
            return new HashMap<>();
        }
        
        // Convert data to map
        if (data instanceof Map) {
            return (Map<String, Object>) data;
        } else {
            return objectMapper.convertValue(data, Map.class);
        }
    }

    /**
     * Calculate HMAC-SHA256 signature
     * @param data - Data to sign
     * @param key - Secret key
     * @return Hex-encoded signature
     */
    private static String hmacSha256(String data, String key) throws Exception {
        Mac sha256Hmac = Mac.getInstance(HMAC_SHA256);
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), HMAC_SHA256);
        sha256Hmac.init(secretKey);
        
        byte[] hmacBytes = sha256Hmac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(hmacBytes);
    }

    /**
     * Convert bytes to hex string
     * @param bytes - Byte array
     * @return Hex string
     */
    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    /**
     * Build query parameters for GET requests
     * @param chain - Optional blockchain network
     * @param symbol - Optional symbol
     * @return Map of query parameters
     */
    public static Map<String, String> buildQueryParams(String chain, String symbol) {
        Map<String, String> params = new HashMap<>();
        if (chain != null) {
            params.put("chain", chain);
        }
        if (symbol != null) {
            params.put("symbol", symbol);
        }
        return params;
    }
}
