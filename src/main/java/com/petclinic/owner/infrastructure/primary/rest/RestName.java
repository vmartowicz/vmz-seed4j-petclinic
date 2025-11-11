package com.petclinic.owner.infrastructure.primary.rest;

import com.petclinic.owner.domain.recipient.Name;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "Name", description = "A name of a recipient")
final class RestName {

  private final String firstname;
  private final String lastname;

  private RestName(String firstname, String lastname) {
    this.firstname = firstname;
    this.lastname = lastname;
  }

  public static RestName from(Name name) {
    return new RestName(name.firstname().value(), name.lastname().value());
  }

  @Schema(name = "firstname", description = "First name of the recipient", example = "John", requiredMode = Schema.RequiredMode.REQUIRED)
  public String getFirstname() {
    return firstname;
  }

  @Schema(name = "lastname", description = "Last name of the recipient", example = "Doe", requiredMode = Schema.RequiredMode.REQUIRED)
  public String getLastname() {
    return lastname;
  }
}
