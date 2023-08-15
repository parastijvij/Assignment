package com.example.assignment.controller;

import java.util.List;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import com.example.assignment.model.Customer;
import com.example.assignment.service.CustomerService;

@RestController
@RequestMapping("/api")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/create-customer")
    public ResponseEntity<String> createCustomer(@RequestBody Customer customer, @RequestHeader("Authorization") String bearerToken) {
        try {
            ResponseEntity<String> response = customerService.createCustomer(customer, bearerToken);
            return response;
        } catch (HttpClientErrorException.BadRequest ex) {
            return ResponseEntity.badRequest().body("Bad Request: " + ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + ex.getMessage());
        }
    }
    @GetMapping("/get-customer-list")
    public ResponseEntity<List<Customer>> getCustomerList(@RequestHeader("Authorization") String bearerToken) {
        List<Customer> customerList = customerService.getCustomerList(bearerToken);
        return ResponseEntity.ok(customerList);
    }
    @PostMapping("/delete-customer")
    public ResponseEntity<String> deleteCustomer(@RequestParam String uuid, @RequestHeader("Authorization") String bearerToken) {
        ResponseEntity<String> response = customerService.deleteCustomer(uuid, bearerToken);
        return response;
    }
    @PostMapping("/update-customer")
    public ResponseEntity<String> updateCustomer(@RequestParam String uuid, @RequestBody Customer customer, @RequestHeader("Authorization") String bearerToken) {
        ResponseEntity<String> response = customerService.updateCustomer(uuid, customer, bearerToken);
        return response;
    }
}

