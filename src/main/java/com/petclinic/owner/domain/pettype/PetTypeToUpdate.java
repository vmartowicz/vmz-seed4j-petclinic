package com.petclinic.owner.domain.pettype;

import com.petclinic.shared.error.domain.Assert;

public record PetTypeToUpdate(PetTypeId id, PetTypeName name) {
  public PetTypeToUpdate {
    Assert.notNull("id", id);
    Assert.notNull("name", name);
  }

  public PetType update() {
    return PetType.builder().id(id()).name(name()).build();
  }
}
