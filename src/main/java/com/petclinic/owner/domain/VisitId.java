package com.petclinic.owner.domain;

import com.petclinic.shared.error.domain.Assert;
import java.util.UUID;

public record VisitId(UUID id) {
  public VisitId {
    Assert.notNull("id", id);
  }

  public static VisitId newId() {
    return new VisitId(UUID.randomUUID());
  }

  public UUID get() {
    return id();
  }
}
