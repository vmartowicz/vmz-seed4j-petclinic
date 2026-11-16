package com.petclinic.owner.domain;

import static org.assertj.core.api.Assertions.*;

import com.petclinic.UnitTest;
import com.petclinic.shared.error.domain.MissingMandatoryValueException;
import com.petclinic.shared.error.domain.StringTooLongException;
import org.junit.jupiter.api.Test;

@UnitTest
class CityTest {

  @Test
  void shouldNotBuildWithoutValue() {
    assertThatThrownBy(() -> new City(null))
      .isExactlyInstanceOf(MissingMandatoryValueException.class)
      .hasMessageContaining("city");
  }

  @Test
  void shouldNotBuildBlank() {
    assertThatThrownBy(() -> new City(" "))
      .isExactlyInstanceOf(MissingMandatoryValueException.class)
      .hasMessageContaining("city");
  }

  @Test
  void shouldBuildWith80Characters() {
    assertThat(new City("a".repeat(80)).get()).hasSize(80);
  }

  @Test
  void shouldNotBuildWith81Characters() {
    assertThatThrownBy(() -> new City("a".repeat(81)))
      .isExactlyInstanceOf(StringTooLongException.class)
      .hasMessageContaining("city");
  }
}
