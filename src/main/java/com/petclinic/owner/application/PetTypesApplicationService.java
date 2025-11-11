package com.petclinic.owner.application;

import com.petclinic.owner.domain.pettype.*;
import java.util.Optional;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PetTypesApplicationService {

  private final PetTypesRepository pettypes;
  private final PetTypesCreator creator;
  private final PetTypesRemover remover;

  public PetTypesApplicationService(PetTypesRepository petTypesRepository) {
    this.pettypes = petTypesRepository;
    creator = new PetTypesCreator(pettypes);
    remover = new PetTypesRemover(pettypes);
  }

  @Transactional(readOnly = true)
  public PetTypes findAllPetTypes() {
    return pettypes.findAll();
  }

  @Transactional(readOnly = true)
  public Optional<PetType> find(PetTypeId petType) {
    return pettypes.get(petType);
  }

  @Transactional
  @PreAuthorize("can('create', #petTypeToCreate)")
  public PetType create(PetTypeToCreate petTypeToCreate) {
    return creator.create(petTypeToCreate);
  }

  @Transactional
  @PreAuthorize("can('remove', #pettype)")
  public void remove(PetTypeId pettype) {
    remover.remove(pettype);
  }

  @Transactional
  @PreAuthorize("can('update', #petTypeToUpdate)")
  public PetType update(PetTypeToUpdate petTypeToUpdate) {
    return creator.update(petTypeToUpdate);
  }
}
