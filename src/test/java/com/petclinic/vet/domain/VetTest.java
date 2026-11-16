package com.petclinic.vet.domain;

import static org.assertj.core.api.Assertions.*;

import com.petclinic.UnitTest;
import com.petclinic.shared.error.domain.StringTooLongException;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

@UnitTest
class VetTest {

  @Test
  void shouldBuildVetWithSortedSpecialties() {
    Vet vet = Vet.builder()
      .id(new VetId(UUID.fromString("00000000-0000-4000-8000-000000000003")))
      .firstname(new Firstname("Linda"))
      .lastname(new Lastname("Douglas"))
      .specialties(List.of(Specialty.DENTISTRY, Specialty.SURGERY))
      .build();

    assertThat(vet.firstname().get()).isEqualTo("Linda");
    assertThat(vet.lastname().get()).isEqualTo("Douglas");
    assertThat(vet.specialties()).containsExactly(Specialty.SURGERY, Specialty.DENTISTRY);
  }

  @Test
  void shouldBuildVetWithoutSpecialties() {
    Vet vet = Vet.builder()
      .id(new VetId(UUID.fromString("00000000-0000-4000-8000-000000000001")))
      .firstname(new Firstname("James"))
      .lastname(new Lastname("Carter"))
      .build();

    assertThat(vet.specialties()).isEmpty();
  }

  @Test
  void shouldNotBuildTooLongFirstname() {
    assertThatThrownBy(() -> new Firstname("a".repeat(31))).isExactlyInstanceOf(StringTooLongException.class);
  }

  @Test
  void shouldNotBuildTooLongLastname() {
    assertThatThrownBy(() -> new Lastname("a".repeat(31))).isExactlyInstanceOf(StringTooLongException.class);
  }
}
