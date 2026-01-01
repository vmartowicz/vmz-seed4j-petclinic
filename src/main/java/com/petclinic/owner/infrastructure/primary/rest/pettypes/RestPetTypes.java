package com.petclinic.owner.infrastructure.primary.rest.pettypes;

import com.petclinic.owner.domain.pettype.PetTypes;
import com.petclinic.shared.error.domain.Assert;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Collection;

@Schema(name = "pettypes", description = "Some Pet Types")
final class RestPetTypes {

  private final Collection<RestPetType> pettypes;

  private RestPetTypes(Collection<RestPetType> pettypes) {
    this.pettypes = pettypes;
  }

  public static RestPetTypes from(PetTypes pettypes) {
    Assert.notNull("pettypes", pettypes);
    return new RestPetTypes(pettypes.stream().map(RestPetType::from).toList());
  }

  public Collection<RestPetType> getPettypes() {
    return pettypes;
  }
}
