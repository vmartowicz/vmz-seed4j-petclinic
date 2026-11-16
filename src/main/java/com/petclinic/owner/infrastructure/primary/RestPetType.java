package com.petclinic.owner.infrastructure.primary;

import com.fasterxml.jackson.annotation.JsonValue;
import com.petclinic.owner.domain.PetType;
import com.petclinic.shared.enumeration.domain.Enums;
import java.util.Locale;
import org.jspecify.annotations.Nullable;

enum RestPetType {
  CAT,
  DOG,
  LIZARD,
  SNAKE,
  BIRD,
  HAMSTER;

  static RestPetType from(PetType type) {
    return Enums.map(type, RestPetType.class);
  }

  static @Nullable PetType toDomain(@Nullable RestPetType type) {
    return Enums.map(type, PetType.class);
  }

  @JsonValue
  String key() {
    return name().toLowerCase(Locale.ROOT);
  }
}
