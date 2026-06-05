package com.example.wallet.repository;

import com.example.wallet.model.Wallet;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface WalletRepository {

    /**
     * Find a wallet by its ID.
     */
    Optional<Wallet> findById(String walletId);

    /**
     * Acquire row-level locks on the given wallet IDs in the order provided.
     * Callers must pass IDs sorted consistently (e.g. alphabetically)
     * to prevent deadlocks under concurrent transactions.
     *
     * Must be called within an active transaction.
     */
    List<Wallet> lockForUpdate(List<String> walletIds);

    /**
     * Decrease the wallet balance by the given amount.
     * Assumes the caller holds a row-level lock on this wallet.
     */
    void debit(String walletId, BigDecimal amount);

    /**
     * Increase the wallet balance by the given amount.
     * Assumes the caller holds a row-level lock on this wallet.
     */
    void credit(String walletId, BigDecimal amount);
}

