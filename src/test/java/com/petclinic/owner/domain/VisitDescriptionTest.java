package com.petclinic.owner.domain;

import static org.assertj.core.api.Assertions.*;

import com.petclinic.UnitTest;
import com.petclinic.shared.error.domain.MissingMandatoryValueException;
import com.petclinic.shared.error.domain.StringTooLongException;
import org.junit.jupiter.api.Test;

@UnitTest
class VisitDescriptionTest {

  @Test
  void shouldNotBuildWithoutValue() {
    assertThatThrownBy(() -> new VisitDescription(null))
      .isExactlyInstanceOf(MissingMandatoryValueException.class)
      .hasMessageContaining("description");
  }

  @Test
  void shouldNotBuildBlank() {
    assertThatThrownBy(() -> new VisitDescription(" "))
      .isExactlyInstanceOf(MissingMandatoryValueException.class)
      .hasMessageContaining("description");
  }

  @Test
  void shouldBuildWith255Characters() {
    assertThat(new VisitDescription("a".repeat(255)).get()).hasSize(255);
  }

  @Test
  void shouldNotBuildWith256Characters() {
    assertThatThrownBy(() -> new VisitDescription("a".repeat(256)))
      .isExactlyInstanceOf(StringTooLongException.class)
      .hasMessageContaining("description");
  }
}
