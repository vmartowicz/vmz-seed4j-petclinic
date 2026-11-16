package com.petclinic.vet.domain;

import com.petclinic.shared.error.domain.Assert;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;
import org.jmolecules.ddd.annotation.AggregateRoot;
import org.jspecify.annotations.Nullable;

@AggregateRoot
public final class Vet {

  private final VetId id;
  private final Firstname firstname;
  private final Lastname lastname;
  private final Set<Specialty> specialties;

  private Vet(VetBuilder builder) {
    Assert.notNull("id", builder.id);
    Assert.notNull("firstname", builder.firstname);
    Assert.notNull("lastname", builder.lastname);
    Assert.field("specialties", builder.specialties).notNull().noNullElement();

    id = builder.id;
    firstname = builder.firstname;
    lastname = builder.lastname;
    specialties = sortedSpecialties(builder.specialties);
  }

  private static Set<Specialty> sortedSpecialties(Collection<Specialty> specialties) {
    EnumSet<Specialty> sorted = EnumSet.noneOf(Specialty.class);
    sorted.addAll(specialties);

    return Collections.unmodifiableSet(sorted);
  }

  public static VetBuilder builder() {
    return new VetBuilder();
  }

  public VetId id() {
    return id;
  }

  public Firstname firstname() {
    return firstname;
  }

  public Lastname lastname() {
    return lastname;
  }

  public Set<Specialty> specialties() {
    return specialties;
  }

  public static final class VetBuilder {

    private @Nullable VetId id;
    private @Nullable Firstname firstname;
    private @Nullable Lastname lastname;
    private Collection<Specialty> specialties = Set.of();

    private VetBuilder() {}

    public VetBuilder id(VetId id) {
      this.id = id;

      return this;
    }

    public VetBuilder firstname(Firstname firstname) {
      this.firstname = firstname;

      return this;
    }

    public VetBuilder lastname(Lastname lastname) {
      this.lastname = lastname;

      return this;
    }

    public VetBuilder specialties(Collection<Specialty> specialties) {
      this.specialties = specialties;

      return this;
    }

    public Vet build() {
      return new Vet(this);
    }
  }
}
