package com.petclinic.vet.application;

import com.petclinic.shared.pagination.domain.Seed4jpetclinicPage;
import com.petclinic.shared.pagination.domain.Seed4jpetclinicPageable;
import com.petclinic.vet.domain.Vet;
import com.petclinic.vet.domain.VetsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class VetsApplicationService {

  private static final int PAGE_SIZE = 5;

  private final VetsRepository vets;

  public VetsApplicationService(VetsRepository vets) {
    this.vets = vets;
  }

  public Seed4jpetclinicPage<Vet> list(int page) {
    return vets.list(new Seed4jpetclinicPageable(page, PAGE_SIZE));
  }
}
