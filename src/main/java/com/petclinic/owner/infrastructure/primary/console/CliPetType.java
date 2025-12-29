package com.petclinic.owner.infrastructure.primary.console;

import com.petclinic.owner.domain.pettype.PetType;
import com.petclinic.shared.error.domain.Assert;
import java.util.UUID;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

final class CliPetType {

  private final UUID id;
  private final String name;

  private CliPetType(CliPetTypeBuilder builder) {
    id = builder.id;
    name = builder.name;
  }

  public static CliPetType from(PetType petType) {
    Assert.notNull("pettype", petType);

    return new CliPetTypeBuilder().id(petType.id().get()).name(petType.name().get()).build();
  }

  private static final class CliPetTypeBuilder {

    private UUID id;
    private String name;

    public CliPetTypeBuilder id(UUID id) {
      this.id = id;

      return this;
    }

    public CliPetTypeBuilder name(String name) {
      this.name = name;

      return this;
    }

    public CliPetType build() {
      return new CliPetType(this);
    }
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
  }
}
