package org.transferservice.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.transferservice.dto.enums.Gender;

import java.time.LocalDate;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerDTO {

    private Long id;

    private String userName;

    private String email;

    private String country;

    private LocalDate dateOfBirth;

    private AccountDTO account;
//    private String phoneNumber;
//
//    private String address;
//
//    private String nationality;
//
//    private String nationalIdNumber;
//
//    private Gender gender;
//


}