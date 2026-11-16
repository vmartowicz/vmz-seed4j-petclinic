package com.petclinic.owner.infrastructure.secondary;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;

interface JpaOwnersRepository extends Repository<OwnerEntity, UUID> {
  OwnerEntity save(OwnerEntity owner);

  Optional<OwnerEntity> findById(UUID id);

  Page<OwnerEntity> findByLastNameStartingWithIgnoreCase(String lastName, Pageable pageable);

  long count();
}
