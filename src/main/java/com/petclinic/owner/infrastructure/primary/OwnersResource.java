package com.petclinic.owner.infrastructure.primary;

import com.petclinic.owner.application.OwnersApplicationService;
import com.petclinic.owner.domain.LastnameStart;
import com.petclinic.owner.domain.OwnerId;
import com.petclinic.owner.domain.PetId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import org.jspecify.annotations.Nullable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Owners")
@RequestMapping("/api/owners")
class OwnersResource {

  private final OwnersApplicationService owners;

  public OwnersResource(OwnersApplicationService owners) {
    this.owners = owners;
  }

  @PostMapping
  @Operation(summary = "Register a new owner")
  ResponseEntity<RestOwner> register(@RequestBody RestOwnerToCreate owner) {
    return new ResponseEntity<>(RestOwner.from(owners.register(owner.toDomain())), HttpStatus.CREATED);
  }

  @PutMapping("/{owner-id}")
  @Operation(summary = "Update an owner")
  RestOwner update(@PathVariable("owner-id") UUID ownerId, @RequestBody RestOwnerToUpdate owner) {
    return RestOwner.from(owners.update(new OwnerId(ownerId), owner.toDomain()));
  }

  @GetMapping
  @Operation(summary = "Search owners by the beginning of their last name, 5 per page")
  RestOwnersPage search(
    @RequestParam(name = "lastName", required = false) @Nullable String lastName,
    @RequestParam(name = "page", defaultValue = "0") int page
  ) {
    return RestOwnersPage.from(owners.search(new LastnameStart(lastName), page));
  }

  @GetMapping("/{owner-id}")
  @Operation(summary = "Get an owner with their pets and visits")
  RestOwner get(@PathVariable("owner-id") UUID ownerId) {
    return RestOwner.from(owners.get(new OwnerId(ownerId)));
  }

  @PostMapping("/{owner-id}/pets")
  @Operation(summary = "Add a pet to an owner")
  ResponseEntity<RestPet> addPet(@PathVariable("owner-id") UUID ownerId, @RequestBody RestPetToCreate pet) {
    return new ResponseEntity<>(RestPet.from(owners.addPet(new OwnerId(ownerId), pet.toDomain())), HttpStatus.CREATED);
  }

  @PutMapping("/{owner-id}/pets/{pet-id}")
  @Operation(summary = "Update a pet of an owner")
  RestPet updatePet(@PathVariable("owner-id") UUID ownerId, @PathVariable("pet-id") UUID petId, @RequestBody RestPetToUpdate pet) {
    return RestPet.from(owners.updatePet(new OwnerId(ownerId), new PetId(petId), pet.toDomain()));
  }

  @PostMapping("/{owner-id}/pets/{pet-id}/visits")
  @Operation(summary = "Schedule a visit for a pet of an owner")
  ResponseEntity<RestVisit> scheduleVisit(
    @PathVariable("owner-id") UUID ownerId,
    @PathVariable("pet-id") UUID petId,
    @RequestBody RestVisitToCreate visit
  ) {
    return new ResponseEntity<>(
      RestVisit.from(owners.scheduleVisit(new OwnerId(ownerId), new PetId(petId), visit.toDomain())),
      HttpStatus.CREATED
    );
  }
}
