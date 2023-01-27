package com.bebit.apigateway.security.config;

import com.bebit.apigateway.security.models.Role;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;

/**
 * https://docs.spring.io/spring-security/reference/reactive/configuration/webflux.html
 */
@EnableWebFluxSecurity
@Configuration
public class SecurityConfig {

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http, AuthConverter jwtAuthConverter,
      AuthManager jwtAuthManager) {
    AuthenticationWebFilter jwtFilter = new AuthenticationWebFilter(jwtAuthManager);
    jwtFilter.setServerAuthenticationConverter(jwtAuthConverter);
    return http
        .authorizeExchange(auth -> {
          auth.pathMatchers("/api/auth/login").permitAll();
          auth.pathMatchers(HttpMethod.GET, "/api/product").hasAnyAuthority(Role.GET_PRODUCT.name());
          auth.pathMatchers(HttpMethod.POST, "/api/product").hasAnyAuthority(Role.EDIT_PRODUCT.name());
          auth.anyExchange().authenticated();
        })
        .addFilterAt(jwtFilter, SecurityWebFiltersOrder.AUTHENTICATION)
        .httpBasic().disable()
        .formLogin().disable()
        .csrf().disable()
        .build();

  }

}
