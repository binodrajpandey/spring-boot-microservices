package com.bebit.apigateway.repositories;

import com.bebit.apigateway.security.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Integer> {

}
