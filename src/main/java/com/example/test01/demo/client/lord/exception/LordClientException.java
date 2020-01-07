package com.example.test01.demo.client.lord.exception;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;

@Data
@RequiredArgsConstructor
public class LordClientException extends RuntimeException {
    private final ResponseEntity response;
}
