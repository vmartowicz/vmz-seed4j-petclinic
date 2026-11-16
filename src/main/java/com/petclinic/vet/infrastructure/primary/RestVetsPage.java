package com.petclinic.vet.infrastructure.primary;

import com.petclinic.shared.pagination.domain.JhipetclinicPage;
import com.petclinic.vet.domain.Vet;
import java.util.List;

record RestVetsPage(List<RestVet> vets, int currentPage, int pageCount, long totalElementsCount) {
  static RestVetsPage from(JhipetclinicPage<Vet> page) {
    return new RestVetsPage(
      page.content().stream().map(RestVet::from).toList(),
      page.currentPage(),
      page.pageCount(),
      page.totalElementsCount()
    );
  }
}
