package com.bebit.apigateway.security.models;

import com.bebit.apigateway.security.models.permissions.ContractedService;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "client")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Client {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;

  int contractedServices;

  public Set<ContractedService> getParseContractedServices() {
    // TODO: parse
    if (id == 1) {
      return Set.of(ContractedService.FUNNEL);
    }
    return Set.of();
  }

}
