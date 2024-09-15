package com.bebit.apigateway.security.models.permissions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ContractedService {
  WEB(1),
  FUNNEL(2),
  TD(3);

  private int code;

}
