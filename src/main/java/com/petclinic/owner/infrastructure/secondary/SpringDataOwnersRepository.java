package com.petclinic.owner.infrastructure.secondary;

import com.petclinic.owner.domain.LastnameStart;
import com.petclinic.owner.domain.Owner;
import com.petclinic.owner.domain.OwnerId;
import com.petclinic.owner.domain.OwnersRepository;
import com.petclinic.shared.error.domain.Assert;
import com.petclinic.shared.pagination.domain.Seed4jpetclinicPage;
import com.petclinic.shared.pagination.domain.Seed4jpetclinicPageable;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

@Repository
class SpringDataOwnersRepository implements OwnersRepository {

  private static final Sort BY_NAME = Sort.by("lastName", "firstName", "id");

  private final JpaOwnersRepository owners;

  public SpringDataOwnersRepository(JpaOwnersRepository owners) {
    this.owners = owners;
  }

  @Override
  public void save(Owner owner) {
    Assert.notNull("owner", owner);

    owners.save(OwnerEntity.from(owner));
  }

  @Override
  public Optional<Owner> get(OwnerId owner) {
    Assert.notNull("owner", owner);

    return owners.findById(owner.get()).map(OwnerEntity::toDomain);
  }

  @Override
  public Seed4jpetclinicPage<Owner> search(LastnameStart lastname, Seed4jpetclinicPageable pageable) {
    Assert.notNull("lastname", lastname);
    Assert.notNull("pageable", pageable);

    Page<OwnerEntity> page = owners.findByLastNameStartingWithIgnoreCase(
      lastname.get(),
      PageRequest.of(pageable.page(), pageable.pageSize(), BY_NAME)
    );

    return Seed4jpetclinicPage.builder(page.getContent().stream().map(OwnerEntity::toDomain).toList())
      .currentPage(pageable.page())
      .pageSize(pageable.pageSize())
      .totalElementsCount(page.getTotalElements())
      .build();
  }
}
