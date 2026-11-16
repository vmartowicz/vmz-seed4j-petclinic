package com.petclinic.vet.infrastructure.primary;

import com.petclinic.vet.domain.Vet;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

record RestVet(UUID id, String firstName, String lastName, List<String> specialties) {
  static RestVet from(Vet vet) {
    return new RestVet(
      vet.id().get(),
      vet.firstname().get(),
      vet.lastname().get(),
      vet
        .specialties()
        .stream()
        .map(specialty -> specialty.name().toLowerCase(Locale.ROOT))
        .toList()
    );
  }
}
