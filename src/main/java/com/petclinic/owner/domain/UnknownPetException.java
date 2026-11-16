package com.petclinic.owner.domain;

public final class UnknownPetException extends UnknownElementException {

  public UnknownPetException(OwnerId owner, PetId pet) {
    super("Pet " + pet.get() + " is unknown for owner " + owner.get());
  }
}
