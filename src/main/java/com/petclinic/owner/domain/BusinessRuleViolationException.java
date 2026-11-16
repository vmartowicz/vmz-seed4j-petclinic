package com.petclinic.owner.domain;

public abstract class BusinessRuleViolationException extends RuntimeException {

  protected BusinessRuleViolationException(String message) {
    super(message);
  }
}
