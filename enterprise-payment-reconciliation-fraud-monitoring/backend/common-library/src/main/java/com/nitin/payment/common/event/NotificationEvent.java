package com.nitin.payment.common.event;

import java.time.Instant;

public record NotificationEvent(String recipient, String type, String subject, String message, Instant createdAt) {
}
