package com.bebit.apigateway.security.config;

import com.bebit.apigateway.security.models.AppUser;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class CustomFilter implements GlobalFilter, Ordered {

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    final Mono<?> authToken = exchange.getPrincipal();
    return authToken
        .cast(UsernamePasswordAuthenticationToken.class)
        .map(token -> token.getPrincipal())
        .cast(AppUser.class)
        .map(AppUser::getClient)
        .map(client -> exchange.getRequest().mutate().header("clientId", "" + client.getId()).build())
        .flatMap(x -> chain.filter(exchange));
  }

  @Override
  public int getOrder() {
    return Ordered.LOWEST_PRECEDENCE; // Set order if needed
  }
}
