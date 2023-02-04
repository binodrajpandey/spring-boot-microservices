package com.bebit.apigateway;

import com.bebit.apigateway.models.HttpErrorInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalControllerExceptionHandler {

  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(UsernameNotFoundException.class)
  public HttpErrorInfo handleInvalidInputException(UsernameNotFoundException ex) {
    log.info("User Not found...");
    return new HttpErrorInfo(ex.getMessage());
  }

}
