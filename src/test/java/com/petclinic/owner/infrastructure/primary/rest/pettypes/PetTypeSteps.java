package com.petclinic.owner.infrastructure.primary.rest.pettypes;

import static com.petclinic.cucumber.rest.CucumberRestAssertions.assertThatLastResponse;

import com.petclinic.cucumber.rest.CucumberRestTemplate;
import com.petclinic.cucumber.rest.CucumberRestTestContext;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.List;
import java.util.Map;

public class PetTypeSteps {

  private static final String ADD_PET_TYPE_TEMPLATE = """
    {
      "name": "{NAME}"
    }
    """;

  private final CucumberRestTemplate rest;

  PetTypeSteps(CucumberRestTemplate rest) {
    this.rest = rest;
  }

  @When("I add petType to clinic")
  public void addPetTypeToClinic(Map<String, String> petsType) {
    String payload = ADD_PET_TYPE_TEMPLATE.replace("{NAME}", petsType.get("Name"));
    rest.post("/api/pet-types", payload);
  }

  @When("I remove the created petType from the clinic")
  public void removeCreatedPetTypeFromClinic() {
    rest.delete("/api/pet-types/" + CucumberRestTestContext.getElement("$.id"));
  }

  @Then("I should have petTypes")
  public void shouldHavePetTypesClinic(List<Map<String, String>> petTypes) {
    rest.get("/api/pet-types");

    assertThatLastResponse().hasOkStatus().hasElement("$.pettypes").containingExactly(petTypes);
  }

  @Then("I should have created petType")
  public void shouldHaveCreatedPetType(Map<String, String> petTypes) {
    rest.get("/api/pet-types/" + CucumberRestTestContext.getElement("$.id"));

    assertThatLastResponse().hasOkStatus().hasResponse().containing(petTypes);
  }
}
