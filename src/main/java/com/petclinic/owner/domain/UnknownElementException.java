package com.petclinic.owner.domain;

public abstract class UnknownElementException extends RuntimeException {

  protected UnknownElementException(String message) {
    super(message);
  }
}
