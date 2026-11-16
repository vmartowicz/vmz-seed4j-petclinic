package com.petclinic.vet.infrastructure.primary;

import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.petclinic.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

@IntegrationTest
@AutoConfigureMockMvc
class VetsResourceIT {

  @Autowired
  private MockMvc rest;

  @Test
  void shouldListVetsWithSpecialtiesByPageOfFive() throws Exception {
    rest
      .perform(get("/api/vets").with(anonymous()))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.totalElementsCount").value(6))
      .andExpect(jsonPath("$.pageCount").value(2))
      .andExpect(jsonPath("$.currentPage").value(0))
      .andExpect(jsonPath("$.vets", hasSize(5)))
      .andExpect(jsonPath("$.vets[0].firstName").value("James"))
      .andExpect(jsonPath("$.vets[0].lastName").value("Carter"))
      .andExpect(jsonPath("$.vets[0].specialties").isEmpty())
      .andExpect(jsonPath("$.vets[1].lastName").value("Douglas"))
      .andExpect(jsonPath("$.vets[1].specialties[0]").value("surgery"))
      .andExpect(jsonPath("$.vets[1].specialties[1]").value("dentistry"));
  }

  @Test
  void shouldListSecondPageOfVets() throws Exception {
    rest
      .perform(get("/api/vets").param("page", "1"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.vets", hasSize(1)))
      .andExpect(jsonPath("$.vets[0].lastName").value("Stevens"));
  }

  @Test
  void shouldNotListNegativePage() throws Exception {
    rest.perform(get("/api/vets").param("page", "-1")).andExpect(status().isBadRequest());
  }
}
