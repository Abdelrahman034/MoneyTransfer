package org.transferservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.transferservice.dto.enums.AccountCurrency;
import org.transferservice.dto.enums.TransactionStatus;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionDTO {

    private Long id;
    private String senderAccountNumber;
    private String recipientAccountNumber;
    private String senderName;
    private String recipientName;
    private double amount;
    private LocalDateTime transactionTime;
    private AccountCurrency currency;
    private final String cardType = "Mastercard";
    private TransactionStatus status;
}