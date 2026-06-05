package com.example.wallet.concurrency;

import com.example.wallet.handler.dto.CreateTransferRequest;
import com.example.wallet.handler.dto.TransferResponse;
import com.example.wallet.model.TransferStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Concurrency tests for wallet transfers.
 *
 * These tests verify that concurrent debits on the same wallet:
 *   - never result in a negative balance
 *   - never double-spend
 *   - produce a consistent ledger
 *
 * Requires a real database (Testcontainers via application-test.yml).
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class ConcurrentTransferTest {

    // TODO: inject transferService or use TestRestTemplate

    @BeforeEach
    void setUp() {
        // TODO: seed wallet_src with balance = 500, wallet_dst with balance = 0
    }

    @Test
    @DisplayName("concurrent debits on same wallet must never overdraft")
    void concurrentDebits_shouldNeverOverdraft() throws Exception {
        int threadCount = 10;
        BigDecimal amountEach = new BigDecimal("100");
        // wallet_src has 500 — only 5 transfers should PROCESS, 5 should FAIL

        ExecutorService pool = Executors.newFixedThreadPool(threadCount);
        CountDownLatch startLatch = new CountDownLatch(1);
        List<Future<TransferResponse>> futures = new ArrayList<>();

        for (int i = 0; i < threadCount; i++) {
            final String key = "concurrent-key-" + i;
            futures.add(pool.submit(() -> {
                startLatch.await(); // all threads start simultaneously
                // TODO: call transferService.execute(new CreateTransferRequest(key, "wallet_src", "wallet_dst", amountEach))
                throw new UnsupportedOperationException("Not yet implemented");
            }));
        }

        startLatch.countDown(); // release all threads at once

        List<TransferResponse> results = new ArrayList<>();
        for (Future<TransferResponse> f : futures) {
            results.add(f.get());
        }

        pool.shutdown();

        long processed = results.stream().filter(r -> r.status() == TransferStatus.PROCESSED).count();
        long failed    = results.stream().filter(r -> r.status() == TransferStatus.FAILED).count();

        // TODO: fetch final wallet balance
        // assertThat(finalBalance).isEqualByComparingTo("0.0000");
        assertThat(processed).isEqualTo(5);
        assertThat(failed).isEqualTo(5);
    }

    @Test
    @DisplayName("concurrent requests with same idempotency key must not duplicate transfer")
    void concurrentDuplicateIdempotencyKey_shouldExecuteOnlyOnce() throws Exception {
        int threadCount = 5;
        String sameKey = "idempotent-concurrent-key";

        ExecutorService pool = Executors.newFixedThreadPool(threadCount);
        CountDownLatch startLatch = new CountDownLatch(1);
        List<Future<TransferResponse>> futures = new ArrayList<>();

        for (int i = 0; i < threadCount; i++) {
            futures.add(pool.submit(() -> {
                startLatch.await();
                // TODO: call transferService.execute with sameKey
                throw new UnsupportedOperationException("Not yet implemented");
            }));
        }

        startLatch.countDown();

        List<TransferResponse> results = new ArrayList<>();
        for (Future<TransferResponse> f : futures) {
            results.add(f.get());
        }

        pool.shutdown();

        // All responses must refer to the same transfer
        long distinctIds = results.stream().map(TransferResponse::id).distinct().count();
        assertThat(distinctIds).isEqualTo(1);

        // TODO: assert exactly 2 ledger entries exist (not 2 * threadCount)
    }
}

