package org.transferservice.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.transferservice.dto.CreateCustomerDTO;
import org.transferservice.dto.LoginRequestDTO;
import org.transferservice.dto.LoginResponseDTO;
import org.transferservice.dto.LogoutResponseDTO;
import org.transferservice.exception.custom.CustomerAlreadyExistException;
import org.transferservice.exception.response.ErrorDetails;
import org.transferservice.service.security.IAuthenticator;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Validated
@Tag(name = "Customer Auth Controller", description = "Customer Auth controller")
public class AuthController {

    private final IAuthenticator authenticatorService;

    @Operation(summary = "Register new Customer")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = String.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema(implementation = ErrorDetails.class), mediaType = "application/json")})
    @PostMapping("/register")
    public String register(@RequestBody @Valid CreateCustomerDTO createCustomerDTO) throws CustomerAlreadyExistException {
        return this.authenticatorService.register(createCustomerDTO);
    }

    @Operation(summary = "Login and generate JWT")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = LoginResponseDTO.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "401", content = {@Content(schema = @Schema(implementation = ErrorDetails.class), mediaType = "application/json")})
    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody @Valid LoginRequestDTO loginRequestDTO) {
        return this.authenticatorService.login(loginRequestDTO);
    }



    @Operation(summary = "Logout and invalidate JWT")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation  =LogoutResponseDTO.class) ,mediaType = "application/json")})
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "500", description = "Logout failed")
    @PostMapping("/logout")
    public LogoutResponseDTO logout(HttpServletRequest request) {
        return this.authenticatorService.logout(request);
    }



}