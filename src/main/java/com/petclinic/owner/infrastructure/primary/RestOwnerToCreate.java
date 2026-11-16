package com.petclinic.owner.infrastructure.primary;

import com.petclinic.owner.domain.Address;
import com.petclinic.owner.domain.City;
import com.petclinic.owner.domain.Firstname;
import com.petclinic.owner.domain.Lastname;
import com.petclinic.owner.domain.OwnerToCreate;
import com.petclinic.owner.domain.Telephone;
import org.jspecify.annotations.Nullable;

record RestOwnerToCreate(
  @Nullable String firstName,
  @Nullable String lastName,
  @Nullable String address,
  @Nullable String city,
  @Nullable String telephone
) {
  OwnerToCreate toDomain() {
    return new OwnerToCreate(
      new Firstname(firstName),
      new Lastname(lastName),
      new Address(address),
      new City(city),
      new Telephone(telephone)
    );
  }
}
