package com.petclinic.vet.infrastructure.primary;

import com.petclinic.vet.application.VetsApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Vets")
@RequestMapping("/api/vets")
class VetsResource {

  private final VetsApplicationService vets;

  public VetsResource(VetsApplicationService vets) {
    this.vets = vets;
  }

  @GetMapping
  @Operation(summary = "List the vets with their specialties, 5 per page")
  RestVetsPage list(@RequestParam(name = "page", defaultValue = "0") @Min(0) int page) {
    return RestVetsPage.from(vets.list(page));
  }
}
