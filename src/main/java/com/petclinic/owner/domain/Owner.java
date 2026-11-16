package com.petclinic.owner.domain;

import com.petclinic.shared.error.domain.Assert;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import org.jmolecules.ddd.annotation.AggregateRoot;
import org.jspecify.annotations.Nullable;

@AggregateRoot
public final class Owner {

  private static final Comparator<Pet> BY_NAME = Comparator.comparing(pet -> pet.name().get(), String.CASE_INSENSITIVE_ORDER);

  private final OwnerId id;
  private final List<Pet> pets;

  private Firstname firstname;
  private Lastname lastname;
  private Address address;
  private City city;
  private Telephone telephone;

  private Owner(OwnerBuilder builder) {
    Assert.notNull("id", builder.id);
    Assert.notNull("firstname", builder.firstname);
    Assert.notNull("lastname", builder.lastname);
    Assert.notNull("address", builder.address);
    Assert.notNull("city", builder.city);
    Assert.notNull("telephone", builder.telephone);
    Assert.field("pets", builder.pets).notNull().noNullElement();

    id = builder.id;
    firstname = builder.firstname;
    lastname = builder.lastname;
    address = builder.address;
    city = builder.city;
    telephone = builder.telephone;
    pets = new ArrayList<>(builder.pets);
  }

  public static OwnerBuilder builder() {
    return new OwnerBuilder();
  }

  public void update(OwnerToUpdate owner) {
    Assert.notNull("owner", owner);

    firstname = owner.firstname();
    lastname = owner.lastname();
    address = owner.address();
    city = owner.city();
    telephone = owner.telephone();
  }

  public Pet addPet(PetToCreate pet, LocalDate today) {
    Assert.notNull("pet", pet);
    Assert.notNull("today", today);

    assertBornBy(pet.birthDate(), today);
    assertNameAvailable(pet.name(), existing -> true);

    Pet created = pet.create();
    pets.add(created);

    return created;
  }

  public Pet updatePet(PetId petId, PetToUpdate pet, LocalDate today) {
    Assert.notNull("pet", pet);
    Assert.notNull("today", today);

    Pet updated = pet(petId);
    assertBornBy(pet.birthDate(), today);
    assertNameAvailable(pet.name(), existing -> !existing.id().equals(petId));

    updated.update(pet);

    return updated;
  }

  public Visit scheduleVisit(PetId petId, VisitToCreate visit, LocalDate today) {
    Assert.notNull("visit", visit);
    Assert.notNull("today", today);

    Pet pet = pet(petId);
    if (!visit.isAfter(today)) {
      throw new VisitNotInFutureException(visit.date());
    }

    Visit created = visit.create();
    pet.add(created);

    return created;
  }

  private Pet pet(PetId petId) {
    Assert.notNull("petId", petId);

    return pets
      .stream()
      .filter(pet -> pet.id().equals(petId))
      .findFirst()
      .orElseThrow(() -> new UnknownPetException(id, petId));
  }

  private static void assertBornBy(LocalDate birthDate, LocalDate today) {
    if (birthDate.isAfter(today)) {
      throw new BirthDateInFutureException(birthDate);
    }
  }

  private void assertNameAvailable(PetName name, Predicate<Pet> otherPets) {
    if (
      pets
        .stream()
        .filter(otherPets)
        .anyMatch(pet -> pet.name().sameAs(name))
    ) {
      throw new DuplicatedPetNameException(name);
    }
  }

  public OwnerId id() {
    return id;
  }

  public Firstname firstname() {
    return firstname;
  }

  public Lastname lastname() {
    return lastname;
  }

  public Address address() {
    return address;
  }

  public City city() {
    return city;
  }

  public Telephone telephone() {
    return telephone;
  }

  public List<Pet> pets() {
    return pets.stream().sorted(BY_NAME).toList();
  }

  public static final class OwnerBuilder {

    private @Nullable OwnerId id;
    private @Nullable Firstname firstname;
    private @Nullable Lastname lastname;
    private @Nullable Address address;
    private @Nullable City city;
    private @Nullable Telephone telephone;
    private Collection<Pet> pets = List.of();

    private OwnerBuilder() {}

    public OwnerBuilder id(OwnerId id) {
      this.id = id;

      return this;
    }

    public OwnerBuilder firstname(Firstname firstname) {
      this.firstname = firstname;

      return this;
    }

    public OwnerBuilder lastname(Lastname lastname) {
      this.lastname = lastname;

      return this;
    }

    public OwnerBuilder address(Address address) {
      this.address = address;

      return this;
    }

    public OwnerBuilder city(City city) {
      this.city = city;

      return this;
    }

    public OwnerBuilder telephone(Telephone telephone) {
      this.telephone = telephone;

      return this;
    }

    public OwnerBuilder pets(Collection<Pet> pets) {
      this.pets = pets;

      return this;
    }

    public Owner build() {
      return new Owner(this);
    }
  }
}
