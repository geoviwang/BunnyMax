package com.bunnyMax.productTrading.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestController
@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class ErrorHandlerController implements ErrorController {

    @ExceptionHandler({
            Throwable.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleThrowable(HttpServletRequest req, HttpServletResponse rep, Exception e) {
        log.error(String.valueOf(e));
        return ResponseEntity.ofNullable("Oh! There are some problems.");
    }

}
