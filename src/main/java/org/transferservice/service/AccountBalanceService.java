package org.transferservice.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.transferservice.model.Account;
import org.transferservice.repository.AccountRepository;

@Service
@RequiredArgsConstructor
public class AccountBalanceService {

    private final AccountRepository accountRepository;
    public Double getBalance(String accountNumber){
        Account acc = this.accountRepository.findByAccountNumber(accountNumber);
        return acc.getBalance();
    }
}
