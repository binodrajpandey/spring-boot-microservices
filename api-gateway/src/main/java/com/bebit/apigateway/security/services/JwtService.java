package com.bebit.apigateway.security.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.sql.Date;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

  private final SecretKey secretKey;
  private final JwtParser jwtParser;

  public JwtService() {
    this.secretKey = Keys.hmacShaKeyFor("1234567890dafafafafafafafafafaffafafafafafa".getBytes());
    this.jwtParser = Jwts.parserBuilder().setSigningKey(this.secretKey).build();
  }

  public String generateToken(String username){
    JwtBuilder jwtBuilder = Jwts.builder()
        .setSubject(username)
        .setIssuedAt(Date.from(Instant.now()))
        .setExpiration(Date.from(Instant.now().plus(5, ChronoUnit.MINUTES)))
        .signWith(secretKey);
    return jwtBuilder.compact();

  }

  public String getUsernameFromToken(String token) {
    Claims claims = jwtParser.parseClaimsJws(token).getBody();
    return claims.getSubject();
  }

}
