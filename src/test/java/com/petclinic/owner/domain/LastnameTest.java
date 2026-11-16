package com.petclinic.owner.domain;

import static org.assertj.core.api.Assertions.*;

import com.petclinic.UnitTest;
import com.petclinic.shared.error.domain.MissingMandatoryValueException;
import com.petclinic.shared.error.domain.StringTooLongException;
import org.junit.jupiter.api.Test;

@UnitTest
class LastnameTest {

  @Test
  void shouldNotBuildWithoutValue() {
    assertThatThrownBy(() -> new Lastname(null))
      .isExactlyInstanceOf(MissingMandatoryValueException.class)
      .hasMessageContaining("lastname");
  }

  @Test
  void shouldNotBuildBlank() {
    assertThatThrownBy(() -> new Lastname(" "))
      .isExactlyInstanceOf(MissingMandatoryValueException.class)
      .hasMessageContaining("lastname");
  }

  @Test
  void shouldBuildWith30Characters() {
    assertThat(new Lastname("a".repeat(30)).get()).hasSize(30);
  }

  @Test
  void shouldNotBuildWith31Characters() {
    assertThatThrownBy(() -> new Lastname("a".repeat(31)))
      .isExactlyInstanceOf(StringTooLongException.class)
      .hasMessageContaining("lastname");
  }
}
