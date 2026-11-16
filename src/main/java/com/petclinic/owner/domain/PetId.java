package com.petclinic.owner.domain;

import com.petclinic.shared.error.domain.Assert;
import java.util.UUID;

public record PetId(UUID id) {
  public PetId {
    Assert.notNull("id", id);
  }

  public static PetId newId() {
    return new PetId(UUID.randomUUID());
  }

  public UUID get() {
    return id();
  }
}
