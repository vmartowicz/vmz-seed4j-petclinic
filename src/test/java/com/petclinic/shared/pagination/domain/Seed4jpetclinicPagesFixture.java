package com.petclinic.shared.pagination.domain;

import com.petclinic.shared.pagination.domain.Seed4jpetclinicPage.Seed4jpetclinicPageBuilder;
import java.util.List;

public final class Seed4jpetclinicPagesFixture {

  private Seed4jpetclinicPagesFixture() {}

  public static Seed4jpetclinicPage<String> page() {
    return pageBuilder().build();
  }

  public static Seed4jpetclinicPageBuilder<String> pageBuilder() {
    return Seed4jpetclinicPage.builder(List.of("test")).currentPage(2).pageSize(10).totalElementsCount(21);
  }
}
