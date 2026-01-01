package com.petclinic.owner.infrastructure.primary.rest;

import com.petclinic.owner.domain.Owner;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

@Schema(description = "Owner")
final class RestOwner {

  private final UUID id;

  private final RestRecipient recipient;

  private RestOwner(RestOwnerBuilder builder) {
    id = builder.id;
    recipient = builder.recipient;
  }

  public static RestOwner from(Owner owner) {
    return new RestOwnerBuilder().id(owner.id().value()).recipient(RestRecipient.from(owner.recipient())).build();
  }

  @Schema(
    name = "id",
    description = "Owner ID",
    example = "123e4567-e89b-12d3-a456-426614174000",
    requiredMode = Schema.RequiredMode.REQUIRED
  )
  public UUID getId() {
    return id;
  }

  @Schema(name = "recipient", description = "Recipient of the owner", requiredMode = Schema.RequiredMode.REQUIRED)
  public RestRecipient getRecipient() {
    return recipient;
  }

  private static final class RestOwnerBuilder {

    private UUID id;
    private RestRecipient recipient;

    public RestOwnerBuilder id(UUID id) {
      this.id = id;
      return this;
    }

    public RestOwnerBuilder recipient(RestRecipient recipient) {
      this.recipient = recipient;
      return this;
    }

    public RestOwner build() {
      return new RestOwner(this);
    }
  }
}
