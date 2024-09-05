package com.bebit.apigateway;

import com.bebit.apigateway.repositories.AppUserPermissionRepository;
import com.bebit.apigateway.repositories.AppUserRepository;
import com.bebit.apigateway.security.models.AppUser;
import com.bebit.apigateway.security.models.AppUserPermission;
import com.bebit.apigateway.security.models.AppUserRole;
import com.bebit.apigateway.security.models.Client;
import com.bebit.apigateway.security.models.permissions.Permission;
import com.bebit.apigateway.security.models.permissions.Role;
import java.util.Set;
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
  public CommandLineRunner commandLineRunner(
      AppUserRepository appUserRepository,
      AppUserPermissionRepository appUserPermissionRepository,
      PasswordEncoder passwordEncoder
  ) {
    return (args) -> {
      AppUserPermission productEditPermission = AppUserPermission.builder()
          .codename(Permission.EDIT_PRODUCT)
          .build();

      AppUserPermission productRetrievePermission = AppUserPermission.builder()
          .codename(Permission.GET_PRODUCT)
          .build();

      appUserPermissionRepository.save(productEditPermission);
      appUserPermissionRepository.save(productRetrievePermission);

      AppUserRole adminRole = AppUserRole.builder()
          .name(Role.ADMIN_USER)
          .appUserPermissions(Set.of(productEditPermission, productRetrievePermission))
          .build();

      AppUserRole userRole = AppUserRole.builder()
          .name(Role.NORMAL_USER)
          .appUserPermissions(Set.of(productRetrievePermission))
          .build();

      Client client1 = Client.builder().build();
      Client client2 = Client.builder().build();

      AppUser appUser1 = AppUser.builder()
          .client(client1)
          .firstname("Binod")
          .lastname("Pandey")
          .username("binod")
          .password(passwordEncoder.encode("binod"))
          .appRole(adminRole)
          .build();

      AppUser appUser2 = AppUser.builder()
          .client(client2)
          .firstname("Sandhya")
          .lastname("Silwal")
          .username("sandhya")
          .password(passwordEncoder.encode("sandhya"))
          .appRole(userRole)
          .build();

      appUserRepository.save(appUser1);
      appUserRepository.save(appUser2);

    };
  }

}
