package com.petclinic.owner.infrastructure.primary;

import com.petclinic.owner.application.OwnersApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Pet types")
@RequestMapping("/api/pet-types")
class PetTypesResource {

  private final OwnersApplicationService owners;

  public PetTypesResource(OwnersApplicationService owners) {
    this.owners = owners;
  }

  @GetMapping
  @Operation(summary = "List the pet types")
  List<RestPetType> petTypes() {
    return owners.petTypes().stream().map(RestPetType::from).toList();
  }
}
