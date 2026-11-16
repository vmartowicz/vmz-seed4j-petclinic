package com.petclinic.owner.domain;

import com.petclinic.shared.error.domain.Assert;
import java.util.UUID;

public record OwnerId(UUID id) {
  public OwnerId {
    Assert.notNull("id", id);
  }

  public static OwnerId newId() {
    return new OwnerId(UUID.randomUUID());
  }

  public UUID get() {
    return id();
  }
}
