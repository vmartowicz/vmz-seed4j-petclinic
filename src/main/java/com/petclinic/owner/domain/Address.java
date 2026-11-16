package com.petclinic.owner.domain;

import com.petclinic.shared.error.domain.Assert;

public record Address(String address) {
  public Address {
    Assert.field("address", address).notBlank().maxLength(255);
  }

  public String get() {
    return address();
  }
}
