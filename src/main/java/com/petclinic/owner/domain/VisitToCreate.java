package com.petclinic.owner.domain;

import com.petclinic.shared.error.domain.Assert;
import java.time.LocalDate;

public record VisitToCreate(LocalDate date, VisitDescription description) {
  public VisitToCreate {
    Assert.notNull("date", date);
    Assert.notNull("description", description);
  }

  boolean isAfter(LocalDate today) {
    return date.isAfter(today);
  }

  Visit create() {
    return new Visit(VisitId.newId(), date, description);
  }
}
