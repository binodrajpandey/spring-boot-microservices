package com.bebit.apigateway.security.services;

import com.bebit.apigateway.repositories.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AppUserDetailsService implements ReactiveUserDetailsService {

  private final AppUserRepository appUserRepository;

  @Override
  public Mono<UserDetails> findByUsername(String username) {
    return Mono.just(appUserRepository.findByUsername(username).orElse(null));
  }
}
