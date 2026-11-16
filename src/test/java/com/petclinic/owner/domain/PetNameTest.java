package com.petclinic.owner.domain;

import static org.assertj.core.api.Assertions.*;

import com.petclinic.UnitTest;
import com.petclinic.shared.error.domain.MissingMandatoryValueException;
import com.petclinic.shared.error.domain.StringTooLongException;
import org.junit.jupiter.api.Test;

@UnitTest
class PetNameTest {

  @Test
  void shouldNotBuildWithoutValue() {
    assertThatThrownBy(() -> new PetName(null))
      .isExactlyInstanceOf(MissingMandatoryValueException.class)
      .hasMessageContaining("name");
  }

  @Test
  void shouldNotBuildBlank() {
    assertThatThrownBy(() -> new PetName(" "))
      .isExactlyInstanceOf(MissingMandatoryValueException.class)
      .hasMessageContaining("name");
  }

  @Test
  void shouldBuildWith30Characters() {
    assertThat(new PetName("a".repeat(30)).get()).hasSize(30);
  }

  @Test
  void shouldNotBuildWith31Characters() {
    assertThatThrownBy(() -> new PetName("a".repeat(31)))
      .isExactlyInstanceOf(StringTooLongException.class)
      .hasMessageContaining("name");
  }

  @Test
  void shouldBeSameNameIgnoringCase() {
    assertThat(new PetName("Rex").sameAs(new PetName("rEX"))).isTrue();
  }

  @Test
  void shouldNotBeSameNameForDifferentName() {
    assertThat(new PetName("Rex").sameAs(new PetName("Rexy"))).isFalse();
  }
}
