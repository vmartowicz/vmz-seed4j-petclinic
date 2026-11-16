package com.petclinic.owner.domain;

import com.petclinic.shared.error.domain.Assert;

public record City(String city) {
  public City {
    Assert.field("city", city).notBlank().maxLength(80);
  }

  public String get() {
    return city();
  }
}
