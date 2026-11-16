package com.petclinic.owner.infrastructure.primary;

import com.petclinic.owner.domain.PetName;
import com.petclinic.owner.domain.PetToUpdate;
import java.time.LocalDate;
import org.jspecify.annotations.Nullable;

record RestPetToUpdate(@Nullable String name, @Nullable LocalDate birthDate, @Nullable RestPetType type) {
  PetToUpdate toDomain() {
    return new PetToUpdate(new PetName(name), birthDate, RestPetType.toDomain(type));
  }
}
