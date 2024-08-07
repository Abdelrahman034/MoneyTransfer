package org.transferservice.controller;


import io.jsonwebtoken.JwtException;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.transferservice.dto.CustomerDTO;
import org.transferservice.exception.custom.AccountNotFoundException;
import org.transferservice.exception.custom.CustomerNotFoundException;
import org.transferservice.service.AccountBalanceService;
import org.transferservice.service.CustomerService;
import org.transferservice.service.security.JwtUtils;


@RestController
@RequestMapping("/api/balance")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Data
@RequiredArgsConstructor
@Component
public class AccountBalanceController {

    private final JwtUtils jwtUtils;
    private final CustomerService customerService;
    private final AccountBalanceService accountBalanceService;
    @GetMapping
    public Double getBalance(@RequestHeader("Authorization") String authorizationHeader) throws AccountNotFoundException,CustomerNotFoundException{
        authorizationHeader = authorizationHeader.substring(7);
        String customerEmail = this.jwtUtils.getUserNameFromJwtToken(authorizationHeader);
        CustomerDTO customer = this.customerService.checkCustomerEmail(customerEmail);
        String accountNumber = customer.getAccount().getAccountNumber();
        if(accountNumber==null){
            throw new AccountNotFoundException(String.format("Customer with id %s doesn't have an account",customer.getId()));
        }
        return accountBalanceService.getBalance(accountNumber);
    }

}
