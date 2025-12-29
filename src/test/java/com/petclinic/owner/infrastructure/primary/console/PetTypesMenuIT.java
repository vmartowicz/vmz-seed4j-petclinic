package com.petclinic.owner.infrastructure.primary.console;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.petclinic.IntegrationTest;
import com.petclinic.owner.application.PetTypesApplicationService;
import com.petclinic.owner.domain.pettype.PetType;
import com.petclinic.owner.domain.pettype.PetTypeId;
import com.petclinic.owner.domain.pettype.PetTypeName;
import com.petclinic.owner.domain.pettype.PetTypeToCreate;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;

@IntegrationTest
@WithMockUser(value = "admin")
class PetTypesMenuIT {

  @Autowired
  private PetTypesApplicationService service;

  private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
  private final PrintStream originalOut = System.out;

  @BeforeEach
  void setUp() {
    System.setOut(new PrintStream(outputStream));
  }

  @AfterEach
  void tearDown() {
    System.setOut(originalOut);
  }

  @Test
  void shouldCreatePetType() {
    // Given
    String input = "1\nDog\n6\n"; // Choix 1 (Create), nom "Dog", puis 6 (Exit)
    System.setIn(new ByteArrayInputStream(input.getBytes()));
    PetTypesMenu menu = new PetTypesMenu(service);

    // When
    menu.run();

    // Then
    assertThat(outputStream.toString()).contains("PetType created.");
  }

  @Test
  void shouldNotCreatePetTypeWhenNameIsBlank() {
    // Given
    String input = "1\n\n6\n"; // Choix 1, nom vide (erreur), puis 6 (Exit)
    System.setIn(new ByteArrayInputStream(input.getBytes()));

    PetTypesMenu menu = new PetTypesMenu(service);

    // When
    menu.run();

    // Then
    assertThat(outputStream.toString()).contains("The field \"name\" is mandatory and wasn't set (blank)");
  }

  @Test
  void shouldFindPetType() {
    // Given
    PetType cat = createPetType("Cat");

    PetType petType = PetType.builder().id(cat.id()).name(new PetTypeName("Cat")).build();
    CliPetType expectedPetType = CliPetType.from(petType);

    String input = "2\n" + cat.id().id() + "\n6\n"; // Choix 2 (Find), UUID, puis 6 (Exit)
    System.setIn(new ByteArrayInputStream(input.getBytes()));
    PetTypesMenu menu = new PetTypesMenu(service);

    // When
    menu.run();

    // Then
    assertThat(outputStream.toString()).contains(expectedPetType.toString());
  }

  @Test
  void shouldNotFindPetType() {
    // Given
    UUID petTypeId = UUID.randomUUID();

    String input = "2\n" + petTypeId + "\n6\n";
    System.setIn(new ByteArrayInputStream(input.getBytes()));
    String expectedPetType = "Warning : This Pet Type {" + petTypeId + "} doesn't exist !";

    PetTypesMenu menu = new PetTypesMenu(service);

    // When
    menu.run();

    // Then
    assertThat(outputStream.toString()).contains(expectedPetType);
  }

  @Test
  void shouldNotFindPetTypeWithInvalidUUID() {
    // Given
    String input = "2\ninvalid-uuid\n6\n"; // Choix 2, UUID invalide, puis 5 (Exit)
    System.setIn(new ByteArrayInputStream(input.getBytes()));

    PetTypesMenu menu = new PetTypesMenu(service);

    // When
    menu.run();

    // Then
    assertThat(outputStream.toString()).contains("Error : Cannot find this Pet Type {invalid-uuid}!!!");
  }

  @Test
  void shouldFindAllPetType() {
    // Given
    // First findAll
    final int firstSize = findAllPetTypes();

    // Create an object
    PetType cat = createPetType("Cat");

    // Second findAll
    final int secondSize = findAllPetTypes();

    PetType petType = PetType.builder().id(cat.id()).name(new PetTypeName("Cat")).build();
    CliPetType expectedPetType = CliPetType.from(petType);

    assertThat(secondSize).isEqualTo(firstSize + 1);

    String input = "3\n6\n"; // Choix 3 (FindAll), puis 6 (Exit)
    System.setIn(new ByteArrayInputStream(input.getBytes()));
    PetTypesMenu menu = new PetTypesMenu(service);

    // When
    menu.run();

    // Then
    assertThat(outputStream.toString()).contains(expectedPetType.toString());
  }

  @Test
  void shouldUpdatePetType() throws Exception {
    // Given
    PetType bird = createPetType("Bird");
    PetType petType = PetType.builder().id(bird.id()).name(new PetTypeName("Dog")).build();
    CliPetType expectedPetType = CliPetType.from(petType);

    String input = "4\n" + bird.id().id() + "\nDog\n6\n";
    System.setIn(new ByteArrayInputStream(input.getBytes()));

    PetTypesMenu menu = new PetTypesMenu(service);

    // When
    menu.run();

    // Then
    assertThat(outputStream.toString()).contains(expectedPetType.toString());
  }

  @Test
  void shouldNotUpdatePetTypeWhenNameIsBlank() throws Exception {
    // Given
    PetType bird = createPetType("Bird");

    String input = "4\n" + bird.id().id() + "\n\n6\n";
    System.setIn(new ByteArrayInputStream(input.getBytes()));

    PetTypesMenu menu = new PetTypesMenu(service);

    // When
    menu.run();

    // Then
    assertThat(outputStream.toString()).contains("The field \"name\" is mandatory and wasn't set (blank)");
  }

  @Test
  void shouldNotUpdatePetTypeWhenIdIsIvalid() throws Exception {
    // Given
    String input = "4\ne7e0199a-122e-4629-b6b0-af29fae38505\n\n6\n";
    System.setIn(new ByteArrayInputStream(input.getBytes()));

    PetTypesMenu menu = new PetTypesMenu(service);

    // When
    menu.run();

    // Then
    assertThat(outputStream.toString()).contains("Warning : This Pet Type {e7e0199a-122e-4629-b6b0-af29fae38505} doesn't exist !");
  }

  @Test
  void shouldDeletePetType() {
    // Given
    // Create an object
    PetType fish = createPetType("Fish");

    String input = "5\n" + fish.id().id() + "\n6\n"; // Choix 5 (Delete), UUID, puis 6 (Exit)
    System.setIn(new ByteArrayInputStream(input.getBytes()));

    PetTypesMenu menu = new PetTypesMenu(service);

    // When
    menu.run();

    // Then
    assertThat(outputStream.toString()).contains("PetType {" + fish.id().id() + "} deleted.");
  }

  @Test
  void shouldNotDeletePetTypeWhenIdNotFound() {
    // Given
    String input = "5\ne7e0199a-122e-4629-b6b0-af29fae38505\n6\n"; // Choix 5 (Delete), UUID, puis 6 (Exit)
    System.setIn(new ByteArrayInputStream(input.getBytes()));

    PetTypesMenu menu = new PetTypesMenu(service);

    // When
    menu.run();

    // Then
    assertThat(outputStream.toString()).contains("Warning : This Pet Type {e7e0199a-122e-4629-b6b0-af29fae38505} doesn't exist !");
  }

  @Test
  void shouldExit() {
    // Given
    String input = "6\n"; // Exit immédiatement
    System.setIn(new ByteArrayInputStream(input.getBytes()));

    PetTypesMenu menu = new PetTypesMenu(service);

    // When
    menu.run();

    // Then
    assertThat(outputStream.toString()).contains("==== PetStore PetType CLI ====");
  }

  @Test
  void shouldGetInvalidChoice() {
    // Given
    String input = "999\n6\n"; // Choix invalide, puis 5 (Exit)
    System.setIn(new ByteArrayInputStream(input.getBytes()));

    PetTypesMenu menu = new PetTypesMenu(service);

    // When
    menu.run();

    // Then
    assertThat(outputStream.toString()).contains("Invalid choice.");
  }

  //==================================
  //=         Private Methods        =
  //==================================

  private Optional<PetType> findPetType(final UUID id) {
    return service.find(new PetTypeId(id));
  }

  private int findAllPetTypes() {
    return service.findAllPetTypes().get().size();
  }

  private PetType createPetType(String name) {
    return service.create(new PetTypeToCreate(new PetTypeName(name)));
  }
}
