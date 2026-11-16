package com.petclinic.owner.domain;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public final class OwnersFixture {

  public static final LocalDate TODAY = LocalDate.of(2026, 11, 15);

  private OwnersFixture() {}

  public static OwnerId georgeId() {
    return new OwnerId(UUID.fromString("6f0d5f0a-0e1b-4a9e-9a52-1b9a6b5d1a01"));
  }

  public static PetId leoId() {
    return new PetId(UUID.fromString("6f0d5f0a-0e1b-4a9e-9a52-1b9a6b5d1a02"));
  }

  public static PetId bellaId() {
    return new PetId(UUID.fromString("6f0d5f0a-0e1b-4a9e-9a52-1b9a6b5d1a03"));
  }

  public static VisitId rabiesShotId() {
    return new VisitId(UUID.fromString("6f0d5f0a-0e1b-4a9e-9a52-1b9a6b5d1a04"));
  }

  public static Owner george() {
    return Owner.builder()
      .id(georgeId())
      .firstname(new Firstname("George"))
      .lastname(new Lastname("Franklin"))
      .address(new Address("110 W. Liberty St."))
      .city(new City("Madison"))
      .telephone(new Telephone("6085551023"))
      .pets(List.of(leo(), bella()))
      .build();
  }

  public static Pet leo() {
    return Pet.builder()
      .id(leoId())
      .name(new PetName("Leo"))
      .birthDate(LocalDate.of(2020, 9, 7))
      .type(PetType.CAT)
      .visits(List.of(rabiesShot()))
      .build();
  }

  public static Pet bella() {
    return Pet.builder()
      .id(bellaId())
      .name(new PetName("bella"))
      .birthDate(LocalDate.of(2021, 3, 12))
      .type(PetType.DOG)
      .build();
  }

  public static Visit rabiesShot() {
    return new Visit(rabiesShotId(), LocalDate.of(2026, 1, 10), new VisitDescription("Rabies shot"));
  }

  public static OwnerToCreate ownerToCreate() {
    return new OwnerToCreate(
      new Firstname("Betty"),
      new Lastname("Davis"),
      new Address("638 Cardinal Ave."),
      new City("Sun Prairie"),
      new Telephone("6085551749")
    );
  }

  public static OwnerToUpdate ownerToUpdate() {
    return new OwnerToUpdate(
      new Firstname("Georges"),
      new Lastname("Franklyn"),
      new Address("111 W. Liberty St."),
      new City("Monona"),
      new Telephone("6085551024")
    );
  }

  public static PetToCreate petToCreate(String name, LocalDate birthDate) {
    return new PetToCreate(new PetName(name), birthDate, PetType.HAMSTER);
  }

  public static PetToUpdate petToUpdate(String name, LocalDate birthDate) {
    return new PetToUpdate(new PetName(name), birthDate, PetType.BIRD);
  }

  public static VisitToCreate visitToCreate(LocalDate date) {
    return new VisitToCreate(date, new VisitDescription("Check-up"));
  }
}
