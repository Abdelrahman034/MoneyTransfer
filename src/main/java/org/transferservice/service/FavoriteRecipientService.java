package org.transferservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.transferservice.model.Account;
import org.transferservice.model.FavoriteRecipients;
import org.transferservice.repository.AccountRepository;
import org.transferservice.repository.FavoriteRecipientsRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoriteRecipientService {

    private final FavoriteRecipientsRepository favoriteRecipientsRepository;

    private final AccountRepository accountRepository;

    public List<String> getRecipientAccountNumbersBySenderAccountNumber(String senderAccountNumber) {
        return favoriteRecipientsRepository.findRecipientAccountNumbersBySenderAccount_AccountNumber(senderAccountNumber);
    }

    public List<String> addRecipientAccountNumber(String senderAccountNumber, String recipientAccountNumber) {
        Account senderAccount = accountRepository.findByAccountNumber(senderAccountNumber);
        Account recipientAccount = accountRepository.findByAccountNumber(recipientAccountNumber);

        FavoriteRecipients favorite = FavoriteRecipients.builder()
                .senderAccount(senderAccount)
                .recipientAccount(recipientAccount)
                .build();

        boolean exists = favoriteRecipientsRepository.existsBySenderAccount_AccountNumberAndRecipientAccount_AccountNumber(senderAccountNumber, recipientAccountNumber);

        if (exists) {
            throw new IllegalArgumentException("Favorite recipient already exists");
        }
        favoriteRecipientsRepository.save(favorite);
        return getRecipientAccountNumbersBySenderAccountNumber(senderAccountNumber);
    }
}
