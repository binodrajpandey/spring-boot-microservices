package com.bebit.apigateway.security.config;

import com.bebit.apigateway.security.services.AppUserDetailsService;
import com.bebit.apigateway.security.services.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthManager implements ReactiveAuthenticationManager {

  private final JwtService jwtService;
  private final AppUserDetailsService userDetailsService;


  @Override
  public Mono<Authentication> authenticate(Authentication authentication) {
    return Mono.justOrEmpty(authentication)
        .cast(BearerToken.class)
        .flatMap(bearerToken -> {
          try {
            String authToken = bearerToken.getCredentials();
            String username = jwtService.getUsernameFromToken(authToken);

            return userDetailsService.findByUsername(username)
                .flatMap(user -> {
                  if (user == null) {
                    log.info("User not found");
                    return Mono.error(new AuthenticationException("User not found") {
                    });
                  }
                    return Mono.just(
                        new UsernamePasswordAuthenticationToken(user.getUsername(), null, user.getAuthorities()));
                });

          } catch (ExpiredJwtException e) {
            log.error("Token has expired");
            return Mono.error(new AuthenticationException("JWT Token expired") {
            });
          } catch (Exception e) {
            log.error("Invalid token", e);
            return Mono.error(new AuthenticationException("Invalid token" + e.getMessage()) {
            });
          }
        });
  }


}
