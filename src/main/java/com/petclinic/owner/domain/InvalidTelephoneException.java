package com.petclinic.owner.domain;

public final class InvalidTelephoneException extends InvalidValueException {

  public InvalidTelephoneException(String telephone) {
    super("Telephone \"" + telephone + "\" must have exactly 10 digits");
  }
}
