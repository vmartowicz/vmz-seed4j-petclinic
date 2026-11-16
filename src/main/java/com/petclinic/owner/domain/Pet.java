package com.petclinic.owner.domain;

import com.petclinic.shared.error.domain.Assert;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import org.jspecify.annotations.Nullable;

public final class Pet {

  private static final Comparator<Visit> BY_DATE = Comparator.comparing(Visit::date);

  private final PetId id;
  private final List<Visit> visits;

  private PetName name;
  private LocalDate birthDate;
  private PetType type;

  private Pet(PetBuilder builder) {
    Assert.notNull("id", builder.id);
    Assert.notNull("name", builder.name);
    Assert.notNull("birthDate", builder.birthDate);
    Assert.notNull("type", builder.type);
    Assert.field("visits", builder.visits).notNull().noNullElement();

    id = builder.id;
    name = builder.name;
    birthDate = builder.birthDate;
    type = builder.type;
    visits = new ArrayList<>(builder.visits);
  }

  public static PetBuilder builder() {
    return new PetBuilder();
  }

  void update(PetToUpdate pet) {
    name = pet.name();
    birthDate = pet.birthDate();
    type = pet.type();
  }

  void add(Visit visit) {
    visits.add(visit);
  }

  public PetId id() {
    return id;
  }

  public PetName name() {
    return name;
  }

  public LocalDate birthDate() {
    return birthDate;
  }

  public PetType type() {
    return type;
  }

  public List<Visit> visits() {
    return visits.stream().sorted(BY_DATE).toList();
  }

  public static final class PetBuilder {

    private @Nullable PetId id;
    private @Nullable PetName name;
    private @Nullable LocalDate birthDate;
    private @Nullable PetType type;
    private Collection<Visit> visits = List.of();

    private PetBuilder() {}

    public PetBuilder id(PetId id) {
      this.id = id;

      return this;
    }

    public PetBuilder name(PetName name) {
      this.name = name;

      return this;
    }

    public PetBuilder birthDate(LocalDate birthDate) {
      this.birthDate = birthDate;

      return this;
    }

    public PetBuilder type(PetType type) {
      this.type = type;

      return this;
    }

    public PetBuilder visits(Collection<Visit> visits) {
      this.visits = visits;

      return this;
    }

    public Pet build() {
      return new Pet(this);
    }
  }
}
