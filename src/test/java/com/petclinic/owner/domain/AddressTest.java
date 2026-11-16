package com.petclinic.owner.domain;

import static org.assertj.core.api.Assertions.*;

import com.petclinic.UnitTest;
import com.petclinic.shared.error.domain.MissingMandatoryValueException;
import com.petclinic.shared.error.domain.StringTooLongException;
import org.junit.jupiter.api.Test;

@UnitTest
class AddressTest {

  @Test
  void shouldNotBuildWithoutValue() {
    assertThatThrownBy(() -> new Address(null))
      .isExactlyInstanceOf(MissingMandatoryValueException.class)
      .hasMessageContaining("address");
  }

  @Test
  void shouldNotBuildBlank() {
    assertThatThrownBy(() -> new Address(" "))
      .isExactlyInstanceOf(MissingMandatoryValueException.class)
      .hasMessageContaining("address");
  }

  @Test
  void shouldBuildWith255Characters() {
    assertThat(new Address("a".repeat(255)).get()).hasSize(255);
  }

  @Test
  void shouldNotBuildWith256Characters() {
    assertThatThrownBy(() -> new Address("a".repeat(256)))
      .isExactlyInstanceOf(StringTooLongException.class)
      .hasMessageContaining("address");
  }
}
