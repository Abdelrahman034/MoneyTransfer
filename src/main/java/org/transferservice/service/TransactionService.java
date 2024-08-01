package org.transferservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Service;
import org.transferservice.dto.AccountDTO;
import org.transferservice.dto.CustomerDTO;
import org.transferservice.dto.TransactionDTO;
import org.transferservice.dto.enums.TransactionStatus;
import org.transferservice.exception.custom.CustomerNotFoundException;
import org.transferservice.model.Account;
import org.transferservice.model.Transaction;
import org.transferservice.repository.AccountRepository;
import org.transferservice.repository.CustomerRepository;
import org.transferservice.repository.TransactionRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public List<TransactionDTO> getTransactionHistory(String  accountId, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Transaction> transactionsPage = transactionRepository.findBySenderAccountNumberOrRecipientAccountNumber(accountId,accountId,pageable);
        List<Transaction> transactions = transactionsPage.getContent();
        List<TransactionDTO> res = new ArrayList<>();
        transactions.forEach(transaction -> res.add(transaction.toDTO()));
        return res;

    }

    public List<TransactionDTO> getTransactionHistoryWithFilters(String accountId, LocalDateTime startDate, LocalDateTime endDate, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Transaction> transactionsPage = transactionRepository.findBySenderAccountNumberOrRecipientAccountNumberAndTransactionTimeBetween(accountId,accountId,startDate, endDate, pageable);
        List<Transaction> transactions = transactionsPage.getContent();
        List<TransactionDTO> res = new ArrayList<>();
        transactions.forEach(transaction -> res.add(transaction.toDTO()));
        return res;
    }

    public Transaction addTransaction(CustomerDTO senderCustomer, String recipientAccountNumber, TransactionStatus status, Double ammount){
        Transaction transaction  = Transaction
                .builder()
                .senderAccountNumber(senderCustomer.getAccount().getAccountNumber())
                .recipientAccountNumber(recipientAccountNumber)
                .status(status)
                .amount(ammount)
                .build();
        return this.transactionRepository.save(transaction);
    }

    public TransactionStatus isValidTransaction(CustomerDTO senderCustomer, CustomerDTO recipientCustomer, Double amount) throws Exception{
        AccountDTO senderAccount = senderCustomer.getAccount();
        AccountDTO recipientAccount = recipientCustomer.getAccount();
        if(Objects.equals(senderAccount.getAccountNumber(), recipientAccount.getAccountNumber())
            || (senderAccount.getBalance()<amount || amount<0) ){
            return TransactionStatus.FAILED;
        }
        Account fromAccount = this.accountRepository.findById(senderAccount.getId())
                .orElseThrow(()-> new CustomerNotFoundException("Customer does not have an account"));

        Account toAccount = this.accountRepository.findById(recipientAccount.getId())
                .orElseThrow(() -> new CustomerNotFoundException("Customer does not have an account"));

        fromAccount.setBalance(fromAccount.getBalance()-amount);
        toAccount.setBalance(toAccount.getBalance()+amount);

        this.accountRepository.save(fromAccount);
        this.accountRepository.save(toAccount);
        return TransactionStatus.SUCCESS;
    }

}