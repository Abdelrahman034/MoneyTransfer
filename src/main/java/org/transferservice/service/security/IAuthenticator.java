package org.transferservice.service.security;

import jakarta.servlet.http.HttpServletRequest;
import org.transferservice.dto.*;
import org.transferservice.exception.custom.CustomerAlreadyExistException;

public interface IAuthenticator {

    /**
     * Register a new customer
     *
     * @param createCustomerDTO customer details
     * @return registered customer @{@link CustomerDTO}
     * @throws CustomerAlreadyExistException if customer already exist
     */
    String register(CreateCustomerDTO createCustomerDTO) throws CustomerAlreadyExistException;

    /**
     * Login a customer
     *
     * @param loginRequestDTO login details
     * @return login response @{@link LoginResponseDTO}
     */
    LoginResponseDTO login(LoginRequestDTO loginRequestDTO);
    LogoutResponseDTO logout(HttpServletRequest httpServletRequest);
}
