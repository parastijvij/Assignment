package com.example.assignment.controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.assignment.service.ApiService;

@RestController
@RequestMapping("/api")
public class ApiController {

    private final ApiService apiService;

    public ApiController(ApiService apiService) {
        this.apiService = apiService;
    }

    @GetMapping("/example")
    public String exampleAPI() {
        String bearerToken = apiService.authenticateAndGetBearerToken();
        
        if (bearerToken != null) {
            // Use bearerToken for subsequent API calls
            // For example, make another API call using the bearer token
            // String result = apiService.makeAnotherAPICall(bearerToken);
            return "Authenticated successfully. Bearer token: " + bearerToken;
        } else {
            return "Authentication failed";
        }
    }
}

