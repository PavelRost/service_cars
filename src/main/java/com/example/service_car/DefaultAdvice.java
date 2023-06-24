package com.example.service_car;

import com.example.service_car.controller.ApplicationMessage;
import org.postgresql.util.PSQLException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class DefaultAdvice {

    @ExceptionHandler(PSQLException.class)
    public ResponseEntity<ApplicationMessage> handleException(PSQLException e) {
        return new ResponseEntity<>(new ApplicationMessage(HttpStatus.BAD_REQUEST.value(),
                "Проверьте правильность заполнения JSON"),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApplicationMessage> handleException(IllegalArgumentException e) {
        return new ResponseEntity<>(new ApplicationMessage(HttpStatus.BAD_REQUEST.value(),
                "Указаны недопустимые параметры запроса"),
                HttpStatus.BAD_REQUEST);
    }
}
