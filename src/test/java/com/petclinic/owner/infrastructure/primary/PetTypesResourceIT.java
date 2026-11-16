package com.petclinic.owner.infrastructure.primary;

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
class PetTypesResourceIT {

  @Autowired
  private MockMvc rest;

  @Test
  void shouldListPetTypesAnonymously() throws Exception {
    rest
      .perform(get("/api/pet-types").with(anonymous()))
      .andExpect(status().isOk())
      .andExpect(content().json("[\"cat\",\"dog\",\"lizard\",\"snake\",\"bird\",\"hamster\"]", true));
  }
}
