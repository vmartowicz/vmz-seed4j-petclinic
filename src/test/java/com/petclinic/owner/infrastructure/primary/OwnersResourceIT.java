package com.petclinic.owner.infrastructure.primary;

import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.jayway.jsonpath.JsonPath;
import com.petclinic.IntegrationTest;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@IntegrationTest
@AutoConfigureMockMvc
class OwnersResourceIT {

  private static final LocalDate TODAY = LocalDate.now(ZoneId.of("Europe/Paris"));

  @Autowired
  private MockMvc rest;

  @Test
  void shouldRegisterOwnerAnonymously() throws Exception {
    String lastName = uniqueLastName();

    register(lastName)
      .andExpect(status().isCreated())
      .andExpect(jsonPath("$.id").isNotEmpty())
      .andExpect(jsonPath("$.firstName").value("George"))
      .andExpect(jsonPath("$.lastName").value(lastName))
      .andExpect(jsonPath("$.address").value("110 W. Liberty St."))
      .andExpect(jsonPath("$.city").value("Madison"))
      .andExpect(jsonPath("$.telephone").value("6085551023"))
      .andExpect(jsonPath("$.pets").isEmpty());
  }

  @Test
  void shouldNotRegisterOwnerWithInvalidTelephone() throws Exception {
    rest
      .perform(post("/api/owners").contentType(MediaType.APPLICATION_JSON).content(ownerJson("Davis", "608-555-10")))
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.title").value("Invalid value"))
      .andExpect(jsonPath("$.detail").value("Telephone \"608-555-10\" must have exactly 10 digits"));
  }

  @Test
  void shouldNotRegisterOwnerWithoutFirstName() throws Exception {
    rest
      .perform(
        post("/api/owners")
          .contentType(MediaType.APPLICATION_JSON)
          .content("{\"lastName\":\"Davis\",\"address\":\"1 St.\",\"city\":\"Madison\",\"telephone\":\"6085551023\"}")
      )
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.field").value("firstname"));
  }

  @Test
  void shouldUpdateOwner() throws Exception {
    String owner = createdOwner(uniqueLastName());
    String lastName = uniqueLastName();

    rest
      .perform(
        put("/api/owners/" + owner)
          .contentType(MediaType.APPLICATION_JSON)
          .content(ownerJson(lastName, "0612345678"))
      )
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id").value(owner))
      .andExpect(jsonPath("$.lastName").value(lastName))
      .andExpect(jsonPath("$.telephone").value("0612345678"));

    rest.perform(get("/api/owners/" + owner)).andExpect(jsonPath("$.lastName").value(lastName));
  }

  @Test
  void shouldNotUpdateUnknownOwner() throws Exception {
    rest
      .perform(
        put("/api/owners/" + UUID.randomUUID())
          .contentType(MediaType.APPLICATION_JSON)
          .content(ownerJson("Davis", "0612345678"))
      )
      .andExpect(status().isNotFound())
      .andExpect(jsonPath("$.title").value("Unknown element"));
  }

  @Test
  void shouldNotGetUnknownOwner() throws Exception {
    rest.perform(get("/api/owners/" + UUID.randomUUID())).andExpect(status().isNotFound());
  }

  @Test
  void shouldSearchOwnersByLastNameStartIgnoringCaseWithTheirPets() throws Exception {
    String prefix = uniqueLastName();
    String owner = createdOwner(prefix + "son");
    createdOwner(prefix + "berg");
    createdOwner("X" + prefix);
    addPet(owner, "Leo", "cat");

    rest
      .perform(get("/api/owners").with(anonymous()).param("lastName", prefix.toLowerCase()))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.totalElementsCount").value(2))
      .andExpect(jsonPath("$.currentPage").value(0))
      .andExpect(jsonPath("$.pageCount").value(1))
      .andExpect(jsonPath("$.owners[0].lastName").value(prefix + "berg"))
      .andExpect(jsonPath("$.owners[1].lastName").value(prefix + "son"))
      .andExpect(jsonPath("$.owners[1].pets[0].name").value("Leo"));
  }

  @Test
  void shouldPaginateOwnersSearchByFive() throws Exception {
    String prefix = uniqueLastName();
    for (int i = 0; i < 6; i++) {
      createdOwner(prefix + i);
    }

    rest
      .perform(get("/api/owners").param("lastName", prefix).param("page", "1"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.totalElementsCount").value(6))
      .andExpect(jsonPath("$.currentPage").value(1))
      .andExpect(jsonPath("$.pageCount").value(2))
      .andExpect(jsonPath("$.owners", hasSize(1)))
      .andExpect(jsonPath("$.owners[0].lastName").value(prefix + 5));
  }

  @Test
  void shouldSearchAllOwnersWithoutLastName() throws Exception {
    createdOwner(uniqueLastName());

    rest
      .perform(get("/api/owners"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.owners", hasSize(lessThanOrEqualTo(5))));
  }

  @Test
  void shouldNotSearchNegativePage() throws Exception {
    rest.perform(get("/api/owners").param("page", "-1")).andExpect(status().isBadRequest());
  }

  @Test
  void shouldAddPetAndShowItInOwnerDetails() throws Exception {
    String owner = createdOwner(uniqueLastName());

    addPet(owner, "Leo", "cat")
      .andExpect(status().isCreated())
      .andExpect(jsonPath("$.id").isNotEmpty())
      .andExpect(jsonPath("$.name").value("Leo"))
      .andExpect(jsonPath("$.birthDate").value("2020-09-07"))
      .andExpect(jsonPath("$.type").value("cat"))
      .andExpect(jsonPath("$.visits").isEmpty());

    rest.perform(get("/api/owners/" + owner)).andExpect(jsonPath("$.pets[0].name").value("Leo"));
  }

  @Test
  void shouldNotAddPetWithSameNameIgnoringCase() throws Exception {
    String owner = createdOwner(uniqueLastName());
    addPet(owner, "Rex", "dog");

    addPet(owner, "rex", "dog").andExpect(status().isConflict()).andExpect(jsonPath("$.title").value("Business rule violation"));
  }

  @Test
  void shouldNotAddPetBornInTheFuture() throws Exception {
    String owner = createdOwner(uniqueLastName());

    rest
      .perform(
        post("/api/owners/" + owner + "/pets")
          .contentType(MediaType.APPLICATION_JSON)
          .content(petJson("Leo", TODAY.plusDays(30), "cat"))
      )
      .andExpect(status().isBadRequest());
  }

  @Test
  void shouldNotAddPetWithoutType() throws Exception {
    String owner = createdOwner(uniqueLastName());

    rest
      .perform(
        post("/api/owners/" + owner + "/pets")
          .contentType(MediaType.APPLICATION_JSON)
          .content("{\"name\":\"Leo\",\"birthDate\":\"2020-09-07\"}")
      )
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.field").value("type"));
  }

  @Test
  void shouldNotAddPetWithUnknownType() throws Exception {
    String owner = createdOwner(uniqueLastName());

    addPet(owner, "Leo", "dragon").andExpect(status().isBadRequest());
  }

  @Test
  void shouldNotAddPetToUnknownOwner() throws Exception {
    addPet(UUID.randomUUID().toString(), "Leo", "cat").andExpect(status().isNotFound());
  }

  @Test
  void shouldUpdatePet() throws Exception {
    String owner = createdOwner(uniqueLastName());
    String pet = createdPet(owner, "Leo");

    rest
      .perform(
        put("/api/owners/" + owner + "/pets/" + pet)
          .contentType(MediaType.APPLICATION_JSON)
          .content(petJson("Tweety", LocalDate.of(2024, 5, 5), "bird"))
      )
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id").value(pet))
      .andExpect(jsonPath("$.name").value("Tweety"))
      .andExpect(jsonPath("$.type").value("bird"));
  }

  @Test
  void shouldNotRenamePetToAnotherPetName() throws Exception {
    String owner = createdOwner(uniqueLastName());
    String pet = createdPet(owner, "Leo");
    createdPet(owner, "Basil");

    rest
      .perform(
        put("/api/owners/" + owner + "/pets/" + pet)
          .contentType(MediaType.APPLICATION_JSON)
          .content(petJson("BASIL", LocalDate.of(2020, 9, 7), "cat"))
      )
      .andExpect(status().isConflict());
  }

  @Test
  void shouldNotUpdatePetOfAnotherOwner() throws Exception {
    String owner = createdOwner(uniqueLastName());
    String otherOwnerPet = createdPet(createdOwner(uniqueLastName()), "Leo");

    rest
      .perform(
        put("/api/owners/" + owner + "/pets/" + otherOwnerPet)
          .contentType(MediaType.APPLICATION_JSON)
          .content(petJson("Leo", LocalDate.of(2020, 9, 7), "cat"))
      )
      .andExpect(status().isNotFound());
  }

  @Test
  void shouldScheduleVisitAndShowItSortedByDate() throws Exception {
    String owner = createdOwner(uniqueLastName());
    String pet = createdPet(owner, "Leo");

    scheduleVisit(owner, pet, TODAY.plusDays(30))
      .andExpect(status().isCreated())
      .andExpect(jsonPath("$.id").isNotEmpty())
      .andExpect(jsonPath("$.date").value(TODAY.plusDays(30).toString()))
      .andExpect(jsonPath("$.description").value("Check-up"));
    scheduleVisit(owner, pet, TODAY.plusDays(10));

    rest
      .perform(get("/api/owners/" + owner))
      .andExpect(jsonPath("$.pets[0].visits[0].date").value(TODAY.plusDays(10).toString()))
      .andExpect(jsonPath("$.pets[0].visits[1].date").value(TODAY.plusDays(30).toString()));
  }

  @Test
  void shouldNotScheduleVisitInThePast() throws Exception {
    String owner = createdOwner(uniqueLastName());
    String pet = createdPet(owner, "Leo");

    scheduleVisit(owner, pet, TODAY.minusDays(30)).andExpect(status().isBadRequest());
  }

  @Test
  void shouldNotScheduleVisitForPetOfAnotherOwner() throws Exception {
    String owner = createdOwner(uniqueLastName());
    String otherOwnerPet = createdPet(createdOwner(uniqueLastName()), "Leo");

    scheduleVisit(owner, otherOwnerPet, TODAY.plusDays(30)).andExpect(status().isNotFound());
  }

  @Test
  void shouldNotScheduleVisitWithoutDescription() throws Exception {
    String owner = createdOwner(uniqueLastName());
    String pet = createdPet(owner, "Leo");

    rest
      .perform(
        post("/api/owners/" + owner + "/pets/" + pet + "/visits")
          .contentType(MediaType.APPLICATION_JSON)
          .content("{\"date\":\"" + TODAY.plusDays(30) + "\"}")
      )
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.field").value("description"));
  }

  private ResultActions register(String lastName) throws Exception {
    return rest.perform(
      post("/api/owners").with(anonymous()).contentType(MediaType.APPLICATION_JSON).content(ownerJson(lastName, "6085551023"))
    );
  }

  private String createdOwner(String lastName) throws Exception {
    return id(register(lastName));
  }

  private ResultActions addPet(String owner, String name, String type) throws Exception {
    return rest.perform(
      post("/api/owners/" + owner + "/pets")
        .contentType(MediaType.APPLICATION_JSON)
        .content(petJson(name, LocalDate.of(2020, 9, 7), type))
    );
  }

  private String createdPet(String owner, String name) throws Exception {
    return id(addPet(owner, name, "cat"));
  }

  private ResultActions scheduleVisit(String owner, String pet, LocalDate date) throws Exception {
    return rest.perform(
      post("/api/owners/" + owner + "/pets/" + pet + "/visits")
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"date\":\"" + date + "\",\"description\":\"Check-up\"}")
    );
  }

  private static String id(ResultActions result) throws Exception {
    return JsonPath.read(result.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8), "$.id");
  }

  private static String uniqueLastName() {
    return "Ln" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
  }

  private static String ownerJson(String lastName, String telephone) {
    return (
      "{\"firstName\":\"George\",\"lastName\":\""
      + lastName
      + "\",\"address\":\"110 W. Liberty St.\",\"city\":\"Madison\",\"telephone\":\""
      + telephone
      + "\"}"
    );
  }

  private static String petJson(String name, LocalDate birthDate, String type) {
    return "{\"name\":\"" + name + "\",\"birthDate\":\"" + birthDate + "\",\"type\":\"" + type + "\"}";
  }
}
