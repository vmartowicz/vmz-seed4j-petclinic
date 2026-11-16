package com.petclinic.vet.domain;

import com.petclinic.shared.error.domain.Assert;
import java.util.UUID;

public record VetId(UUID id) {
  public VetId {
    Assert.notNull("id", id);
  }

  public UUID get() {
    return id();
  }
}
