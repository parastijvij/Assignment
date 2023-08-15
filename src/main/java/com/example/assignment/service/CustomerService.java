package com.example.assignment.service;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.example.assignment.model.Customer;

@Service
public class CustomerService {

    @Value("${customer.api.url}")
    private String customerApiUrl;

    private final RestTemplate restTemplate;

    public CustomerService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ResponseEntity<String> createCustomer(Customer customer, String bearerToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(bearerToken);

        HttpEntity<Customer> requestEntity = new HttpEntity<>(customer, headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                customerApiUrl,
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        return responseEntity;
    }
    
    public List<Customer> getCustomerList(String bearerToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(bearerToken);

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<Customer[]> responseEntity = restTemplate.exchange(
                customerApiUrl + "?cmd=get_customer_list",
                HttpMethod.GET,
                requestEntity,
                Customer[].class
        );

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            return Arrays.asList(responseEntity.getBody());
        } else {
            // Handle the response as needed (e.g., return an empty list or throw an exception)
            return Collections.emptyList();
        }
    }
    public ResponseEntity<String> deleteCustomer(String uuid, String bearerToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(bearerToken);

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    customerApiUrl + "?cmd=delete&uuid=" + uuid,
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );

            return responseEntity;
        } catch (HttpClientErrorException.NotFound ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("UUID not found");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + ex.getMessage());
        }
    }
    public ResponseEntity<String> updateCustomer(String uuid, Customer customer, String bearerToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(bearerToken);

        HttpEntity<Customer> requestEntity = new HttpEntity<>(customer, headers);

        try {
            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    customerApiUrl + "?cmd=update&uuid=" + uuid,
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );

            return responseEntity;
        } catch (HttpClientErrorException.NotFound ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("UUID not found");
        } catch (HttpClientErrorException.BadRequest ex) {
            return ResponseEntity.badRequest().body("Bad Request: " + ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + ex.getMessage());
        }
    }
}

