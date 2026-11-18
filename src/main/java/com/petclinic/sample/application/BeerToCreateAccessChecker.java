package com.petclinic.sample.application;

import com.petclinic.sample.domain.beer.BeerToCreate;
import com.petclinic.shared.kipe.application.AccessChecker;
import com.petclinic.shared.kipe.application.AccessContext;
import com.petclinic.shared.kipe.application.Seed4jpetclinicAuthorizations;
import org.springframework.stereotype.Component;

@Component
class BeerToCreateAccessChecker implements AccessChecker<BeerToCreate> {

  private final Seed4jpetclinicAuthorizations authorizations;

  public BeerToCreateAccessChecker(Seed4jpetclinicAuthorizations authorizations) {
    this.authorizations = authorizations;
  }

  @Override
  public boolean can(AccessContext<BeerToCreate> access) {
    return authorizations.allAuthorized(access.authentication(), access.action(), BeerResource.BEERS);
  }
}
