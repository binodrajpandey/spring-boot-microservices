package com.bebit.apigateway.security.config;

import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Converts header value into authentication token (bearer token).
 */
@Component
public class AuthConverter implements ServerAuthenticationConverter {

  /**
   * Retrieves the token from request headers and converts to Bearer Token.
   */
  @Override
  public Mono<Authentication> convert(ServerWebExchange exchange) {

    return Mono.justOrEmpty(
            exchange
                .getRequest()
                .getHeaders()
                .getFirst(HttpHeaders.AUTHORIZATION)
        )
        .filter(s -> s.startsWith("Bearer "))
        .map(s -> s.substring(7))
        .map(s -> new BearerToken(s));
  }
}
