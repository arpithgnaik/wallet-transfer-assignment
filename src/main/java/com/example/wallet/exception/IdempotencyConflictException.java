package com.example.wallet.exception;

/**
 * Thrown when an idempotency key is reused with a different payload.
 * The system should return the original result, not re-execute.
 */
public class IdempotencyConflictException extends RuntimeException {

    public IdempotencyConflictException(String idempotencyKey) {
        super("Idempotency key already used with a different payload: " + idempotencyKey);
    }
}

