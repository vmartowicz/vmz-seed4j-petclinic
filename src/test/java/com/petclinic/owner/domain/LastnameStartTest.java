package com.petclinic.owner.domain;

import static org.assertj.core.api.Assertions.*;

import com.petclinic.UnitTest;
import org.junit.jupiter.api.Test;

@UnitTest
class LastnameStartTest {

  @Test
  void shouldMatchAnyLastnameWithoutValue() {
    assertThat(new LastnameStart(null).get()).isEmpty();
  }

  @Test
  void shouldStripLastnameStart() {
    assertThat(new LastnameStart("  Fra ").get()).isEqualTo("Fra");
  }
}
