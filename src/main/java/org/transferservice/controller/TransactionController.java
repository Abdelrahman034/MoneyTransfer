package org.transferservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.transferservice.dto.TransactionDTO;
import org.transferservice.service.TransactionService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping
    public ResponseEntity<List<TransactionDTO>> getTransactionHistory(
            @RequestParam Integer page,
            @RequestParam Integer size) {
        List<TransactionDTO> transactions = transactionService.getTransactionHistory(page, size);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/filtered")
    public ResponseEntity<List<TransactionDTO>> getTransactionHistoryWithFilters(
            @RequestParam LocalDateTime startDate,
            @RequestParam LocalDateTime endDate,
            @RequestParam Integer page,
            @RequestParam Integer size) {
        List<TransactionDTO> transactions = transactionService.getTransactionHistoryWithFilters(startDate, endDate, page, size);
        return ResponseEntity.ok(transactions);
    }

}