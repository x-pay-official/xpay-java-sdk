package io.xpay.sdk.exception;

import lombok.Getter;

/**
 * Exception thrown when the X-Pay API returns an error
 */
@Getter
public class XPayApiException extends Exception {
    /**
     * HTTP status code
     */
    private final int statusCode;
    
    /**
     * API error code
     */
    private final int errorCode;
    
    /**
     * Additional error data
     */
    private final Object errorData;
    
    /**
     * Create a new XPayApiException
     * @param message - Error message
     * @param statusCode - HTTP status code
     * @param errorCode - API error code
     * @param errorData - Additional error data
     */
    public XPayApiException(String message, int statusCode, int errorCode, Object errorData) {
        super(message);
        this.statusCode = statusCode;
        this.errorCode = errorCode;
        this.errorData = errorData;
    }
    
    /**
     * Create a new XPayApiException
     * @param message - Error message
     * @param statusCode - HTTP status code
     */
    public XPayApiException(String message, int statusCode) {
        this(message, statusCode, 0, null);
    }
    
    /**
     * Create a new XPayApiException
     * @param message - Error message
     */
    public XPayApiException(String message) {
        this(message, 0, 0, null);
    }
}