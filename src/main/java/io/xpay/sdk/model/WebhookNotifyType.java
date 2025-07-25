package io.xpay.sdk.model;

/**
 * Webhook Notification Types
 */
public enum WebhookNotifyType {
    ORDER_PENDING,
    ORDER_PENDING_CONFIRMATION,
    ORDER_SUCCESS,
    ORDER_FAILED,
    ORDER_EXPIRED,
    COLLECT_PENDING,
    COLLECT_SUCCESS,
    COLLECT_FAILED
}