package com.nitin.payment.common.exception;

public class KafkaProcessingException extends RuntimeException {
    public KafkaProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
