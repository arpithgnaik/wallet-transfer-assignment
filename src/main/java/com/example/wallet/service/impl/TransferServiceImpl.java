package com.example.wallet.service.impl;

import com.example.wallet.exception.WalletNotFoundException;
import com.example.wallet.handler.dto.CreateTransferRequest;
import com.example.wallet.handler.dto.TransferResponse;
import com.example.wallet.model.Transfer;
import com.example.wallet.model.TransferStatus;
import com.example.wallet.model.Wallet;
import com.example.wallet.repository.LedgerRepository;
import com.example.wallet.repository.TransferRepository;
import com.example.wallet.repository.WalletRepository;
import com.example.wallet.service.TransferService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransferServiceImpl implements TransferService {

    private final TransferRepository transferRepository;
    private final WalletRepository walletRepository;
    private final LedgerRepository ledgerRepository;
    private final TransactionTemplate transactionTemplate;

    @Override
    public TransferResponse execute(CreateTransferRequest request) {
        // Step 1: Idempotency fast-path — outside transaction to avoid unnecessary locking
        Optional<Transfer> existing = transferRepository.findByIdempotencyKey(request.idempotencyKey());
        if (existing.isPresent()) {
            log.debug("Idempotent replay for key={} transferId={}", request.idempotencyKey(), existing.get().id());
            return TransferResponse.from(existing.get(), false);
        }

        // Step 2: Execute the full transfer workflow inside a single transaction
        TransferResponse response = transactionTemplate.execute(status -> {

            // Step 3: Insert transfer in PENDING state.
            // insertPending handles ON CONFLICT (idempotency_key) — if a concurrent request
            // already inserted it, we re-read and return that record.
            Transfer transfer = transferRepository.insertPending(request);

            // Re-check after insert: concurrent request may have already completed it
            if (transfer.status() != TransferStatus.PENDING) {
                log.debug("Transfer already processed, returning existing result transferId={}", transfer.id());
                return TransferResponse.from(transfer, false);
            }

            // Step 4: Lock wallets in consistent alphabetical order to prevent deadlocks.
            // e.g. wallet_A and wallet_B always locked as [wallet_A, wallet_B] regardless
            // of which is the source or destination.
            List<String> sortedIds = List.of(request.fromWalletId(), request.toWalletId())
                    .stream()
                    .sorted()
                    .toList();

            List<Wallet> lockedWallets = walletRepository.lockForUpdate(sortedIds);

            Wallet fromWallet = lockedWallets.stream()
                    .filter(w -> w.id().equals(request.fromWalletId()))
                    .findFirst()
                    .orElseThrow(() -> new WalletNotFoundException(request.fromWalletId()));

            // Ensure destination wallet exists
            lockedWallets.stream()
                    .filter(w -> w.id().equals(request.toWalletId()))
                    .findFirst()
                    .orElseThrow(() -> new WalletNotFoundException(request.toWalletId()));

            // Step 5: Balance check — mark FAILED and commit without ledger entries
            if (fromWallet.balance().compareTo(request.amount()) < 0) {
                log.warn("Insufficient funds walletId={} balance={} requested={}",
                        fromWallet.id(), fromWallet.balance(), request.amount());
                transferRepository.updateStatus(transfer.id(), TransferStatus.FAILED);
                return TransferResponse.from(transfer.withStatus(TransferStatus.FAILED), true);
            }

            // Step 6: Update balances (wallet-level constraint CHECK balance >= 0 is the last guardrail)
            walletRepository.debit(request.fromWalletId(), request.amount());
            walletRepository.credit(request.toWalletId(), request.amount());

            // Step 7: Write double-entry ledger — UNIQUE (transfer_id, type) constraint
            // guarantees exactly one DEBIT + one CREDIT even under retries
            ledgerRepository.insertDebit(transfer.id(), request.fromWalletId(), request.amount());
            ledgerRepository.insertCredit(transfer.id(), request.toWalletId(), request.amount());

            // Step 8: Mark PROCESSED
            transferRepository.updateStatus(transfer.id(), TransferStatus.PROCESSED);

            log.info("Transfer completed transferId={} from={} to={} amount={}",
                    transfer.id(), request.fromWalletId(), request.toWalletId(), request.amount());

            return TransferResponse.from(transfer.withStatus(TransferStatus.PROCESSED), true);
        });

        if (response == null) {
            throw new IllegalStateException("Transaction returned null — this should never happen");
        }

        return response;
    }
}
