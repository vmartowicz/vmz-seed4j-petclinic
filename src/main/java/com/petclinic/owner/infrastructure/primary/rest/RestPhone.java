package com.petclinic.owner.infrastructure.primary.rest;

import com.petclinic.owner.domain.recipient.Phone;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "Phone", description = "Phone number of the owner")
final class RestPhone {

  private final String value;

  public RestPhone(String value) {
    this.value = value;
  }

  public static RestPhone from(Phone phone) {
    return new RestPhone(phone.value());
  }

  @Schema(name = "value", description = "Phone number", example = "+1234567890", requiredMode = Schema.RequiredMode.REQUIRED)
  public String getValue() {
    return value;
  }
}
