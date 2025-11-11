package com.petclinic.owner.infrastructure.primary.rest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.petclinic.JsonHelper;
import com.petclinic.UnitTest;
import com.petclinic.owner.domain.OwnerFixture;
import org.junit.jupiter.api.Test;

@UnitTest
public class RestOwnerTest {

  @Test
  void shouldSerializeJson() {
    assertThat(JsonHelper.writeAsString(RestOwner.from(OwnerFixture.owner()))).isEqualTo(json());
  }

  private static String json() {
    return """
    {\
    "id":"d4c3b2a1-0f9e-8d7c-6b5a-4c3b2a1d0e9f",\
    "recipient":{\
    "name":{"firstname":"John","lastname":"Doe"},\
    "address":{"zipCode":"62701","street":"123 Main St","city":"Springfield"},\
    "phone":{"value":"123-456-7890"}}\
    }\
    """;
  }
}
