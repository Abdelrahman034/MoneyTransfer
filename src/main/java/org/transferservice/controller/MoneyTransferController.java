package org.transferservice.controller;


import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.bind.annotation.*;
import org.transferservice.dto.AccountDTO;
import org.transferservice.dto.CustomerDTO;
import org.transferservice.dto.TransactionDTO;
import org.transferservice.dto.TransferRequestDTO;
import org.transferservice.dto.enums.TransactionStatus;
import org.transferservice.exception.custom.CustomerNotFoundException;
import org.transferservice.model.Account;
import org.transferservice.model.Customer;
import org.transferservice.repository.CustomerRepository;
import org.transferservice.service.CustomerService;
import org.transferservice.service.TransactionService;
import org.transferservice.service.security.JwtUtils;

import static org.transferservice.service.security.JwtUtils.extractToken;

@Data
@RestController
@RequestMapping("/api/transfer")
@AllArgsConstructor
public class MoneyTransferController {

    private final CustomerService customerService;
    private final TransactionService transactionService;
    private final JwtUtils jwt;

    @PostMapping
    public TransactionDTO transferMoney(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody TransferRequestDTO transferRequest ) throws Exception{

        Long recipientId = transferRequest.getRecipientId();
        Double amount = transferRequest.getAmount();
        String token = extractToken(authorizationHeader);
        if(token == null) {
            throw new CustomerNotFoundException("UnAuthorized");
        }

        String customerEmail = this.jwt.getUserNameFromJwtToken(token);
        CustomerDTO customer = this.customerService.checkCustomerEmail(customerEmail);
        CustomerDTO recipientCustomer = this.customerService.getCustomerById(recipientId);


        AccountDTO senderAccount = customer.getAccount();
        AccountDTO recipientAccount = recipientCustomer.getAccount();
        TransactionStatus isValid = this.customerService.isValidTransaction(senderAccount,recipientAccount,amount);
        return this.transactionService.addTransaction(senderAccount.getId(),recipientId,isValid,amount).toDTO();
    }

}
