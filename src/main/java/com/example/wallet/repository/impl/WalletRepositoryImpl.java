package com.example.wallet.repository.impl;

import com.example.wallet.model.Wallet;
import com.example.wallet.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class WalletRepositoryImpl implements WalletRepository {

    private final JdbcClient jdbcClient;

    @Override
    public Optional<Wallet> findById(String walletId) {
        return jdbcClient.sql("""
                        SELECT id, balance, created_at
                        FROM wallets
                        WHERE id = :id
                        """)
                .param("id", walletId)
                .query(WalletRepositoryImpl::mapRow)
                .optional();
    }

    @Override
    public List<Wallet> lockForUpdate(List<String> walletIds) {
        if (walletIds.isEmpty()) {
            return Collections.emptyList();
        }

        // Build a positional IN clause — JdbcClient does not support Collection binding
        // for FOR UPDATE queries directly. We use a plain string with named params per id.
        // IDs must already be sorted by the caller (alphabetically) to guarantee
        // consistent lock ordering and prevent deadlocks.
        String placeholders = walletIds.stream()
                .map(id -> "'" + id.replace("'", "''") + "'")   // escape single quotes
                .collect(Collectors.joining(", "));

        return jdbcClient.sql("""
                        SELECT id, balance, created_at
                        FROM wallets
                        WHERE id IN (%s)
                        ORDER BY id
                        FOR UPDATE
                        """.formatted(placeholders))
                .query(WalletRepositoryImpl::mapRow)
                .list();
    }

    @Override
    public void debit(String walletId, BigDecimal amount) {
        jdbcClient.sql("""
                        UPDATE wallets
                        SET balance = balance - :amount
                        WHERE id = :id
                        """)
                .param("amount", amount)
                .param("id", walletId)
                .update();
    }

    @Override
    public void credit(String walletId, BigDecimal amount) {
        jdbcClient.sql("""
                        UPDATE wallets
                        SET balance = balance + :amount
                        WHERE id = :id
                        """)
                .param("amount", amount)
                .param("id", walletId)
                .update();
    }

    private static Wallet mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Wallet(
                rs.getString("id"),
                rs.getBigDecimal("balance"),
                rs.getTimestamp("created_at").toInstant()
        );
    }
}
