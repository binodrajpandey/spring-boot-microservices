package com.bebit.apigateway.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
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
  public MapReactiveUserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
    UserDetails userDetails = User.builder()
        .username("binod")
        .password(passwordEncoder.encode("binod"))
        .roles("USER")
        .build();
    return new MapReactiveUserDetailsService(userDetails);
  }


  @Bean
  public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http, AuthConverter jwtAuthConverter, AuthManager jwtAuthManager) {
    AuthenticationWebFilter jwtFilter = new AuthenticationWebFilter(jwtAuthManager);
    jwtFilter.setServerAuthenticationConverter(jwtAuthConverter);
    return http
        .authorizeExchange(auth -> {
          auth.pathMatchers("/api/auth/login").permitAll();
          auth.anyExchange().authenticated();
        })
        .addFilterAt(jwtFilter, SecurityWebFiltersOrder.AUTHENTICATION)
        .httpBasic().disable()
        .formLogin().disable()
        .csrf().disable()
        .build();

  }

}
