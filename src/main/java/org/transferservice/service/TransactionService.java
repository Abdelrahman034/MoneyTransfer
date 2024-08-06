package org.transferservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Service;
import org.transferservice.dto.AccountDTO;
import org.transferservice.dto.CustomerDTO;
import org.transferservice.dto.TransactionDTO;
import org.transferservice.dto.enums.AccountCurrency;
import org.transferservice.dto.enums.TransactionStatus;
import org.transferservice.exception.custom.CustomerNotFoundException;
import org.transferservice.exception.custom.TransactionNotFoundException;
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
    private final CustomerService customerService;

    public List<TransactionDTO> getTransactionHistory(String  accountId, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "transactionTime"));
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

    public Transaction addTransaction(CustomerDTO customer, String recipientAccountNumber, TransactionStatus status, Double amount, AccountCurrency currency) throws CustomerNotFoundException {
        CustomerDTO recipientCustomer = this.customerService.getCustomerByAccountNumber(recipientAccountNumber);
        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setCurrency(currency);
        transaction.setRecipientAccountNumber(recipientAccountNumber);
        transaction.setRecipientName(recipientCustomer.getUserName());
        transaction.setSenderAccountNumber(customer.getAccount().getAccountNumber());
        transaction.setSenderName(customer.getUserName());
        transaction.setStatus(status);
        transaction.setTransactionTime(LocalDateTime.now());

        return this.transactionRepository.save(transaction);
    }

    public Double amountConversion(Double amount,AccountCurrency senderCurrency,AccountCurrency recipientCurrency){
        final double EGP_TO_USD = 0.032;
        final double USD_TO_EGP = 31.25;
        final double EGP_TO_EUR = 0.029;
        final double EUR_TO_EGP = 34.48;
        final double USD_TO_EUR = 0.91;
        final double EUR_TO_USD = 1.1;

            if (senderCurrency == recipientCurrency) {
                return amount;
            }

            switch (senderCurrency) {
                case EGP:
                    if (recipientCurrency == AccountCurrency.USD) {
                        return amount * EGP_TO_USD;
                    } else if (recipientCurrency == AccountCurrency.EUR) {
                        return amount * EGP_TO_EUR;
                    }
                    break;
                case USD:
                    if (recipientCurrency == AccountCurrency.EGP) {
                        return amount * USD_TO_EGP;
                    } else if (recipientCurrency == AccountCurrency.EUR) {
                        return amount * USD_TO_EUR;
                    }
                    break;
                case EUR:
                    if (recipientCurrency == AccountCurrency.EGP) {
                        return amount * EUR_TO_EGP;
                    } else if (recipientCurrency == AccountCurrency.USD) {
                        return amount * EUR_TO_USD;
                    }
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported currency: " + senderCurrency);
            }

            throw new IllegalArgumentException("Unsupported currency conversion: " + senderCurrency + " to " + recipientCurrency);
        }



    public TransactionStatus isValidTransaction(CustomerDTO senderCustomer, CustomerDTO recipientCustomer, Double amount, AccountCurrency currency) throws Exception {
        AccountDTO senderAccount = senderCustomer.getAccount();
        AccountDTO recipientAccount = recipientCustomer.getAccount();


        if (Objects.equals(senderAccount.getAccountNumber(), recipientAccount.getAccountNumber())
                || (senderAccount.getBalance() < amount || amount < 0)) {
            return TransactionStatus.Failed;
        }
        double newAmount = amountConversion(amount,senderAccount.getCurrency(),recipientAccount.getCurrency());

        Account fromAccount = this.accountRepository.findById(senderAccount.getId())
                .orElseThrow(() -> new CustomerNotFoundException("Customer does not have an account"));

        Account toAccount = this.accountRepository.findById(recipientAccount.getId())
                .orElseThrow(() -> new CustomerNotFoundException("Customer does not have an account"));

        fromAccount.setBalance(fromAccount.getBalance() - amount);
        toAccount.setBalance(toAccount.getBalance() + newAmount);

        this.accountRepository.save(fromAccount);
        this.accountRepository.save(toAccount);

        return TransactionStatus.Successful;
    }

    public TransactionDTO getTransactionById(Long transactionId) throws TransactionNotFoundException {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new TransactionNotFoundException("Transaction not found"));
        return transaction.toDTO();
    }

}