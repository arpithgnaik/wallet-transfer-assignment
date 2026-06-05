package com.example.wallet.integration;

import com.example.wallet.handler.dto.CreateTransferRequest;
import com.example.wallet.handler.dto.TransferResponse;
import com.example.wallet.model.TransferStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for POST /transfers.
 *
 * Uses Testcontainers (configured via application-test.yml with tc: JDBC URL).
 * Tests run against a real PostgreSQL instance with Flyway migrations applied.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class TransferIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        // TODO: reset wallet balances to known state before each test
        // e.g. truncate ledger_entries, transfers; reset wallet balances
    }

    @Test
    @DisplayName("POST /transfers — should debit source and credit destination wallet")
    void shouldDebitAndCreditOnSuccessfulTransfer() {
        // TODO: arrange — wallets with known balances
        // TODO: act — POST /transfers
        // TODO: assert 201, status == PROCESSED
        // TODO: assert source balance decreased, destination balance increased
        // TODO: assert exactly 2 ledger entries (1 DEBIT + 1 CREDIT)
    }

    @Test
    @DisplayName("POST /transfers — duplicate idempotency key should return original response")
    void shouldReturnOriginalResponseOnDuplicateIdempotencyKey() {
        // TODO: POST /transfers once → 201
        // TODO: POST /transfers again with same idempotencyKey → 200
        // TODO: assert both responses have the same transfer id and status
        // TODO: assert wallet balances changed only once
    }

    @Test
    @DisplayName("POST /transfers — should return FAILED on insufficient funds")
    void shouldReturnFailedOnInsufficientFunds() {
        // TODO: arrange wallet with balance of 10
        // TODO: POST /transfers with amount 100
        // TODO: assert response status == FAILED (or 422)
        // TODO: assert no ledger entries were created
        // TODO: assert wallet balance unchanged
    }

    @Test
    @DisplayName("POST /transfers — should return 400 on invalid request")
    void shouldReturn400OnInvalidRequest() {
        // TODO: POST /transfers with missing idempotencyKey or negative amount
        // TODO: assert 400 Bad Request
    }

    @Test
    @DisplayName("POST /transfers — ledger entries must balance (DEBIT == CREDIT)")
    void ledgerEntriesMustBalance() {
        // TODO: execute a transfer
        // TODO: fetch ledger entries for the transfer
        // TODO: assert SUM(DEBIT) == SUM(CREDIT) == transfer amount
    }
}

