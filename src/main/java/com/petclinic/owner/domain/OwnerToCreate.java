package com.petclinic.owner.domain;

import com.petclinic.shared.error.domain.Assert;

public record OwnerToCreate(Firstname firstname, Lastname lastname, Address address, City city, Telephone telephone) {
  public OwnerToCreate {
    Assert.notNull("firstname", firstname);
    Assert.notNull("lastname", lastname);
    Assert.notNull("address", address);
    Assert.notNull("city", city);
    Assert.notNull("telephone", telephone);
  }

  public Owner create() {
    return Owner.builder()
      .id(OwnerId.newId())
      .firstname(firstname)
      .lastname(lastname)
      .address(address)
      .city(city)
      .telephone(telephone)
      .build();
  }
}
