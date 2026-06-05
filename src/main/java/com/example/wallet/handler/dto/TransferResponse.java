package com.example.wallet.handler.dto;

import com.example.wallet.model.Transfer;
import com.example.wallet.model.TransferStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record TransferResponse(
        UUID id,
        String idempotencyKey,
        String fromWalletId,
        String toWalletId,
        BigDecimal amount,
        TransferStatus status,
        Instant createdAt,
        boolean newlyCreated
) {

    /**
     * Maps a Transfer domain model to a response DTO.
     *
     * @param transfer    the domain transfer
     * @param newlyCreated true when the transfer was just created (201), false for idempotent replay (200)
     */
    public static TransferResponse from(Transfer transfer, boolean newlyCreated) {
        return new TransferResponse(
                transfer.id(),
                transfer.idempotencyKey(),
                transfer.fromWalletId(),
                transfer.toWalletId(),
                transfer.amount(),
                transfer.status(),
                transfer.createdAt(),
                newlyCreated
        );
    }
}

