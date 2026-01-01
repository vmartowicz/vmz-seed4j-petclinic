package com.petclinic.owner.infrastructure.secondary.repository.inmemory;

import com.petclinic.owner.domain.pettype.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class PetTypeInMemoryRepository implements PetTypesRepository {

  private static final Map<PetTypeId, PetType> PET_TYPES = buildPetTypes();

  private static Map<PetTypeId, PetType> buildPetTypes() {
    //return new HashMap<>(Map.of(petTypeId(), petType()));
    return new HashMap<>();
  }

  private static PetTypeId petTypeId() {
    return new PetTypeId(UUID.fromString("716d28f5-fb62-4a7f-9ce0-360d1893aed5"));
  }

  private static PetType petType() {
    return PetType.builder().id(petTypeId()).name(petTypeName()).build();
  }

  private static PetTypeName petTypeName() {
    return new PetTypeName("Dog");
  }

  @Override
  public PetTypes findAll() {
    return new PetTypes(PET_TYPES.values());
  }

  @Override
  public PetType save(PetType petType) {
    PET_TYPES.put(petType.id(), petType);
    return petType;
  }

  @Override
  public void remove(PetTypeId pettype) {
    PET_TYPES.remove(pettype);
  }

  @Override
  public Optional<PetType> get(PetTypeId pettype) {
    return Optional.ofNullable(PET_TYPES.get(pettype));
  }
}
