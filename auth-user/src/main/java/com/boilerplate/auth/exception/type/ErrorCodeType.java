package com.boilerplate.auth.exception.type;

import org.springframework.http.HttpStatus;

public interface ErrorCodeType {
  String getValue();

  String getDescription();

  HttpStatus getHttpStatus();
}
