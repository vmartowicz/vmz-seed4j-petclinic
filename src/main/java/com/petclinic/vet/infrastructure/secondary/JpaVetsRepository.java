package com.petclinic.vet.infrastructure.secondary;

import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.Repository;

interface JpaVetsRepository extends Repository<VetEntity, UUID> {
  Page<VetEntity> findAll(Pageable pageable);
}
