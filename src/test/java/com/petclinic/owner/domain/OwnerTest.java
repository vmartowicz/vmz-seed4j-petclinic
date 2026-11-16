package com.petclinic.owner.domain;

import static com.petclinic.owner.domain.OwnersFixture.*;
import static org.assertj.core.api.Assertions.*;

import com.petclinic.UnitTest;
import java.time.LocalDate;
import java.util.UUID;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@UnitTest
class OwnerTest {

  @Test
  void shouldCreateOwnerWithoutPets() {
    Owner owner = ownerToCreate().create();

    assertThat(owner.id()).isNotNull();
    assertThat(owner.firstname()).isEqualTo(new Firstname("Betty"));
    assertThat(owner.lastname()).isEqualTo(new Lastname("Davis"));
    assertThat(owner.address()).isEqualTo(new Address("638 Cardinal Ave."));
    assertThat(owner.city()).isEqualTo(new City("Sun Prairie"));
    assertThat(owner.telephone()).isEqualTo(new Telephone("6085551749"));
    assertThat(owner.pets()).isEmpty();
  }

  @Test
  void shouldUpdateOwner() {
    Owner owner = george();

    owner.update(ownerToUpdate());

    assertThat(owner.id()).isEqualTo(georgeId());
    assertThat(owner.firstname()).isEqualTo(new Firstname("Georges"));
    assertThat(owner.lastname()).isEqualTo(new Lastname("Franklyn"));
    assertThat(owner.address()).isEqualTo(new Address("111 W. Liberty St."));
    assertThat(owner.city()).isEqualTo(new City("Monona"));
    assertThat(owner.telephone()).isEqualTo(new Telephone("6085551024"));
    assertThat(owner.pets()).hasSize(2);
  }

  @Test
  void shouldSortPetsByNameIgnoringCase() {
    Owner owner = george();

    owner.addPet(petToCreate("Max", TODAY), TODAY);

    assertThat(owner.pets())
      .extracting(pet -> pet.name().get())
      .containsExactly("bella", "Leo", "Max");
  }

  @Nested
  class AddPet {

    @Test
    void shouldAddPet() {
      Owner owner = george();

      Pet pet = owner.addPet(petToCreate("Basil", LocalDate.of(2025, 2, 1)), TODAY);

      assertThat(pet.id()).isNotNull();
      assertThat(pet.name()).isEqualTo(new PetName("Basil"));
      assertThat(pet.birthDate()).isEqualTo(LocalDate.of(2025, 2, 1));
      assertThat(pet.type()).isEqualTo(PetType.HAMSTER);
      assertThat(pet.visits()).isEmpty();
      assertThat(owner.pets()).contains(pet);
    }

    @Test
    void shouldAddPetBornToday() {
      Owner owner = george();

      Pet pet = owner.addPet(petToCreate("Basil", TODAY), TODAY);

      assertThat(pet.birthDate()).isEqualTo(TODAY);
    }

    @Test
    void shouldNotAddPetBornTomorrow() {
      Owner owner = george();

      assertThatThrownBy(() -> owner.addPet(petToCreate("Basil", TODAY.plusDays(1)), TODAY))
        .isExactlyInstanceOf(BirthDateInFutureException.class)
        .hasMessage("Birth date 2026-11-16 can't be in the future");
      assertThat(owner.pets()).hasSize(2);
    }

    @Test
    void shouldNotAddPetWithSameNameIgnoringCase() {
      Owner owner = george();

      assertThatThrownBy(() -> owner.addPet(petToCreate("LEO", TODAY), TODAY))
        .isExactlyInstanceOf(DuplicatedPetNameException.class)
        .hasMessage("The owner already has a pet named \"LEO\"");
      assertThat(owner.pets()).hasSize(2);
    }
  }

  @Nested
  class UpdatePet {

    @Test
    void shouldUpdatePet() {
      Owner owner = george();

      Pet pet = owner.updatePet(leoId(), petToUpdate("Tweety", LocalDate.of(2024, 5, 5)), TODAY);

      assertThat(pet.id()).isEqualTo(leoId());
      assertThat(pet.name()).isEqualTo(new PetName("Tweety"));
      assertThat(pet.birthDate()).isEqualTo(LocalDate.of(2024, 5, 5));
      assertThat(pet.type()).isEqualTo(PetType.BIRD);
      assertThat(pet.visits()).containsExactly(rabiesShot());
    }

    @Test
    void shouldUpdatePetKeepingItsName() {
      Owner owner = george();

      Pet pet = owner.updatePet(leoId(), petToUpdate("Leo", TODAY), TODAY);

      assertThat(pet.name()).isEqualTo(new PetName("Leo"));
      assertThat(pet.birthDate()).isEqualTo(TODAY);
    }

    @Test
    void shouldUpdatePetChangingItsNameCase() {
      Owner owner = george();

      Pet pet = owner.updatePet(leoId(), petToUpdate("LEO", TODAY), TODAY);

      assertThat(pet.name()).isEqualTo(new PetName("LEO"));
    }

    @Test
    void shouldNotRenamePetToAnotherPetName() {
      Owner owner = george();

      assertThatThrownBy(() -> owner.updatePet(leoId(), petToUpdate("Bella", TODAY), TODAY)).isExactlyInstanceOf(
        DuplicatedPetNameException.class
      );
      assertThat(owner.pets())
        .extracting(pet -> pet.name().get())
        .containsExactly("bella", "Leo");
    }

    @Test
    void shouldNotUpdatePetWithBirthDateTomorrow() {
      Owner owner = george();

      assertThatThrownBy(() -> owner.updatePet(leoId(), petToUpdate("Tweety", TODAY.plusDays(1)), TODAY)).isExactlyInstanceOf(
        BirthDateInFutureException.class
      );
      assertThat(owner.pets())
        .extracting(pet -> pet.name().get())
        .containsExactly("bella", "Leo");
    }

    @Test
    void shouldNotUpdateUnknownPet() {
      Owner owner = george();
      PetId unknown = new PetId(UUID.fromString("00000000-0000-0000-0000-000000000001"));

      assertThatThrownBy(() -> owner.updatePet(unknown, petToUpdate("Tweety", TODAY), TODAY))
        .isExactlyInstanceOf(UnknownPetException.class)
        .hasMessage("Pet 00000000-0000-0000-0000-000000000001 is unknown for owner " + georgeId().get());
    }
  }

  @Nested
  class ScheduleVisit {

    @Test
    void shouldScheduleVisitTomorrow() {
      Owner owner = george();

      Visit visit = owner.scheduleVisit(bellaId(), visitToCreate(TODAY.plusDays(1)), TODAY);

      assertThat(visit.id()).isNotNull();
      assertThat(visit.date()).isEqualTo(TODAY.plusDays(1));
      assertThat(visit.description()).isEqualTo(new VisitDescription("Check-up"));
      assertThat(owner.pets().getFirst().visits()).containsExactly(visit);
    }

    @Test
    void shouldNotScheduleVisitToday() {
      Owner owner = george();

      assertThatThrownBy(() -> owner.scheduleVisit(bellaId(), visitToCreate(TODAY), TODAY))
        .isExactlyInstanceOf(VisitNotInFutureException.class)
        .hasMessage("Visit date 2026-11-15 must be tomorrow at the earliest");
      assertThat(owner.pets().getFirst().visits()).isEmpty();
    }

    @Test
    void shouldNotScheduleVisitForUnknownPet() {
      Owner owner = george();
      PetId unknown = new PetId(UUID.fromString("00000000-0000-0000-0000-000000000001"));

      assertThatThrownBy(() -> owner.scheduleVisit(unknown, visitToCreate(TODAY.plusDays(1)), TODAY)).isExactlyInstanceOf(
        UnknownPetException.class
      );
    }

    @Test
    void shouldSortVisitsByDate() {
      Owner owner = george();

      Visit later = owner.scheduleVisit(leoId(), visitToCreate(TODAY.plusDays(10)), TODAY);
      Visit sooner = owner.scheduleVisit(leoId(), visitToCreate(TODAY.plusDays(2)), TODAY);

      assertThat(owner.pets().get(1).visits()).containsExactly(rabiesShot(), sooner, later);
    }
  }

  @Test
  void shouldHaveUnknownOwnerMessage() {
    assertThat(new UnknownOwnerException(georgeId())).hasMessage("Owner " + georgeId().get() + " is unknown");
  }
}
