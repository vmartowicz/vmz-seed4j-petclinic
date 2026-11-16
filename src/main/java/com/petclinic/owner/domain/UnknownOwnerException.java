package com.petclinic.owner.domain;

public final class UnknownOwnerException extends UnknownElementException {

  public UnknownOwnerException(OwnerId owner) {
    super("Owner " + owner.get() + " is unknown");
  }
}
