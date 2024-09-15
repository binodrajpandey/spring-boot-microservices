package com.bebit.apigateway.security.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FallbackController {

  @RequestMapping("/fallback")
  public ResponseEntity<String> fallback() {
    return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Service is unavailable. Please try again later.");
  }

}
