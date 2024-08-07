package org.transferservice.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.transferservice.dto.CustomerDTO;
import org.transferservice.dto.UpdateCustomerDTO;
import org.transferservice.exception.custom.CustomerNotFoundException;
import org.transferservice.exception.response.ErrorDetails;
import org.transferservice.model.Customer;
import org.transferservice.service.ICustomer;

@RestController
@RequestMapping("/api/customer")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
@Validated
@Tag(name = "Customer Controller", description = "Customer controller")
public class CustomerController {

    private final ICustomer customerService;

    @Operation(summary = "Get Customer by ID")
    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = CustomerDTO.class), mediaType = "application/json")})
    @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema(implementation = ErrorDetails.class), mediaType = "application/json")})
    @GetMapping("/{id}")
    public CustomerDTO getCustomerById(@PathVariable Long id) throws CustomerNotFoundException {
        return customerService.getCustomerById(id);
    }

//    @Operation(summary = "Delete Customer by ID")
//    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = CustomerDTO.class), mediaType = "application/json")})
//    @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema(implementation = ErrorDetails.class), mediaType = "application/json")})
//    @DeleteMapping("/{id}")
//    public void deleteCustomer(@PathVariable Long id) throws CustomerNotFoundException {
//        customerService.deleteCustomer(id);
//    }
//
//    @Operation(summary = "Update Customer by ID")
//    @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = CustomerDTO.class), mediaType = "application/json")})
//    @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema(implementation = ErrorDetails.class), mediaType = "application/json")})
//    @PutMapping("/{id}")
//    public Customer updateCustomer(@PathVariable Long id,
//                                   @RequestBody UpdateCustomerDTO updateCustomerDTO) throws CustomerNotFoundException {
//        return customerService.updateCustomer(id, updateCustomerDTO);
//    }

}
