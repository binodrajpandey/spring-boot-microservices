package com.bebit.apigateway.repositories;

import com.bebit.apigateway.security.models.AppUserPermission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserPermissionRepository extends JpaRepository<AppUserPermission, Integer> {

}
