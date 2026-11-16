package com.petclinic.owner.domain;

import java.time.LocalDate;

public final class BirthDateInFutureException extends InvalidValueException {

  public BirthDateInFutureException(LocalDate birthDate) {
    super("Birth date " + birthDate + " can't be in the future");
  }
}
