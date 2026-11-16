package com.petclinic.owner.domain;

import static org.assertj.core.api.Assertions.*;

import com.petclinic.UnitTest;
import com.petclinic.shared.error.domain.MissingMandatoryValueException;
import com.petclinic.shared.error.domain.StringTooLongException;
import org.junit.jupiter.api.Test;

@UnitTest
class FirstnameTest {

  @Test
  void shouldNotBuildWithoutValue() {
    assertThatThrownBy(() -> new Firstname(null))
      .isExactlyInstanceOf(MissingMandatoryValueException.class)
      .hasMessageContaining("firstname");
  }

  @Test
  void shouldNotBuildBlank() {
    assertThatThrownBy(() -> new Firstname(" "))
      .isExactlyInstanceOf(MissingMandatoryValueException.class)
      .hasMessageContaining("firstname");
  }

  @Test
  void shouldBuildWith30Characters() {
    assertThat(new Firstname("a".repeat(30)).get()).hasSize(30);
  }

  @Test
  void shouldNotBuildWith31Characters() {
    assertThatThrownBy(() -> new Firstname("a".repeat(31)))
      .isExactlyInstanceOf(StringTooLongException.class)
      .hasMessageContaining("firstname");
  }
}
