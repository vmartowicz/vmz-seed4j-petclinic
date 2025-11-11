package com.petclinic.owner.infrastructure.secondary.repository;

import com.petclinic.owner.domain.pettype.PetType;
import com.petclinic.owner.domain.pettype.PetTypeId;
import com.petclinic.owner.domain.pettype.PetTypes;
import com.petclinic.owner.domain.pettype.PetTypesRepository;
import com.petclinic.owner.infrastructure.secondary.entity.PetTypeEntity;
import com.petclinic.owner.infrastructure.secondary.repository.jpa.PetTypeJpaRepository;
import com.petclinic.shared.error.domain.Assert;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class PetTypesRepositoryImpl implements PetTypesRepository {

  private final PetTypeJpaRepository pettypes;

  public PetTypesRepositoryImpl(PetTypeJpaRepository pettypes) {
    this.pettypes = pettypes;
  }

  @Override
  public PetTypes findAll() {
    return new PetTypes(pettypes.findAll().stream().map(PetTypeEntity::toDomain).collect(Collectors.toList()));
  }

  @Override
  public PetType save(PetType petType) {
    Assert.notNull("petType", petType);
    return pettypes.save(PetTypeEntity.from(petType)).toDomain();
  }

  @Override
  public void remove(PetTypeId pettype) {
    Assert.notNull("pettype", pettype);
    pettypes.deleteById(pettype.id());
  }

  @Override
  public Optional<PetType> get(PetTypeId pettype) {
    Assert.notNull("pettype", pettype);
    return pettypes.findById(pettype.id()).map(PetTypeEntity::toDomain);
  }
}
