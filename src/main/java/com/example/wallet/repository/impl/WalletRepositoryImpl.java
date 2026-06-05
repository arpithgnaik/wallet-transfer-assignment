package com.example.wallet.repository.impl;

import com.example.wallet.model.Wallet;
import com.example.wallet.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class WalletRepositoryImpl implements WalletRepository {

    private final JdbcClient jdbcClient;

    @Override
    public Optional<Wallet> findById(String walletId) {
        // TODO: implement
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public List<Wallet> lockForUpdate(List<String> walletIds) {
        // TODO: implement — SELECT ... FOR UPDATE ORDER BY id
        // Lock order must be consistent across all callers to prevent deadlocks
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void debit(String walletId, BigDecimal amount) {
        // TODO: implement — UPDATE wallets SET balance = balance - :amount WHERE id = :id
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void credit(String walletId, BigDecimal amount) {
        // TODO: implement — UPDATE wallets SET balance = balance + :amount WHERE id = :id
        throw new UnsupportedOperationException("Not yet implemented");
    }
}

