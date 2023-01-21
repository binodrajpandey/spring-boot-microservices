package com.bebit.apigateway.security.config;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;

public class BearerToken extends AbstractAuthenticationToken {

  private final String token;

  public BearerToken(String token) {
    super(AuthorityUtils.NO_AUTHORITIES);
    this.token = token;

  }

  @Override
  public String getCredentials() {
    return this.token;
  }

  @Override
  public String  getPrincipal() {
    return this.token;
  }
}
