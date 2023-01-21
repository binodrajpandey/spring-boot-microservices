package com.bebit.apigateway.security.config;

import com.bebit.apigateway.security.services.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthManager implements ReactiveAuthenticationManager {

  private final JwtService jwtService;
  private final ReactiveUserDetailsService userDetailsService;


  @Override
  public Mono<Authentication> authenticate(Authentication authentication) {
    return Mono.justOrEmpty(
            authentication
        )
        .cast(BearerToken.class)
        .flatMap(bearerToken -> {
          String username = jwtService.getUsernameFromToken(bearerToken.getCredentials());
          Mono<UserDetails> foundUser = userDetailsService.findByUsername(username);

          // Check if user was found.
          return foundUser.<Authentication>flatMap(u -> {
            //Check if user is valid
            if (u.getUsername() == null) {
              log.info("User not found");
              return Mono.error(new Exception("User not found"));
            }
            //validate token
            if (jwtService.validateToken(u,bearerToken.getCredentials())) {
              return Mono.just(new UsernamePasswordAuthenticationToken(u.getUsername(), u.getPassword(), u.getAuthorities()));
            }
            log.info("Invalid / Expired Token : {}", bearerToken.getCredentials());
            return Mono.error(new Exception("Invalid/Expired Token"));
          });
        });
  }

}
