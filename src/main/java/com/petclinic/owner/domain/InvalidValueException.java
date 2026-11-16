package com.petclinic.owner.domain;

public abstract class InvalidValueException extends RuntimeException {

  protected InvalidValueException(String message) {
    super(message);
  }
}
