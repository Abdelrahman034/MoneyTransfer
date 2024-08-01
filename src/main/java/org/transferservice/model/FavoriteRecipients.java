package org.transferservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Columns;


@Data
@AllArgsConstructor
@Entity
@Builder
@NoArgsConstructor
public class FavoriteRecipients {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sender_account_number", referencedColumnName = "accountNumber")

    private Account senderAccount;

    @ManyToOne
    @JoinColumn(name = "recipient_account_number", referencedColumnName = "accountNumber")
    private Account recipientAccount;
}

