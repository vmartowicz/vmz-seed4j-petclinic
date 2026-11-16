package com.petclinic.wire.frontend.infrastructure.primary;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.petclinic.IntegrationTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

@IntegrationTest
@AutoConfigureMockMvc
class FrontendAccessIT {

  @Autowired
  private MockMvc rest;

  @ParameterizedTest
  @ValueSource(
    strings = {
      "/home",
      "/owners",
      "/owners/new",
      "/owners/owner-id",
      "/owners/owner-id/edit",
      "/owners/owner-id/pets/new",
      "/owners/owner-id/pets/pet-id/edit",
      "/owners/owner-id/pets/pet-id/visits/new",
      "/vets",
    }
  )
  void shouldForwardFrontendRoutesAnonymously(String route) throws Exception {
    rest.perform(get(route).with(anonymous())).andExpect(status().isOk()).andExpect(forwardedUrl("/"));
  }

  @ParameterizedTest
  @ValueSource(strings = { "/", "/index.html", "/assets/index.js" })
  void shouldNotForbidFrontendResourcesAnonymously(String resource) throws Exception {
    int status = rest.perform(get(resource).with(anonymous())).andReturn().getResponse().getStatus();

    assertThat(status).isNotIn(401, 403);
  }
}
