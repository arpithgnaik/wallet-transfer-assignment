package com.example.wallet.repository.impl;

import com.example.wallet.handler.dto.CreateTransferRequest;
import com.example.wallet.model.Transfer;
import com.example.wallet.model.TransferStatus;
import com.example.wallet.repository.TransferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class TransferRepositoryImpl implements TransferRepository {

    private final JdbcClient jdbcClient;

    @Override
    public Optional<Transfer> findByIdempotencyKey(String idempotencyKey) {
        // TODO: implement — SELECT * FROM transfers WHERE idempotency_key = :key
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Optional<Transfer> findById(UUID id) {
        // TODO: implement — SELECT * FROM transfers WHERE id = :id
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Transfer insertPending(CreateTransferRequest request) {
        // TODO: implement
        // INSERT INTO transfers (id, idempotency_key, from_wallet_id, to_wallet_id, amount, status)
        // VALUES (:id, :key, :from, :to, :amount, 'PENDING')
        // ON CONFLICT (idempotency_key) DO NOTHING
        // then re-read to return the winning row
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void updateStatus(UUID transferId, TransferStatus status) {
        // TODO: implement — UPDATE transfers SET status = :status, updated_at = now() WHERE id = :id
        throw new UnsupportedOperationException("Not yet implemented");
    }
}

