package com.petclinic.owner.infrastructure.primary;

import com.petclinic.owner.domain.Owner;
import com.petclinic.shared.pagination.domain.Seed4jpetclinicPage;
import java.util.List;

record RestOwnersPage(List<RestOwner> owners, int currentPage, int pageCount, long totalElementsCount) {
  static RestOwnersPage from(Seed4jpetclinicPage<Owner> page) {
    return new RestOwnersPage(
      page.content().stream().map(RestOwner::from).toList(),
      page.currentPage(),
      page.pageCount(),
      page.totalElementsCount()
    );
  }
}
