package com.petclinic.vet.infrastructure.secondary;

import com.petclinic.shared.error.domain.Assert;
import com.petclinic.shared.pagination.domain.Seed4jpetclinicPage;
import com.petclinic.shared.pagination.domain.Seed4jpetclinicPageable;
import com.petclinic.vet.domain.Vet;
import com.petclinic.vet.domain.VetsRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

@Repository
class SpringDataVetsRepository implements VetsRepository {

  private static final Sort BY_NAME = Sort.by("lastName", "firstName", "id");

  private final JpaVetsRepository vets;

  public SpringDataVetsRepository(JpaVetsRepository vets) {
    this.vets = vets;
  }

  @Override
  public Seed4jpetclinicPage<Vet> list(Seed4jpetclinicPageable pageable) {
    Assert.notNull("pageable", pageable);

    Page<VetEntity> page = vets.findAll(PageRequest.of(pageable.page(), pageable.pageSize(), BY_NAME));

    return Seed4jpetclinicPage.builder(page.getContent().stream().map(VetEntity::toDomain).toList())
      .currentPage(pageable.page())
      .pageSize(pageable.pageSize())
      .totalElementsCount(page.getTotalElements())
      .build();
  }
}
