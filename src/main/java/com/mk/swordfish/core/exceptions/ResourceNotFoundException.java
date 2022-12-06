package com.mk.swordfish.core.exceptions;

import lombok.Getter;

@Getter
public class ResourceNotFoundException extends RuntimeException {

  public static final String RESOURCE_NOT_FOUND_MESSAGE = "resource.not.found.exception";
  String code;
  String[] args;

  public ResourceNotFoundException() {
    super(RESOURCE_NOT_FOUND_MESSAGE);
    this.code = RESOURCE_NOT_FOUND_MESSAGE;
  }

  public ResourceNotFoundException(Throwable cause) {
    super(RESOURCE_NOT_FOUND_MESSAGE, cause);
    this.code = RESOURCE_NOT_FOUND_MESSAGE;
  }

  public ResourceNotFoundException(String code, String... args) {
    super(code);
    this.code = code;
    this.args = args;
  }

  public ResourceNotFoundException(Throwable cause, String code, String... args) {
    super(code, cause);
    this.code = code;
    this.args = args;
  }

}
