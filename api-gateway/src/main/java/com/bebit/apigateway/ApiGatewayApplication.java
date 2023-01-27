package com.bebit.apigateway;

import com.bebit.apigateway.repositories.AppUserRepository;
import com.bebit.apigateway.security.models.AppUser;
import com.bebit.apigateway.security.models.Role;
import java.util.HashSet;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@EnableDiscoveryClient
public class ApiGatewayApplication {

  public static void main(String[] args) {
    SpringApplication.run(ApiGatewayApplication.class, args);
  }

  @Bean
  public CommandLineRunner commandLineRunner(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder) {
    return (args) -> {
      AppUser appUser1 = AppUser.builder()
          .firstname("Binod")
          .lastname("Pandey")
          .username("binod")
          .password(passwordEncoder.encode("binod"))
          .roles(new HashSet<>(List.of(Role.GET_PRODUCT, Role.EDIT_PRODUCT)))
          .build();
      AppUser appUser2 = AppUser.builder()
          .firstname("Sandhya")
          .lastname("Silwal")
          .username("sandhya")
          .password(passwordEncoder.encode("sandhya"))
          .roles(new HashSet<>(List.of(Role.GET_PRODUCT)))
          .build();
      appUserRepository.save(appUser1);
      appUserRepository.save(appUser2);

    };
  }

}
