package com.petclinic.owner.domain.pettype;

import com.petclinic.shared.error.domain.Assert;

public class PetTypesCreator {

  private final PetTypesRepository pettypes;

  public PetTypesCreator(PetTypesRepository pettypes) {
    Assert.notNull("pettypes", pettypes);

    this.pettypes = pettypes;
  }

  public PetType create(PetTypeToCreate petTypeToCreate) {
    var createdPetType = petTypeToCreate.create();
    return pettypes.save(createdPetType);
  }

  public PetType update(PetTypeToUpdate petTypeToUpdate) {
    var updatePetType = petTypeToUpdate.update();
    return pettypes.save(updatePetType);
  }
}
