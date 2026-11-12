package com.petclinic.sample.infrastructure.primary.beer;

import static org.assertj.core.api.Assertions.*;

import com.petclinic.JsonHelper;
import com.petclinic.UnitTest;
import com.petclinic.sample.domain.beer.Beers;
import com.petclinic.sample.domain.beer.BeersFixture;
import java.util.List;
import org.junit.jupiter.api.Test;

@UnitTest
class RestBeersTest {

  @Test
  void shouldSerializeToJson() {
    assertThat(JsonHelper.writeAsString(RestBeers.from(new Beers(List.of(BeersFixture.beer()))))).isEqualTo(json());
  }

  private String json() {
    return "{\"beers\":[" + RestBeerTest.json() + "]}";
  }
}
