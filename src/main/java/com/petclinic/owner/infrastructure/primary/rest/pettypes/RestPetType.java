package com.petclinic.owner.infrastructure.primary.rest.pettypes;

import com.petclinic.owner.domain.pettype.PetType;
import com.petclinic.shared.error.domain.Assert;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import java.util.UUID;

@Schema(name = "pettype", description = "A Pet Type")
final class RestPetType {

  private final UUID id;
  private final String name;

  private RestPetType(RestPetTypeBuilder builder) {
    id = builder.id;
    name = builder.name;
  }

  public static RestPetType from(PetType petType) {
    Assert.notNull("pettype", petType);

    return new RestPetTypeBuilder().id(petType.id().get()).name(petType.name().get()).build();
  }

  @Schema(description = "ID of this pet type", requiredMode = RequiredMode.REQUIRED)
  public UUID getId() {
    return id;
  }

  @Schema(description = "Name of this pett ype", requiredMode = RequiredMode.REQUIRED)
  public String getName() {
    return name;
  }

  private static final class RestPetTypeBuilder {

    private UUID id;
    private String name;

    public RestPetTypeBuilder id(UUID id) {
      this.id = id;

      return this;
    }

    public RestPetTypeBuilder name(String name) {
      this.name = name;

      return this;
    }

    public RestPetType build() {
      return new RestPetType(this);
    }
  }
}
