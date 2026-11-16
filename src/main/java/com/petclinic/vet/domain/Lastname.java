package com.petclinic.vet.domain;

import com.petclinic.shared.error.domain.Assert;

public record Lastname(String lastname) {
  public Lastname {
    Assert.field("lastname", lastname).notBlank().maxLength(30);
  }

  public String get() {
    return lastname();
  }
}
