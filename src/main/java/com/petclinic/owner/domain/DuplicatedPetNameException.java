package com.petclinic.owner.domain;

public final class DuplicatedPetNameException extends BusinessRuleViolationException {

  public DuplicatedPetNameException(PetName name) {
    super("The owner already has a pet named \"" + name.get() + "\"");
  }
}
