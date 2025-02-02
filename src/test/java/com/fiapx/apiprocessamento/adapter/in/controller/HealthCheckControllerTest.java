package com.fiapx.apiprocessamento.adapter.in.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HealthCheckControllerTest {

    @InjectMocks
    private HealthCheckController healthCheckController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testHealthCheck() {
        ResponseEntity<String> response = healthCheckController.healthCheck();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Healthy", response.getBody());
    }
}