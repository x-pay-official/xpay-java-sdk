package io.xpay.sdk.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Test class for SignatureUtil
 */
public class SignatureUtilTest {
    public static void main(String[] args) {
        // Create a test map with nested objects
        Map<String, Object> testData = new HashMap<>();
        testData.put("amount", 100);
        testData.put("symbol", "USDT");
        testData.put("chain", "TRON");
        testData.put("orderId", "order-1753451591786");
        testData.put("uid", "user123");
        testData.put("receiveAddress", "TXmVthgn6yT1kANGJHTHcbEGEKYDLLGJGp");
        
        // Add a nested object
        Map<String, Object> nestedMap = new HashMap<>();
        nestedMap.put("key1", "value1");
        nestedMap.put("key2", 123);
        testData.put("nestedObject", nestedMap);
        
        // Add an array with objects
        Map<String, Object> item1 = new HashMap<>();
        item1.put("id", 1);
        item1.put("name", "Item 1");
        
        Map<String, Object> item2 = new HashMap<>();
        item2.put("id", 2);
        item2.put("name", "Item 2");
        
        testData.put("items", Arrays.asList(item1, item2));
        
        // Create parameters for signature
        Map<String, Object> params = new HashMap<>();
        params.put("data", testData);
        params.put("nonce", "puy6sxk5jwoyyeafi0hi7i");
        params.put("timestamp", 1753451591);
        
        // Generate signature
        String apiSecret = "test_secret_key";
        String signature = SignatureUtil.generateSignature(params, apiSecret);
        
        System.out.println("Signature: " + signature);
    }
}