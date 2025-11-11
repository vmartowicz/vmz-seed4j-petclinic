package com.petclinic.owner.infrastructure.primary.rest;

import com.petclinic.owner.domain.recipient.Recipient;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "Recipient", description = "Recipient of the owner")
final class RestRecipient {

  private final RestName name;
  private final RestAddress address;
  private final RestPhone phone;

  private RestRecipient(RestName name, RestAddress address, RestPhone phone) {
    this.name = name;
    this.address = address;
    this.phone = phone;
  }

  public static RestRecipient from(Recipient recipient) {
    return new RestRecipient(RestName.from(recipient.name()), RestAddress.from(recipient.address()), RestPhone.from(recipient.phone()));
  }

  @Schema(name = "name", description = "Name of the recipient", requiredMode = Schema.RequiredMode.REQUIRED)
  public RestPhone getPhone() {
    return phone;
  }

  @Schema(name = "address", description = "Address of the recipient", requiredMode = Schema.RequiredMode.REQUIRED)
  public RestAddress getAddress() {
    return address;
  }

  @Schema(name = "name", description = "Name of the recipient", requiredMode = Schema.RequiredMode.REQUIRED)
  public RestName getName() {
    return name;
  }
}
