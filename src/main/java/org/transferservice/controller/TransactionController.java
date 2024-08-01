package org.transferservice.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.transferservice.dto.CustomerDTO;
import org.transferservice.dto.TransactionDTO;
import org.transferservice.exception.custom.CustomerNotFoundException;
import org.transferservice.model.Customer;
import org.transferservice.repository.CustomerRepository;
import org.transferservice.service.CustomerService;
import org.transferservice.service.TransactionService;
import org.transferservice.service.security.JwtUtils;

import java.time.LocalDateTime;
import java.util.List;


@RestController
@RequestMapping("/api/transactions")
@Component
@AllArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;
    private final CustomerService customerService;
    private final JwtUtils Jwt;
    @GetMapping
    public List<TransactionDTO> getTransactionHistory(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestParam Integer page,
            @RequestParam Integer size) throws CustomerNotFoundException {
        if((authorizationHeader) == null) {
            throw new CustomerNotFoundException("UnAuthorized");
        }
        String customerEmail = this.Jwt.getUserNameFromJwtToken((authorizationHeader));
        CustomerDTO customer = this.customerService.checkCustomerEmail(customerEmail);
        return transactionService.getTransactionHistory(customer.getAccount().getAccountNumber(),page, size);
    }

    @GetMapping("/filtered")
    public List<TransactionDTO> getTransactionHistoryWithFilters(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestParam LocalDateTime startDate,
            @RequestParam LocalDateTime endDate,
            @RequestParam Integer page,
            @RequestParam Integer size) throws CustomerNotFoundException{

        authorizationHeader = authorizationHeader.substring(7);

        if((authorizationHeader) == null) {
            throw new CustomerNotFoundException("UnAuthorized");
        }

        String customerEmail = this.Jwt.getUserNameFromJwtToken((authorizationHeader));
        CustomerDTO customer = this.customerService.checkCustomerEmail(customerEmail);
        return transactionService.getTransactionHistoryWithFilters(customer.getAccount().getAccountNumber(), startDate,endDate, page, size);
    }

}