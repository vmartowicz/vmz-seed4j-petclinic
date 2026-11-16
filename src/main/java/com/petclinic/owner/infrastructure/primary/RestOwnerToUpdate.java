package com.petclinic.owner.infrastructure.primary;

import com.petclinic.owner.domain.Address;
import com.petclinic.owner.domain.City;
import com.petclinic.owner.domain.Firstname;
import com.petclinic.owner.domain.Lastname;
import com.petclinic.owner.domain.OwnerToUpdate;
import com.petclinic.owner.domain.Telephone;
import org.jspecify.annotations.Nullable;

record RestOwnerToUpdate(
  @Nullable String firstName,
  @Nullable String lastName,
  @Nullable String address,
  @Nullable String city,
  @Nullable String telephone
) {
  OwnerToUpdate toDomain() {
    return new OwnerToUpdate(
      new Firstname(firstName),
      new Lastname(lastName),
      new Address(address),
      new City(city),
      new Telephone(telephone)
    );
  }
}
