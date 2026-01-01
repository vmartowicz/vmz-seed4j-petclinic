package com.petclinic.owner.infrastructure.primary.rest.pettypes;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.petclinic.owner.domain.pettype.PetTypeName;
import com.petclinic.owner.domain.pettype.PetTypeToCreate;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(name = "petTypeToCreate", description = "A pet type to create")
public class RestPetTypeToCreate {

  private final String name;

  RestPetTypeToCreate(@JsonProperty("name") String name) {
    this.name = name;
  }

  public PetTypeToCreate toDomain() {
    return new PetTypeToCreate(new PetTypeName(getName()));
  }

  @NotNull
  @Schema(description = "Name of this pet type", requiredMode = Schema.RequiredMode.REQUIRED)
  public String getName() {
    return name;
  }
}
