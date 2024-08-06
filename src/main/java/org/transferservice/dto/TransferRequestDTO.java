package org.transferservice.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.transferservice.dto.enums.AccountCurrency;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransferRequestDTO {
    private String recipientAccountNumber;
    private Double amount;
    private AccountCurrency currency;
}
