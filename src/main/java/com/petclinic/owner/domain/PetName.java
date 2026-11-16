package com.petclinic.owner.domain;

import com.petclinic.shared.error.domain.Assert;

public record PetName(String name) {
  public PetName {
    Assert.field("name", name).notBlank().maxLength(30);
  }

  public boolean sameAs(PetName other) {
    Assert.notNull("other", other);

    return name.equalsIgnoreCase(other.name);
  }

  public String get() {
    return name();
  }
}
