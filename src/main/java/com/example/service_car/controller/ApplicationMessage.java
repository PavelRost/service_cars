package com.example.service_car.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ApplicationMessage {

    private int statusCode;
    private String message;

    public ApplicationMessage(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }

    public static ResponseEntity<ApplicationMessage> response(String msg, HttpStatus status) {
        return new ResponseEntity<>(new ApplicationMessage(status.value(), msg), status);
    }
}
