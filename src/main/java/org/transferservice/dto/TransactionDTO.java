package org.transferservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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
    private double amount;
    private LocalDateTime transactionTime;
    private TransactionStatus status;
}