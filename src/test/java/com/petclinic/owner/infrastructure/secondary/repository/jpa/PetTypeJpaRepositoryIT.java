package com.petclinic.owner.infrastructure.secondary.repository.jpa;

import static com.petclinic.owner.domain.pettype.PetTypesFixture.petType;
import static com.petclinic.owner.domain.pettype.PetTypesIdentityFixture.cloackOfFeathersId;
import static org.assertj.core.api.Assertions.assertThat;

import com.petclinic.IntegrationTest;
import com.petclinic.owner.infrastructure.secondary.entity.PetTypeEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@IntegrationTest
class PetTypeJpaRepositoryIT {

  @Autowired
  private PetTypeJpaRepository petTypes;

  @Test
  void shouldSaveAndGetPetTypes() {
    petTypes.save(PetTypeEntity.from(petType()));

    assertThat(petTypes.findById(cloackOfFeathersId().id()).get().toDomain()).usingRecursiveComparison().isEqualTo(petType());
  }
}
