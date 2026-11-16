package com.petclinic.owner.infrastructure.primary;

import com.petclinic.owner.domain.VisitDescription;
import com.petclinic.owner.domain.VisitToCreate;
import java.time.LocalDate;
import org.jspecify.annotations.Nullable;

record RestVisitToCreate(@Nullable LocalDate date, @Nullable String description) {
  VisitToCreate toDomain() {
    return new VisitToCreate(date, new VisitDescription(description));
  }
}
