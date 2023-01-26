package com.bebit.apigateway;

import com.bebit.apigateway.repositories.AppUserRepository;
import com.bebit.apigateway.security.models.AppUser;
import com.bebit.apigateway.security.models.Role;
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
          .role(Role.NORMAL)
          .build();
      appUserRepository.save(appUser1);

    };
  }

}
