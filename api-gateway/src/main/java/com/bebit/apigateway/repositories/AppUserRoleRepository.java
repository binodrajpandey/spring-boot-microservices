package com.bebit.apigateway.repositories;

import com.bebit.apigateway.security.models.AppUserRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserRoleRepository extends JpaRepository<AppUserRole, Integer> {

}
