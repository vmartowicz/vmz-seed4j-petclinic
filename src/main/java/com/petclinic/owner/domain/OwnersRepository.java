package com.petclinic.owner.domain;

import com.petclinic.shared.pagination.domain.Seed4jpetclinicPage;
import com.petclinic.shared.pagination.domain.Seed4jpetclinicPageable;
import java.util.Optional;

public interface OwnersRepository {
  void save(Owner owner);

  Optional<Owner> get(OwnerId owner);

  Seed4jpetclinicPage<Owner> search(LastnameStart lastname, Seed4jpetclinicPageable pageable);
}
