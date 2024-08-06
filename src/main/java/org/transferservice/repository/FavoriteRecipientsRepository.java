package org.transferservice.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.transferservice.model.FavoriteRecipients;

import java.util.List;

@Repository
public interface FavoriteRecipientsRepository extends JpaRepository<FavoriteRecipients, Long> {

    boolean existsBySenderAccount_AccountNumberAndRecipientAccount_AccountNumber(String senderAccountNumber, String recipientAccountNumber);
    @Query("SELECT f.recipientAccount.accountNumber FROM FavoriteRecipients f WHERE f.senderAccount.accountNumber = :senderAccountNumber")
    List<String> findRecipientAccountNumbersBySenderAccount_AccountNumber(@Param("senderAccountNumber") String senderAccountNumber);
}