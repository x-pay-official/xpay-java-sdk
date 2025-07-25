# X-Pay Java SDK

Official Java SDK for the X-Pay cryptocurrency payment gateway.

## Features

- Create cryptocurrency payout orders (merchant sends crypto to user)
- Create cryptocurrency collection orders (merchant receives crypto from user)
- Check order status
- Get supported cryptocurrencies and chains
- Verify and parse webhook notifications
- Spring Boot 3.4.6 compatible

## Installation

Add the dependency to your Maven project:

```xml
<dependency>
    <groupId>io.xpay</groupId>
    <artifactId>xpay-java-sdk</artifactId>
    <version>0.1.0</version>
</dependency>
```

Or for Gradle:

```groovy
implementation 'io.xpay:xpay-java-sdk:0.1.0'
```

## Quick Start

```java
import io.xpay.sdk.XPay;
import io.xpay.sdk.XPayConfig;
import io.xpay.sdk.model.request.PayoutRequest;
import io.xpay.sdk.model.response.ApiResponse;
import io.xpay.sdk.model.response.PayoutData;

// Initialize the SDK with your API credentials
XPay xpay = new XPay(XPayConfig.builder()
        .apiKey("your-api-token")
        .apiSecret("your-api-secret")
        .baseUrl("https://api.x-pay.fun") // Optional, defaults to production API
        .build());

// Create a payout order (merchant sends crypto to user)
try {
    PayoutRequest request = PayoutRequest.builder()
            .amount(100.0)
            .symbol("USDT")
            .chain("TRON")
            .orderId("order-" + System.currentTimeMillis()) // Optional,Generate a unique order ID (optional)
            .uid("user123") // Required user ID
            .receiveAddress("TXmVthgn6yT1kANGJHTHcbEGEKYDLLGJGp") // User's wallet address
            .build();

    ApiResponse<PayoutData> response = xpay.createPayout(request);

    System.out.println("Payout created successfully:");
    System.out.println("- Code: " + response.getCode());
    System.out.println("- Message: " + response.getMsg());
    System.out.println("- Order ID: " + response.getData().getOrderId());
} catch (Exception e) {
    System.err.println("Error creating payout: " + e.getMessage());
}
```

## API Reference

### Configuration

```java
XPay xpay = new XPay(XPayConfig.builder()
        .apiKey("your-api-token")
        .apiSecret("your-api-secret")
        .baseUrl("https://api.x-pay.fun") // Optional, defaults to production API
        .connectTimeout(30000) // Optional, connection timeout in milliseconds
        .readTimeout(30000) // Optional, read timeout in milliseconds
        .build());
```

### Payout Orders

#### Create a payout order (merchant sends crypto to user)

```java
PayoutRequest request = PayoutRequest.builder()
        .amount(100.0)
        .symbol("USDT")
        .chain("TRON")
        .orderId("order-123") // Optional
        .uid("user123") // Required
        .receiveAddress("TXmVthgn6yT1kANGJHTHcbEGEKYDLLGJGp") // User's wallet address
        .build();

ApiResponse<PayoutData> response = xpay.createPayout(request);

// Response structure
// {
//   "code": 200,
//   "msg": "Success",
//   "data": {
//     "orderId": "order-123",
//     "status": "PENDING",
//     "amount": "100.00000000",
//     "symbol": "USDT",
//     "chain": "TRON",
//     "uid": "user123",
//     "receiveAddress": "TXmVthgn6yT1kANGJHTHcbEGEKYDLLGJGp"
//   }
// }
```

### Collection Orders

#### Create a collection order (merchant receives crypto from user)

```java
CollectionRequest request = CollectionRequest.builder()
        .amount(50.0)
        .symbol("USDT")
        .chain("TRON")
        .orderId("order-123") // Optional
        .uid("user123") // Required
        .build();

ApiResponse<CollectionData> response = xpay.createCollection(request);

// Response structure
// {
//   "code": 200,
//   "msg": "Success",
//   "data": {
//     "orderId": "order-123",
//     "address": "TW8ArYLg5PuwYugmYM8QSux5oXxfUbXA8c",
//     "amount": "50.00000000",
//     "symbol": "USDT",
//     "chain": "TRON",
//     "uid": "user123",
//     "expiredTime": 1753380643035
//   }
// }
```

### Order Status

#### Get order status

```java
ApiResponse<OrderDetails> response = xpay.getOrderStatus("order-123");

// Response structure
// {
//   "code": 200,
//   "msg": "Success",
//   "data": {
//     "orderId": "order-123",
//     "orderType": "PAYOUT",
//     "status": "SUCCESS",
//     "reason": "",
//     "transaction": {
//       "chain": "TRON",
//       "symbol": "USDT",
//       "blockNum": 73971843,
//       "txid": "938d4d20f049bfe45f429f1c3cb62de7c57d3f7505ae691b79aa9a024f23ef87",
//       "contractAddress": "TR7NHqjeKQxGTCi8q8ZY4pL8otSzgjLj6t",
//       "from": "TGyjjt1esfqJWrPncpygq3QA43epY46V8D",
//       "to": "TXmVthgn6yT1kANGJHTHcbEGEKYDLLGJGp",
//       "amount": "100.00000000",
//       "timestamp": 1752573867000,
//       "txGas": "27.35985",
//       "confirmedNum": 196573,
//       "status": "SUCCESS"
//     }
//   }
// }
```

### Supported Symbols

#### Get supported symbols

```java
// Get all supported symbols
ApiResponse<List<SupportedSymbol>> allSymbols = xpay.getSupportedSymbols();

// Get symbols for a specific chain
ApiResponse<List<SupportedSymbol>> tronSymbols = xpay.getSupportedSymbols("TRON", null);

// Get a specific symbol on a specific chain
ApiResponse<List<SupportedSymbol>> tronUsdt = xpay.getSupportedSymbols("TRON", "USDT");

// Response structure
// {
//   "code": 200,
//   "msg": "Success",
//   "data": [
//     {
//       "symbol": "USDT",
//       "chain": "TRON",
//       "decimals": 6,
//       "contract": "TR7NHqjeKQxGTCi8q8ZY4pL8otSzgjLj6t",
//       "minAmount": 1,
//       "maxAmount": 100000
//     },
//     // Other supported symbols...
//   ]
// }
```

### Webhooks

#### Verify and parse webhook

```java
// Spring Boot controller example
@RestController
public class WebhookController {

    private final XPay xpay;

    public WebhookController(XPay xpay) {
        this.xpay = xpay;
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(@RequestBody String body) {
        try {
            WebhookEvent event = xpay.parseWebhook(body);
            
            if (event == null) {
                return ResponseEntity.badRequest().body("Invalid webhook signature or timestamp expired");
            }
            
            // Process the webhook event
            switch (event.getNotifyType()) {
                case ORDER_SUCCESS:
                    // Handle order success notification
                    OrderWebhookData orderData = (OrderWebhookData) event.getData();
                    System.out.println("Order " + orderData.getOrderId() + " completed successfully!");
                    break;
                case COLLECT_SUCCESS:
                    // Handle collection success notification
                    CollectWebhookData collectData = (CollectWebhookData) event.getData();
                    System.out.println("Collection completed successfully! Amount: " + collectData.getCollectAmount());
                    break;
                // Handle other notification types...
            }
            
            return ResponseEntity.ok("Webhook received");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error processing webhook: " + e.getMessage());
        }
    }
}
```

## Error Handling

The SDK throws `XPayApiException` for API errors:

```java
try {
    ApiResponse<PayoutData> response = xpay.createPayout(request);
} catch (XPayApiException e) {
    System.err.println("API Error: " + e.getMessage());
    System.err.println("Status Code: " + e.getStatusCode());
    System.err.println("Error Code: " + e.getErrorCode());
    System.err.println("Error Data: " + e.getErrorData());
} catch (Exception e) {
    System.err.println("General Error: " + e.getMessage());
}
```

## License

MIT