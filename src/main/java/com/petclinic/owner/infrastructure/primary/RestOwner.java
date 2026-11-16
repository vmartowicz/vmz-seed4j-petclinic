package com.petclinic.owner.infrastructure.primary;

import com.petclinic.owner.domain.Owner;
import java.util.List;
import java.util.UUID;

record RestOwner(UUID id, String firstName, String lastName, String address, String city, String telephone, List<RestPet> pets) {
  static RestOwner from(Owner owner) {
    return new RestOwner(
      owner.id().get(),
      owner.firstname().get(),
      owner.lastname().get(),
      owner.address().get(),
      owner.city().get(),
      owner.telephone().get(),
      owner.pets().stream().map(RestPet::from).toList()
    );
  }
}
