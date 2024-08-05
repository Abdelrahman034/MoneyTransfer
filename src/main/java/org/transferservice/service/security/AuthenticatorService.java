package org.transferservice.service.security;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.transferservice.dto.CreateCustomerDTO;
import org.transferservice.dto.LoginRequestDTO;
import org.transferservice.dto.LoginResponseDTO;
import org.transferservice.dto.LogoutResponseDTO;
import org.transferservice.dto.enums.AccountCurrency;
import org.transferservice.dto.enums.AccountType;
import org.transferservice.exception.custom.CustomerAlreadyExistException;
import org.transferservice.exception.custom.InvalidJwtException;
import org.transferservice.model.Account;
import org.transferservice.model.Customer;
import org.transferservice.repository.AccountRepository;
import org.transferservice.repository.CustomerRepository;
import java.security.SecureRandom;

import static org.hibernate.query.sqm.tree.SqmNode.log;

@Service
@RequiredArgsConstructor
public class AuthenticatorService implements IAuthenticator {

    private final CustomerRepository customerRepository;
    private final AccountRepository accountRepository;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @Override
    @Transactional
    public String register(CreateCustomerDTO createCustomerDTO) throws CustomerAlreadyExistException {

        if (this.customerRepository.existsByEmail(createCustomerDTO.getEmail())) {
            throw new CustomerAlreadyExistException(String.format("Customer with email %s already exists", createCustomerDTO.getEmail()));
        }

        Account account = Account.builder()
                .accountNumber(String.valueOf(new SecureRandom().nextInt(1000000000)))
                .accountType(AccountType.SAVINGS)
                .accountName(createCustomerDTO.getUserName())
                .accountDescription("Savings Account")
                .active(true)
                .currency(AccountCurrency.EGP)
                .balance(1000.0)
                .build();

        Account newAccount = this.accountRepository.save(account);

        Customer customer;

        customer = Customer
                .builder()
                .email(createCustomerDTO.getEmail())
                .userName(createCustomerDTO.getUserName())
                .country(createCustomerDTO.getCountry())
                .dateOfBirth(createCustomerDTO.getDateOfBirth())  // Ensure this is in correct format
                .password(this.encoder.encode(createCustomerDTO.getPassword()))
                .account(newAccount)
                .build();




        this.customerRepository.save(customer).toDTO();
        return customer.getAccount().getAccountNumber();
    }

    @Override
    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDTO.getEmail(), loginRequestDTO.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtUtils.generateJwtToken(authentication);

        return LoginResponseDTO.builder()
                .token(jwt)
                .message("Login Successful")
                .status(HttpStatus.ACCEPTED)
                .tokenType("Bearer")
                .build();
    }




    public LogoutResponseDTO logout(HttpServletRequest request) {
        String token = parseJwt(request);
        if (token != null) {
            try {
//                String userName = jwtUtils.getUserEmailFromJwtToken(token); // Assuming userName is extracted from token
//                jwtUtils.invalidateJwtToken(token, userName);
                jwtUtils.invalidateJwtToken(token);
                return LogoutResponseDTO.builder()
                        .message("Logout successful")
                        .status(HttpStatus.OK)
                        .build();
            } catch (InvalidJwtException e) {
                log.error("Invalid JWT token during logout: {}");
                return LogoutResponseDTO.builder()
                        .message("Invalid token")
                        .status(HttpStatus.UNAUTHORIZED)
                        .build();
            } catch (Exception e) {
                log.error("Logout failed: {}", e.getMessage(), e);
                return LogoutResponseDTO.builder()
                        .message("Logout failed")
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .build();
            }
        } else {
            return LogoutResponseDTO.builder()
                    .message("Unauthorized")
                    .status(HttpStatus.UNAUTHORIZED)
                    .build();
        }
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }
        return null;
    }


}
