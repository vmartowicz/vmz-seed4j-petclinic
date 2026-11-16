package com.petclinic.owner.domain;

import static org.assertj.core.api.Assertions.*;

import com.petclinic.UnitTest;
import com.petclinic.shared.error.domain.MissingMandatoryValueException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@UnitTest
class TelephoneTest {

  @Test
  void shouldNotBuildWithoutTelephone() {
    assertThatThrownBy(() -> new Telephone(" "))
      .isExactlyInstanceOf(MissingMandatoryValueException.class)
      .hasMessageContaining("telephone");
  }

  @Test
  void shouldBuildWithTenDigits() {
    assertThat(new Telephone("0612345678").get()).isEqualTo("0612345678");
  }

  @ParameterizedTest
  @ValueSource(strings = { "061234567", "06123456789", "06 1234567", "06-2345678", "061234567a", "+336123456" })
  void shouldNotBuildWithoutExactlyTenDigits(String telephone) {
    assertThatThrownBy(() -> new Telephone(telephone))
      .isExactlyInstanceOf(InvalidTelephoneException.class)
      .hasMessage("Telephone \"" + telephone + "\" must have exactly 10 digits");
  }
}
