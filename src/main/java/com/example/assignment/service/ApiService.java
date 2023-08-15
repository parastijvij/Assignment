package com.example.assignment.service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.assignment.model.AuthRequest;
import com.example.assignment.model.AuthResponse;

@Service
public class ApiService {

    @Value("${auth.api.url}")
    private String authApiUrl;

    @Value("${auth.credentials.login_id}")
    private String loginId;

    @Value("${auth.credentials.password}")
    private String password;

    private final RestTemplate restTemplate;

    public ApiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String authenticateAndGetBearerToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        AuthRequest authRequest = new AuthRequest();
        authRequest.setLogin_id(loginId);
        authRequest.setPassword(password);

        HttpEntity<AuthRequest> requestEntity = new HttpEntity<>(authRequest, headers);

        ResponseEntity<AuthResponse> responseEntity = restTemplate.exchange(
                authApiUrl,
                HttpMethod.POST,
                requestEntity,
                AuthResponse.class
        );

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            AuthResponse authResponse = responseEntity.getBody();
            if (authResponse != null) {
                return authResponse.getToken();
            }
        }

        return null;
    }
}
