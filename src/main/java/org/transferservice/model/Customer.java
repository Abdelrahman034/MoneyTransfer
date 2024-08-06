package org.transferservice.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.transferservice.dto.CustomerDTO;
import org.transferservice.dto.enums.Gender;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@Entity
@Builder
@NoArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String userName;


    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String country;

    @Column(nullable = false)
    private LocalDate dateOfBirth;

    @Column(nullable = false)
    private String password;


    @CreationTimestamp
    private LocalDate creationTimeStamp;

    @OneToOne(fetch = FetchType.EAGER)
    private Account account;

//    @Column(nullable = false, unique = true)
//    private String phoneNumber;
//
//    @Column(nullable = false)
//    private String address;
//
//
//
//    @Column(nullable = false, unique = true)
//    private String nationalIdNumber;
//
//    @Enumerated(EnumType.STRING)
//    private Gender gender;
//




    public CustomerDTO toDTO() {
        return CustomerDTO.builder()
                .id(this.id)
                .userName(this.userName)
                .country(this.country)
                .email(this.email)
                .dateOfBirth(this.dateOfBirth)
                // .phoneNumber(this.phoneNumber)
//                .address(this.address)
//                .gender(this.gender)
//                .nationality(this.nationality)
//                .nationalIdNumber(this.nationalIdNumber)
                .account(this.account.toDTO())
                .build();
    }
}