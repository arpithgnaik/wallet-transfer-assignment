package com.example.wallet.repository;

import com.example.wallet.handler.dto.CreateTransferRequest;
import com.example.wallet.model.Transfer;
import com.example.wallet.model.TransferStatus;

import java.util.Optional;
import java.util.UUID;

public interface TransferRepository {

    /**
     * Find a transfer by its idempotency key.
     * Used to detect duplicate requests before executing any side effects.
     */
    Optional<Transfer> findByIdempotencyKey(String idempotencyKey);

    /**
     * Find a transfer by its ID.
     */
    Optional<Transfer> findById(UUID id);

    /**
     * Insert a new transfer in PENDING status.
     * Must handle concurrent inserts with the same idempotency key gracefully
     * (e.g. via ON CONFLICT DO NOTHING + re-read).
     */
    Transfer insertPending(CreateTransferRequest request);

    /**
     * Update the status of an existing transfer.
     * Only valid transitions: PENDING -> PROCESSED, PENDING -> FAILED.
     */
    void updateStatus(UUID transferId, TransferStatus status);
}

