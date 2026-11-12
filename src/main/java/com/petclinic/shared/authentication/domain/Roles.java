package com.petclinic.shared.authentication.domain;

import com.petclinic.shared.collection.domain.JhipetclinicCollections;
import com.petclinic.shared.error.domain.Assert;
import java.util.Set;
import java.util.stream.Stream;

public record Roles(Set<Role> roles) {
  public static final Roles EMPTY = new Roles(Set.of());

  public Roles(Set<Role> roles) {
    this.roles = JhipetclinicCollections.immutable(roles);
  }

  public boolean hasRole() {
    return !roles.isEmpty();
  }

  public boolean hasRole(Role role) {
    Assert.notNull("role", role);

    return roles.contains(role);
  }

  public Stream<Role> stream() {
    return get().stream();
  }

  public Set<Role> get() {
    return roles();
  }
}
