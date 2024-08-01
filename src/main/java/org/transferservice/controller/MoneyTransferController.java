package org.transferservice.controller;


import io.swagger.v3.oas.annotations.media.Content;
import lombok.Data;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.transferservice.dto.AccountDTO;
import org.transferservice.dto.CustomerDTO;
import org.transferservice.dto.TransactionDTO;
import org.transferservice.dto.TransferRequestDTO;
import org.transferservice.dto.enums.TransactionStatus;
import org.transferservice.exception.custom.CustomerNotFoundException;
import org.transferservice.service.CustomerService;
import org.transferservice.service.TransactionService;
import org.transferservice.service.security.JwtUtils;

import java.util.Objects;

@Data
@Component
@RestController
@RequestMapping("/api/transfer")
@RequiredArgsConstructor
public class MoneyTransferController {

    private final CustomerService customerService;
    private final TransactionService transactionService;
    private final JwtUtils jwt;

    @PostMapping
    public TransactionDTO transferMoney(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody TransferRequestDTO transferRequest ) throws Exception{

        authorizationHeader = authorizationHeader.substring(7);
        String recipientAccountNumber = transferRequest.getRecipientAccountNumber();
        Double amount = transferRequest.getAmount();
        if((authorizationHeader) == null) {
            throw new CustomerNotFoundException("UnAuthorized");
        }
        String customerEmail = this.jwt.getUserNameFromJwtToken((authorizationHeader));
        CustomerDTO customer = this.customerService.checkCustomerEmail(customerEmail);
        CustomerDTO recipientCustomer = this.customerService.getCustomerByAccountNumber(recipientAccountNumber);
        TransactionStatus isValid = this.transactionService.isValidTransaction(customer,recipientCustomer,amount);
        return this.transactionService.addTransaction(customer,recipientAccountNumber,isValid,amount).toDTO();
    }

}
