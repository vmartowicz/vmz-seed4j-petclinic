package com.petclinic.vet.domain;

import com.petclinic.shared.pagination.domain.JhipetclinicPage;
import com.petclinic.shared.pagination.domain.JhipetclinicPageable;

public interface VetsRepository {
  JhipetclinicPage<Vet> list(JhipetclinicPageable pageable);
}
