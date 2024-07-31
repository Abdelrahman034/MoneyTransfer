package org.transferservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.transferservice.dto.TransactionDTO;
import org.transferservice.model.Transaction;
import org.transferservice.repository.TransactionRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public List<TransactionDTO> getTransactionHistory(Long accountId, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Transaction> transactionsPage = transactionRepository.findByid(accountId,pageable);

        List<Transaction> transactions = transactionsPage.getContent();

        List<TransactionDTO> res = new ArrayList<>();
        transactions.forEach(transaction -> res.add(transaction.toDTO()));
        return res;

    }

    public List<TransactionDTO> getTransactionHistoryWithFilters(Long accountId, LocalDateTime startDate, LocalDateTime endDate, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Transaction> transactionsPage = transactionRepository.findByidAndTransactionTimeBetween(accountId,startDate, endDate, pageable);

        List<Transaction> transactions = transactionsPage.getContent();

        List<TransactionDTO> res = new ArrayList<>();
        transactions.forEach(transaction -> res.add(transaction.toDTO()));
        return res;
    }

}