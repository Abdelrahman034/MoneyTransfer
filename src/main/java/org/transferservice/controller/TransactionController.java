package org.transferservice.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.transferservice.dto.TransactionDTO;
import org.transferservice.exception.custom.CustomerNotFoundException;
import org.transferservice.model.Customer;
import org.transferservice.repository.CustomerRepository;
import org.transferservice.service.TransactionService;
import org.transferservice.service.security.JwtUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


import static org.transferservice.service.security.JwtUtils.extractToken;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;
    private final CustomerRepository customerRepository;

    @GetMapping
    public List<TransactionDTO> getTransactionHistory(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestParam Integer page,
            @RequestParam Integer size) throws CustomerNotFoundException {
        String token = extractToken(authorizationHeader);
        if(token == null) {
            throw new CustomerNotFoundException("UnAuthorized");
        }

        JwtUtils jwtUtils = new JwtUtils();
        String customerEmail = jwtUtils.getUserNameFromJwtToken(token);
        Customer customer = this.customerRepository.findUserByEmail(customerEmail)
                .orElseThrow(() -> new CustomerNotFoundException(String.format("Customer with email %s not found", customerEmail)));

        return transactionService.getTransactionHistory(customer.getId(),page, size);
    }

    @GetMapping("/filtered")
    public ResponseEntity<List<TransactionDTO>> getTransactionHistoryWithFilters(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestParam LocalDateTime startDate,
            @RequestParam LocalDateTime endDate,
            @RequestParam Integer page,
            @RequestParam Integer size) throws CustomerNotFoundException{
        String token = extractToken(authorizationHeader);
        if(token == null) {
            throw new CustomerNotFoundException("UnAuthorized");
        }

        JwtUtils jwtUtils = new JwtUtils();
        String customerEmail = jwtUtils.getUserNameFromJwtToken(token);
        Customer customer = this.customerRepository.findUserByEmail(customerEmail)
                .orElseThrow(() -> new CustomerNotFoundException(String.format("Customer with email %s not found", customerEmail)));
        List<TransactionDTO> transactions = transactionService.getTransactionHistoryWithFilters(customer.getId(), startDate,endDate, page, size);
        return ResponseEntity.ok(transactions);
    }

}