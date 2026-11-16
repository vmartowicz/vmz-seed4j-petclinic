package com.petclinic.owner.infrastructure.primary;

import com.petclinic.owner.domain.PetName;
import com.petclinic.owner.domain.PetToCreate;
import java.time.LocalDate;
import org.jspecify.annotations.Nullable;

record RestPetToCreate(@Nullable String name, @Nullable LocalDate birthDate, @Nullable RestPetType type) {
  PetToCreate toDomain() {
    return new PetToCreate(new PetName(name), birthDate, RestPetType.toDomain(type));
  }
}
