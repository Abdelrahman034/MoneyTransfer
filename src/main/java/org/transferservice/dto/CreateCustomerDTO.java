package org.transferservice.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.transferservice.dto.enums.Gender;

import java.time.LocalDate;


@Data
public class CreateCustomerDTO {

//    private final Long id;

//    @NotNull
//    private final String firstName;
//    @NotNull
//    private final String lastName;

    @NotNull(message = "user name is required")
    @Size(min = 2, max = 50, message = " Name must be between 2 and 50 characters")
    @Pattern(regexp = "^[a-zA-Z]+$", message = " Name must contain only letters and no special characters")
    private final String userName;

    @NotNull
    @Email
    @NotNull(message = "Email is required")
    @Email(message = "Email should be valid")
    private final String email;


    @NotNull
    @Size(min = 6,message = "Password must be at least 8 characters long")
    @NotNull(message = "Password is required")

    private final String password;

    @NotNull
    private final String country;

    @NotNull
    private final LocalDate dateOfBirth;

//    @NotNull
//    private final String phoneNumber;
//
//    @NotNull
//    private final String address;
//
//    @NotNull
//    private final String nationality;
//
//    @NotNull
//    @Size(max = 14)
//    private final String nationalIdNumber;
//
//    @NotNull
//    private final Gender gender;
//
//    @NotNull
//    private final LocalDate dateOfBirth;
//
//    @NotNull
//    @Size(min = 6)
//    private final String password;

}
