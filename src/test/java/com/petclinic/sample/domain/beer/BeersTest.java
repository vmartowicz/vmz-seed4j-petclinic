package com.petclinic.sample.domain.beer;

import static com.petclinic.sample.domain.BeersIdentityFixture.anteMeridiemId;
import static com.petclinic.sample.domain.BeersIdentityFixture.anteMeridiemUnitPrice;
import static com.petclinic.sample.domain.beer.BeersFixture.beer;
import static org.assertj.core.api.Assertions.assertThat;

import com.petclinic.UnitTest;
import java.util.List;
import org.junit.jupiter.api.Test;

@UnitTest
class BeersTest {

  @Test
  void shouldSortBeersByNames() {
    Beer anteMeridiem = Beer.builder()
      .id(anteMeridiemId())
      .name(new BeerName("Ante Meridiem"))
      .unitPrice(anteMeridiemUnitPrice())
      .sellingState(BeerSellingState.SOLD)
      .build();

    var beers = new Beers(List.of(beer(), anteMeridiem));

    assertThat(beers.get()).usingRecursiveFieldByFieldElementComparator().containsExactly(anteMeridiem, beer());
  }
}
