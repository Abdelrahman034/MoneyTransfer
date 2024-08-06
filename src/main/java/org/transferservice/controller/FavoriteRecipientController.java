package org.transferservice.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.transferservice.dto.CustomerDTO;
import org.transferservice.dto.FavoriteRecipientRequestDTO;
import org.transferservice.exception.custom.CustomerNotFoundException;
import org.transferservice.service.CustomerService;
import org.transferservice.service.FavoriteRecipientService;
import org.transferservice.service.security.JwtUtils;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/favorites")
public class FavoriteRecipientController {

    private final CustomerService customerService;
    private final JwtUtils jwtUtils;
    private final FavoriteRecipientService favoriteRecipientService;
    @GetMapping
    public List<String> getFavorites(@RequestHeader("Authorization") String authorizationHeader) throws CustomerNotFoundException {
        authorizationHeader = authorizationHeader.substring(7);
        String customerEmail = jwtUtils.getUserNameFromJwtToken(authorizationHeader);
        CustomerDTO customer = this.customerService.checkCustomerEmail(customerEmail);
        String senderAccountNumber = customer.getAccount().getAccountNumber();
        return favoriteRecipientService.getRecipientAccountNumbersBySenderAccountNumber(senderAccountNumber);
    }

    @PostMapping
    public List<String> addToFavorites(@RequestHeader("Authorization") String authorizationHeader,
                                       @RequestBody FavoriteRecipientRequestDTO favoriteRecipientRequest) throws CustomerNotFoundException {
        authorizationHeader = authorizationHeader.substring(7);
        String recipientAccountNumber = favoriteRecipientRequest.getRecipientAccountNumber();
        String customerEmail = jwtUtils.getUserNameFromJwtToken(authorizationHeader);
        CustomerDTO customer = this.customerService.checkCustomerEmail(customerEmail);
        String senderAccountNumber = customer.getAccount().getAccountNumber();
        return favoriteRecipientService.addRecipientAccountNumber(senderAccountNumber,recipientAccountNumber);
    }


}
