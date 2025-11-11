package com.petclinic.owner.domain.pettype;

import com.petclinic.shared.error.domain.Assert;

public class PetTypesRemover {

  private final PetTypesRepository pettypes;

  public PetTypesRemover(PetTypesRepository pettypes) {
    Assert.notNull("pettypes", pettypes);
    this.pettypes = pettypes;
  }

  public void remove(PetTypeId id) {
    Assert.notNull("id", id);
    pettypes.remove(id);
  }
}
