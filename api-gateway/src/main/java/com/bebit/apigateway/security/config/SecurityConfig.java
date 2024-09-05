package com.bebit.apigateway.security.config;

import com.bebit.apigateway.security.models.permissions.ContractedService;
import com.bebit.apigateway.security.models.permissions.Permission;
import com.bebit.apigateway.security.models.permissions.Role;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authorization.AuthorizationContext;

/**
 * https://docs.spring.io/spring-security/reference/reactive/configuration/webflux.html
 */
@EnableWebFluxSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }


  @Bean
  public SecurityWebFilterChain securityWebFilterChain(
      ServerHttpSecurity http,
      AuthConverter jwtAuthConverter,
      AuthManager jwtAuthManager
  ) {
    AuthenticationWebFilter jwtFilter = new AuthenticationWebFilter(jwtAuthManager);
    jwtFilter.setServerAuthenticationConverter(jwtAuthConverter);
    return http
        .authorizeExchange(auth -> {
          auth.pathMatchers("/api/auth/login").permitAll();
          auth.pathMatchers(HttpMethod.POST, "/api/conversion").hasAnyRole(Role.SUPER_USER.name(), Role.ADMIN_USER.name());
          auth.pathMatchers(HttpMethod.GET, "/api/funnel")
              .access(getAuthorizationManager("ROLE_" + Role.ADMIN_USER.name(), ContractedService.FUNNEL.name()));
          auth.pathMatchers(HttpMethod.GET, "/api/client-settins/treasire-data")
              .access(getAuthorizationManager(Role.ADMIN_USER.name(), ContractedService.TD.name()));
          auth.pathMatchers(HttpMethod.GET, "/api/product").hasAnyAuthority(Permission.GET_PRODUCT.name());
          auth.pathMatchers(HttpMethod.POST, "/api/product").hasAnyAuthority(Permission.EDIT_PRODUCT.name());
          auth.anyExchange().authenticated();
        })
        .addFilterAt(jwtFilter, SecurityWebFiltersOrder.AUTHENTICATION)
        .httpBasic().disable()
        .formLogin().disable()
        .csrf().disable()
        .build();

  }

  private static ReactiveAuthorizationManager<AuthorizationContext> getAuthorizationManager(
      String... requiredAuthorities) {
    return (authenticationMono, ctx) -> authenticationMono
        .map(authentication -> {
          // Extract authorities from the authentication object
          Set<String> authorities = authentication.getAuthorities().stream()
              .map(GrantedAuthority::getAuthority)
              .collect(Collectors.toSet());
          System.out.println(authorities);
          Arrays.stream(requiredAuthorities).forEach(System.out::println);
//          System.out.println(requiredAuthorities);
          boolean authorized = Stream.of(requiredAuthorities).allMatch(authorities::contains);

          return new AuthorizationDecision(authorized);
        });
  }

}
