package com.petclinic.owner.domain;

import java.time.LocalDate;

public final class VisitNotInFutureException extends InvalidValueException {

  public VisitNotInFutureException(LocalDate date) {
    super("Visit date " + date + " must be tomorrow at the earliest");
  }
}
