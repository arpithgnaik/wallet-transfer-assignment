package com.example.wallet.service;

import com.example.wallet.handler.dto.CreateTransferRequest;
import com.example.wallet.handler.dto.TransferResponse;
import com.example.wallet.model.TransferStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for TransferService business logic.
 *
 * Use mocked repositories to test service behaviour in isolation.
 * Focus on: idempotency, state transitions, validation rules.
 */
class TransferServiceTest {

    // TODO: inject mocks and instantiate TransferServiceImpl

    @BeforeEach
    void setUp() {
        // TODO: set up mock repositories and TransferServiceImpl
    }

    @Test
    @DisplayName("should return PROCESSED when transfer succeeds")
    void shouldReturnProcessedOnSuccess() {
        // TODO: arrange wallets with sufficient balance
        // TODO: act — call transferService.execute(request)
        // TODO: assert status == PROCESSED
    }

    @Test
    @DisplayName("should return FAILED when source wallet has insufficient funds")
    void shouldReturnFailedOnInsufficientFunds() {
        // TODO: arrange wallet with balance lower than transfer amount
        // TODO: act
        // TODO: assert status == FAILED
    }

    @Test
    @DisplayName("should return original transfer on duplicate idempotency key")
    void shouldReturnOriginalTransferOnDuplicateKey() {
        // TODO: arrange existing transfer in repository
        // TODO: act — call execute with the same idempotency key
        // TODO: assert returned id matches original, no new writes occurred
    }

    @Test
    @DisplayName("should not create duplicate ledger entries on retry")
    void shouldNotCreateDuplicateLedgerEntriesOnRetry() {
        // TODO: call execute twice with same idempotency key
        // TODO: verify insertDebit/insertCredit called exactly once
    }

    @Test
    @DisplayName("should reject transfer when source and destination wallets are the same")
    void shouldRejectSelfTransfer() {
        // TODO: arrange request with same fromWalletId and toWalletId
        // TODO: assert appropriate exception or FAILED status
    }
}

