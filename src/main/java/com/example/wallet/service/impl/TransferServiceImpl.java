package com.example.wallet.service.impl;

import com.example.wallet.handler.dto.CreateTransferRequest;
import com.example.wallet.handler.dto.TransferResponse;
import com.example.wallet.repository.LedgerRepository;
import com.example.wallet.repository.TransferRepository;
import com.example.wallet.repository.WalletRepository;
import com.example.wallet.service.TransferService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

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
        // TODO: implement transfer workflow
        //
        // Step 1: Idempotency check (outside transaction — fast path)
        //         transferRepository.findByIdempotencyKey(request.idempotencyKey())
        //         If found → return TransferResponse.from(existing, false)
        //
        // Step 2: transactionTemplate.execute(status -> { ... })
        //
        // Step 3 (inside tx): INSERT transfer with PENDING status
        //         Handle ON CONFLICT for concurrent duplicate requests
        //
        // Step 4: Lock wallets in sorted order to prevent deadlocks
        //         walletRepository.lockForUpdate(sorted list of wallet IDs)
        //
        // Step 5: Balance check
        //         If insufficient → updateStatus(FAILED), commit, return FAILED response
        //
        // Step 6: walletRepository.debit(fromWalletId, amount)
        //         walletRepository.credit(toWalletId, amount)
        //
        // Step 7: ledgerRepository.insertDebit(...)
        //         ledgerRepository.insertCredit(...)
        //
        // Step 8: updateStatus(PROCESSED), return PROCESSED response
        throw new UnsupportedOperationException("Not yet implemented");
    }
}

