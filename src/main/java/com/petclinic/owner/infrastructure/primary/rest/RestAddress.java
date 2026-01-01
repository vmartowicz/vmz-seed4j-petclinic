package com.petclinic.owner.infrastructure.primary.rest;

import com.petclinic.owner.domain.recipient.Address;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "Address", description = "Address of the owner")
final class RestAddress {

  private final String zipCode;
  private final String street;
  private final String city;

  private RestAddress(RestAddressBuilder builder) {
    street = builder.street;
    city = builder.city;
    zipCode = builder.zipCode;
  }

  @Schema(name = "zipCode", description = "Zip code of the address", example = "12345", requiredMode = Schema.RequiredMode.REQUIRED)
  public String getZipCode() {
    return zipCode;
  }

  @Schema(name = "street", description = "Street of the address", example = "Main St", requiredMode = Schema.RequiredMode.REQUIRED)
  public String getStreet() {
    return street;
  }

  @Schema(name = "city", description = "City of the address", example = "Springfield", requiredMode = Schema.RequiredMode.REQUIRED)
  public String getCity() {
    return city;
  }

  public static RestAddress from(Address address) {
    return new RestAddressBuilder()
      .street(address.street().value())
      .city(address.city().value())
      .zipCode(address.zipCode().value())
      .build();
  }

  private static final class RestAddressBuilder {

    private String street;
    private String city;
    private String zipCode;

    public RestAddressBuilder street(String street) {
      this.street = street;
      return this;
    }

    public RestAddressBuilder city(String city) {
      this.city = city;
      return this;
    }

    public RestAddressBuilder zipCode(String zipCode) {
      this.zipCode = zipCode;
      return this;
    }

    public RestAddress build() {
      return new RestAddress(this);
    }
  }
}
