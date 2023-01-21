package com.bebit.apigateway.security.controllers;

import com.bebit.apigateway.security.models.LoginRequest;
import com.bebit.apigateway.security.services.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

  private final JwtService jwtService;
  private final ReactiveUserDetailsService reactiveUserDetailsService;
  private final PasswordEncoder passwordEncoder;


  @PostMapping("/login")
  public Mono<ResponseEntity<String>> login(@RequestBody LoginRequest loginRequest) {
    final Mono<UserDetails> userMono =
        reactiveUserDetailsService.findByUsername(loginRequest.getUsername());
    return userMono.map(
        user -> {
          if (passwordEncoder.matches(
              loginRequest.getPassword(), user.getPassword())) {
            return
                ResponseEntity.ok(jwtService.generateToken(user.getUsername()));
          }
          return ResponseEntity.badRequest().body("Invalid Credential");

        });
  }

}
