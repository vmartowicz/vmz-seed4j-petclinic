package com.petclinic.owner.domain;

import com.petclinic.shared.error.domain.Assert;

public record VisitDescription(String description) {
  public VisitDescription {
    Assert.field("description", description).notBlank().maxLength(255);
  }

  public String get() {
    return description();
  }
}
