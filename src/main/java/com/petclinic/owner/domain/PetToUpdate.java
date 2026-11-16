package com.petclinic.owner.domain;

import com.petclinic.shared.error.domain.Assert;
import java.time.LocalDate;

public record PetToUpdate(PetName name, LocalDate birthDate, PetType type) {
  public PetToUpdate {
    Assert.notNull("name", name);
    Assert.notNull("birthDate", birthDate);
    Assert.notNull("type", type);
  }
}
