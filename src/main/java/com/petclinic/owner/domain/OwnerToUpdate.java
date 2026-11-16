package com.petclinic.owner.domain;

import com.petclinic.shared.error.domain.Assert;

public record OwnerToUpdate(Firstname firstname, Lastname lastname, Address address, City city, Telephone telephone) {
  public OwnerToUpdate {
    Assert.notNull("firstname", firstname);
    Assert.notNull("lastname", lastname);
    Assert.notNull("address", address);
    Assert.notNull("city", city);
    Assert.notNull("telephone", telephone);
  }
}
