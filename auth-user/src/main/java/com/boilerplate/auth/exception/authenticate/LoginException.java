package com.boilerplate.auth.exception.authenticate;

import com.boilerplate.auth.exception.type.ErrorCodeType;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum LoginException implements ErrorCodeType {

  /**
   * Error login exception.
   */
  AUTHENTICATE_LOGIN_Failed("AUTHENTICATE_LOGIN_Failed", "Login failed",
      HttpStatus.FORBIDDEN),

  ;

  final String value;
  final String description;
  final HttpStatus httpStatus;

  @Override
  public String getValue() {
    return value;
  }

  @Override
  public String getDescription() {
    return description;
  }

  @Override
  public HttpStatus getHttpStatus() {
    return httpStatus;
  }
}
