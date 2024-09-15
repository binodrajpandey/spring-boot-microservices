package com.bebit.apigateway.security.models;

import com.bebit.apigateway.security.models.permissions.Permission;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "app_user_permission")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppUserPermission {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Enumerated(EnumType.STRING)
  @Column(name = "codename", unique = true)
  private Permission codename;

  @ManyToMany(mappedBy = "appUserPermissions")
  private Set<AppUserRole> appUserRoles;

}
