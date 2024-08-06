package org.transferservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.transferservice.dto.TransactionDTO;
import org.transferservice.dto.enums.AccountCurrency;
import org.transferservice.dto.enums.TransactionStatus;

import java.time.LocalDateTime;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String senderName;

    @Column(nullable = false)
    private String recipientName;

    @Column(nullable = false)
    private String senderAccountNumber;

    @Column(nullable = false)
    private String recipientAccountNumber;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AccountCurrency currency;

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    @CreationTimestamp
    private LocalDateTime transactionTime;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    public TransactionDTO toDTO() {
        return TransactionDTO.builder()
                .id(this.getId())
                .senderAccountNumber(this.getSenderAccountNumber())
                .recipientAccountNumber(this.getRecipientAccountNumber())
                .recipientName(this.getRecipientName())
                .senderName(this.getSenderName())
                .amount(this.getAmount())
                .currency(this.getCurrency())
                .transactionTime(this.getTransactionTime())
                .status(this.getStatus())
                .build();
    }

}
