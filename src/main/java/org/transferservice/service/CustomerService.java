package org.transferservice.service;

import jakarta.servlet.http.PushBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.transferservice.dto.AccountDTO;
import org.transferservice.dto.CustomerDTO;
import org.transferservice.dto.UpdateCustomerDTO;
import org.transferservice.dto.enums.TransactionStatus;
import org.transferservice.exception.custom.CustomerNotFoundException;
import org.transferservice.model.Account;
import org.transferservice.model.Customer;
import org.transferservice.repository.AccountRepository;
import org.transferservice.repository.CustomerRepository;

import java.lang.constant.DynamicCallSiteDesc;
import java.util.AbstractCollection;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerService implements ICustomer {

    private final CustomerRepository customerRepository;
    private final AccountRepository accountRepository;

//    @Override
//    @Transactional(isolation = Isolation.SERIALIZABLE)
//    public Customer updateCustomer(Long id, UpdateCustomerDTO updateCustomerDTO) throws CustomerNotFoundException {
//
//        Customer customer = this.getCustomerByCustomerId(id);
//
//        customer.setFirstName(updateCustomerDTO.getFirstName());
//        customer.setLastName(updateCustomerDTO.getLastName());
//        customer.setEmail(updateCustomerDTO.getEmail());
//        customer.setPhoneNumber(updateCustomerDTO.getPhoneNumber());
//        customer.setAddress(updateCustomerDTO.getAddress());
//        customer.setNationality(updateCustomerDTO.getNationality());
//        customer.setNationalIdNumber(updateCustomerDTO.getNationalIdNumber());
//        customer.setDateOfBirth(updateCustomerDTO.getDateOfBirth());
//
//        return this.customerRepository.save(customer);
//
//    }

//    @Override
//    public void deleteCustomer(Long id) throws CustomerNotFoundException {
//
//        Customer customer = this.getCustomerByCustomerId(id);
//
//        this.customerRepository.delete(customer);
//    }

    @Override
    public CustomerDTO getCustomerById(Long id) throws CustomerNotFoundException {

        Customer customer = this.getCustomerByCustomerId(id);

        return customer.toDTO();
    }

    private Customer getCustomerByCustomerId(Long id) throws CustomerNotFoundException {
        return this.customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(String.format("Customer with Id %s not found", id)));
    }


    public CustomerDTO checkCustomerEmail(String email) throws CustomerNotFoundException {
       Customer customer = customerRepository.findUserByEmail(email)
                .orElseThrow(() -> new CustomerNotFoundException(String.format("Customer with Id %s not found", email)));
       return customer.toDTO();
    }

    @Override
    public CustomerDTO getCustomerByAccountNumber(String accountNumber) throws CustomerNotFoundException {
        Customer customer = customerRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new CustomerNotFoundException("Account number not found"));
        return customer.toDTO();

    }


}
