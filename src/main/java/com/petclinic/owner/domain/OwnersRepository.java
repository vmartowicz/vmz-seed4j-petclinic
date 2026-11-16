package com.petclinic.owner.domain;

import com.petclinic.shared.pagination.domain.JhipetclinicPage;
import com.petclinic.shared.pagination.domain.JhipetclinicPageable;
import java.util.Optional;

public interface OwnersRepository {
  void save(Owner owner);

  Optional<Owner> get(OwnerId owner);

  JhipetclinicPage<Owner> search(LastnameStart lastname, JhipetclinicPageable pageable);
}
