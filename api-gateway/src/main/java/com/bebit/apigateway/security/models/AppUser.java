package com.bebit.apigateway.security.models;

import com.bebit.apigateway.security.models.permissions.ContractedService;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "app_user")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppUser implements UserDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String firstname;
  private String lastname;
  private String username;
  private String email;
  private String password;

  // no cascade as we shouldn't save client while saving user
  @ManyToOne
  @JoinColumn(name = "client_id")
  private Client client;

  // no cascade as we shouldn't save role while saving user
  @ManyToOne
  @JoinColumn(name = "role_id")
  private AppUserRole appRole;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    List<SimpleGrantedAuthority> permissions = appRole.getAppUserPermissions()
        .stream()
        .map(appUserPermission -> new SimpleGrantedAuthority(appUserPermission.getCodename().name()))
        .collect(Collectors.toList());
    List<SimpleGrantedAuthority> roles = List.of(new SimpleGrantedAuthority("ROLE_" + appRole.getName().name()));
    permissions.addAll(roles);
//    Set<SimpleGrantedAuthority> contractedPermissions = client.getParseContractedServices().stream()
//            .map(contractedService -> new SimpleGrantedAuthority(contractedService.name()))
//                .collect(Collectors.toSet());
//
//    permissions.addAll(contractedPermissions);
    return permissions;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return username;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  public boolean hasContract(ContractedService contractedService) {
    return ((client.getContractedServices() & contractedService.getCode()) == contractedService.getCode());
  }
}
