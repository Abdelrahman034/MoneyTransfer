package org.transferservice.controller;


import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.transferservice.dto.CustomerDTO;
import org.transferservice.dto.TransactionDTO;
import org.transferservice.dto.TransferRequestDTO;
import org.transferservice.dto.enums.AccountCurrency;
import org.transferservice.dto.enums.TransactionStatus;
import org.transferservice.exception.custom.CustomerNotFoundException;
import org.transferservice.service.CustomerService;
import org.transferservice.service.TransactionService;
import org.transferservice.service.security.JwtUtils;


@Data
@Component
@RestController
@RequestMapping("/api/transfer")
@CrossOrigin(origins = "*", allowedHeaders = "*")
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
        AccountCurrency currency = transferRequest.getCurrency();

        if((authorizationHeader) == null) {
            throw new CustomerNotFoundException("UnAuthorized");
        }

        String customerEmail = this.jwt.getUserNameFromJwtToken((authorizationHeader));
        CustomerDTO customer = this.customerService.checkCustomerEmail(customerEmail);
        CustomerDTO recipientCustomer = this.customerService.getCustomerByAccountNumber(recipientAccountNumber);
        TransactionStatus isValid = this.transactionService.isValidTransaction(customer,recipientCustomer,amount,currency);
        return this.transactionService.addTransaction(customer,recipientAccountNumber,isValid,amount,currency).toDTO();
    }

}
