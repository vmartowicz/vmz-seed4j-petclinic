package com.petclinic.owner.domain;

import com.petclinic.shared.error.domain.Assert;
import java.time.LocalDate;

public record PetToCreate(PetName name, LocalDate birthDate, PetType type) {
  public PetToCreate {
    Assert.notNull("name", name);
    Assert.notNull("birthDate", birthDate);
    Assert.notNull("type", type);
  }

  Pet create() {
    return Pet.builder().id(PetId.newId()).name(name).birthDate(birthDate).type(type).build();
  }
}
