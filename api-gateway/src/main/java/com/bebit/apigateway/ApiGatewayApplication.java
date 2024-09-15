package com.bebit.apigateway;

import com.bebit.apigateway.repositories.AppUserPermissionRepository;
import com.bebit.apigateway.repositories.AppUserRepository;
import com.bebit.apigateway.repositories.AppUserRoleRepository;
import com.bebit.apigateway.repositories.ClientRepository;
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
      ClientRepository clientRepository,
      AppUserRoleRepository appUserRoleRepository,
      PasswordEncoder passwordEncoder
  ) {
    return (args) -> {
      appUserRoleRepository.deleteAll();
      clientRepository.deleteAll();
      appUserPermissionRepository.deleteAll();
      appUserRoleRepository.deleteAll();

      Client client1 = Client.builder().contractedServices(1).build();
      clientRepository.save(client1);
      Client client2 = Client.builder().contractedServices(3).build();
      clientRepository.save(client2);

      AppUserPermission productEditPermission = AppUserPermission.builder()
          .codename(Permission.EDIT_MEASUREMENT_SETTING)
          .build();

      AppUserPermission productRetrievePermission = AppUserPermission.builder()
          .codename(Permission.VIEW_MEASUREMENT_SETTING)
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

      appUserRoleRepository.save(adminRole);
      appUserRoleRepository.save(userRole);

      // To test ADMIN
      AppUser appUser1 = AppUser.builder()
          .client(client1)
          .firstname("Binod")
          .lastname("Pandey")
          .username("binod")
          .email("email")
          .password(passwordEncoder.encode("binod"))
          .appRole(adminRole)
          .build();

      // To test NORMAL USER
      AppUser appUser2 = AppUser.builder()
          .client(client1)
          .firstname("Sandhya")
          .lastname("Silwal")
          .username("sandhya")
          .email("email2")
          .password(passwordEncoder.encode("sandhya"))
          .appRole(userRole)
          .build();

      // To test contracted feature
      AppUser appUser3 = AppUser.builder()
          .client(client2)
          .firstname("Lukas")
          .lastname("Schmid")
          .username("lukas")
          .email("email3")
          .password(passwordEncoder.encode("lukas"))
          .appRole(userRole)
          .build();

      appUserRepository.save(appUser1);
      appUserRepository.save(appUser2);
      appUserRepository.save(appUser3);

    };
  }


}
