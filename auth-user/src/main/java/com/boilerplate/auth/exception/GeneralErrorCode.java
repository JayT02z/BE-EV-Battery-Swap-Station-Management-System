package com.boilerplate.auth.exception;

import lombok.AllArgsConstructor;

import com.boilerplate.auth.exception.type.ErrorCodeType;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum GeneralErrorCode implements ErrorCodeType {

  E001("E001", "General exception error.", HttpStatus.INTERNAL_SERVER_ERROR);

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
