package org.transferservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.transferservice.dto.TransactionDTO;
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
    private Long senderAccountId;

    @Column(nullable = false)
    private Long recipientAccountId;

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
                .senderAccountId(this.getSenderAccountId())
                .recipientAccountId(this.getRecipientAccountId())
                .amount(this.getAmount())
                .transactionTime(this.getTransactionTime())
                .status(this.getStatus())
                .build();
    }

}
