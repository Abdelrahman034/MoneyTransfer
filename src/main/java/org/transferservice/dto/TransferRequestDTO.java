package org.transferservice.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TransferRequestDTO {
    private Long recipientId;
    private Double amount;
}
