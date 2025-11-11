package com.petclinic.owner.infrastructure.primary.cli;

import com.petclinic.owner.domain.pettype.PetTypes;
import com.petclinic.shared.error.domain.Assert;
import java.util.Collection;

final class CliPetTypes {

  private final Collection<CliPetType> pettypes;

  private CliPetTypes(Collection<CliPetType> pettypes) {
    this.pettypes = pettypes;
  }

  public static CliPetTypes from(PetTypes pettypes) {
    Assert.notNull("pettypes", pettypes);
    return new CliPetTypes(pettypes.stream().map(CliPetType::from).toList());
  }

  public Collection<CliPetType> getPettypes() {
    return pettypes;
  }
}
