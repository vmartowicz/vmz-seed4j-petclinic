package com.petclinic.owner.domain;

import org.jspecify.annotations.Nullable;

public record LastnameStart(String start) {
  public LastnameStart(@Nullable String start) {
    this.start = start == null ? "" : start.strip();
  }

  public String get() {
    return start();
  }
}
