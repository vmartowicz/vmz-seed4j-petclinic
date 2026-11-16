package com.petclinic.vet.domain;

import com.petclinic.shared.error.domain.Assert;

public record Firstname(String firstname) {
  public Firstname {
    Assert.field("firstname", firstname).notBlank().maxLength(30);
  }

  public String get() {
    return firstname();
  }
}
