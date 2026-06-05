package com.example.wallet.repository.impl;

import com.example.wallet.model.LedgerEntry;
import com.example.wallet.repository.LedgerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class LedgerRepositoryImpl implements LedgerRepository {

    private final JdbcClient jdbcClient;

    @Override
    public void insertDebit(UUID transferId, String walletId, BigDecimal amount) {
        // TODO: implement
        // INSERT INTO ledger_entries (id, transfer_id, wallet_id, type, amount)
        // VALUES (gen_random_uuid(), :transferId, :walletId, 'DEBIT', :amount)
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void insertCredit(UUID transferId, String walletId, BigDecimal amount) {
        // TODO: implement
        // INSERT INTO ledger_entries (id, transfer_id, wallet_id, type, amount)
        // VALUES (gen_random_uuid(), :transferId, :walletId, 'CREDIT', :amount)
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public List<LedgerEntry> findByTransferId(UUID transferId) {
        // TODO: implement — SELECT * FROM ledger_entries WHERE transfer_id = :transferId
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int countByTransferId(UUID transferId) {
        // TODO: implement — SELECT COUNT(*) FROM ledger_entries WHERE transfer_id = :transferId
        throw new UnsupportedOperationException("Not yet implemented");
    }
}

