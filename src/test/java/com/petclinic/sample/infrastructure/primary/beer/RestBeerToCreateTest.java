package com.petclinic.sample.infrastructure.primary.beer;

import static com.petclinic.BeanValidationAssertions.*;
import static org.assertj.core.api.Assertions.*;

import com.petclinic.JsonHelper;
import com.petclinic.UnitTest;
import com.petclinic.sample.domain.beer.BeersFixture;
import org.junit.jupiter.api.Test;

@UnitTest
class RestBeerToCreateTest {

  @Test
  void shouldDeserializeFromJson() {
    assertThat(JsonHelper.readFromJson(json(), RestBeerToCreate.class).toDomain())
      .usingRecursiveComparison()
      .isEqualTo(BeersFixture.beerToCreate());
  }

  private String json() {
    return """
    {
      "name": "Cloak of feathers",
      "unitPrice": 8.53
    }
    """;
  }

  @Test
  void shouldNotValidateEmptyBean() {
    assertThatBean(new RestBeerToCreate(null, null)).hasInvalidProperty("name").and().hasInvalidProperty("unitPrice");
  }
}
