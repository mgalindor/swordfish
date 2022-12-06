package com.mk.swordfish.core.exceptions;

import lombok.Getter;

@Getter
public class BusinessErrorException extends RuntimeException {

  public static final String BUSINESS_ERROR_MESSAGE = "request.has.error";
  String code;
  String[] args;

  public BusinessErrorException() {
    super(BUSINESS_ERROR_MESSAGE);
    this.code = BUSINESS_ERROR_MESSAGE;
  }

  public BusinessErrorException(Throwable cause) {
    super(BUSINESS_ERROR_MESSAGE, cause);
    this.code = BUSINESS_ERROR_MESSAGE;
  }

  public BusinessErrorException(String code, String... args) {
    super(code);
    this.code = code;
    this.args = args;
  }

  public BusinessErrorException(Throwable cause, String code, String... args) {
    super(code, cause);
    this.code = code;
    this.args = args;
  }

}
