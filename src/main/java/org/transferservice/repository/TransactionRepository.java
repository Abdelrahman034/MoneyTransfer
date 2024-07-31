package org.transferservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.transferservice.model.Transaction;

import java.time.LocalDateTime;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    Page<Transaction> findByAccountId(Long senderAccountId,Pageable pageable);

    Page<Transaction> findByAccountIdAndTransactionTimeBetween(Long senderAccountId, Long recipientAccountId, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
}