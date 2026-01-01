package com.petclinic.sample.infrastructure.secondary;

import com.petclinic.sample.domain.BeerId;
import com.petclinic.sample.domain.beer.Beer;
import com.petclinic.sample.domain.beer.BeerSellingState;
import com.petclinic.sample.domain.beer.Beers;
import com.petclinic.sample.domain.beer.BeersRepository;
import com.petclinic.shared.error.domain.Assert;
import java.util.Optional;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

@Repository
@Primary
class SpringDataBeersRepository implements BeersRepository {

  private final JpaBeersRepository beers;

  public SpringDataBeersRepository(JpaBeersRepository beers) {
    this.beers = beers;
  }

  @Override
  public void save(Beer beer) {
    Assert.notNull("beer", beer);

    beers.save(BeerEntity.from(beer));
  }

  @Override
  public Beers catalog() {
    return new Beers(beers.findBySellingState(BeerSellingState.SOLD).stream().map(BeerEntity::toDomain).toList());
  }

  @Override
  public Optional<Beer> get(BeerId beer) {
    Assert.notNull("beer", beer);

    return beers.findById(beer.get()).map(BeerEntity::toDomain);
  }
}
