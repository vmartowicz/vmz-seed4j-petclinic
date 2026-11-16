package com.petclinic.vet.domain;

import com.petclinic.shared.pagination.domain.Seed4jpetclinicPage;
import com.petclinic.shared.pagination.domain.Seed4jpetclinicPageable;

public interface VetsRepository {
  Seed4jpetclinicPage<Vet> list(Seed4jpetclinicPageable pageable);
}
