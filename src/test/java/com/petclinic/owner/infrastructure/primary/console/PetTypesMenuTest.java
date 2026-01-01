package com.petclinic.owner.infrastructure.primary.console;

import static org.assertj.core.api.Assertions.assertThat;

import com.petclinic.owner.application.PetTypesApplicationService;
import com.petclinic.owner.domain.pettype.PetType;
import com.petclinic.owner.domain.pettype.PetTypeId;
import com.petclinic.owner.domain.pettype.PetTypeName;
import com.petclinic.owner.infrastructure.secondary.repository.inmemory.PetTypeInMemoryRepository;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class PetTypesMenuTest {

  private static final String EXISTING_PETTYPE_ID_1 = "716d28f5-fb62-4a7f-9ce0-360d1893aed5";
  private static final String EXISTING_PETTYPE_ID_2 = "716d28f5-fb62-4a7f-9ce0-360d1893aed6";
  private static final String NON_EXISTING_PETTYPE_ID = "716d28f5-fb62-4a7f-9ce0-360d1893aed7";

  private ConsoleMenu consoleMenu;
  private PetTypesApplicationService petTypeService;
  private PetTypeInMemoryRepository petRepository;
  private ByteArrayOutputStream outputStream;

  @BeforeEach
  void setUp() {
    // Créer les vraies implémentations
    petRepository = new PetTypeInMemoryRepository();
    petRepository.save(PetType.builder().id(new PetTypeId(UUID.fromString(EXISTING_PETTYPE_ID_1))).name(new PetTypeName("Dog")).build());
    petRepository.save(PetType.builder().id(new PetTypeId(UUID.fromString(EXISTING_PETTYPE_ID_2))).name(new PetTypeName("Cat")).build());

    petTypeService = new PetTypesApplicationService(petRepository);

    // Capturer la sortie console
    outputStream = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outputStream));
  }

  @Test
  @DisplayName("Doit sortir avec un choix invalide")
  void shouldExitWithInvalidChoice() throws Exception {
    // Given
    String input = "1\n99\n0\n0\n";
    System.setIn(new ByteArrayInputStream(input.getBytes()));
    consoleMenu = new ConsoleMenu(petTypeService);
    consoleMenu.run();

    // Then
    String output = outputStream.toString();
    assertThat(output).contains("❌ Choix invalide ! Veuillez réessayer!");
    assertThat(output).contains("👋 Merci d'avoir utilisé Pet Store !");
    assertThat(output).contains("À bientôt !");
  }

  @Test
  @DisplayName("Lister tous les types d'animaux")
  void shouldListAllPetTypes() throws Exception {
    // Given
    String input = "1\n1\n\n0\n0\n";
    System.setIn(new ByteArrayInputStream(input.getBytes()));
    consoleMenu = new ConsoleMenu(petTypeService);

    // When
    consoleMenu.run();

    // Then
    String output = outputStream.toString();
    assertThat(output).contains("LISTE DES TYPES D'ANIMAUX");
    assertThat(output).contains("Dog");
    assertThat(output).contains("👋 Merci d'avoir utilisé Pet Store !");
    assertThat(output).contains("À bientôt !");
  }

  @Test
  @DisplayName("Ne doit pas lister tous les types d'animaux")
  void shouldNotListPetTypes() throws Exception {
    // Given
    petRepository.remove(new PetTypeId(UUID.fromString(EXISTING_PETTYPE_ID_1)));
    petRepository.remove(new PetTypeId(UUID.fromString(EXISTING_PETTYPE_ID_2)));

    String input = "1\n1\n\n0\n0\n";
    System.setIn(new ByteArrayInputStream(input.getBytes()));
    consoleMenu = new ConsoleMenu(petTypeService);

    // When
    consoleMenu.run();

    // Then
    String output = outputStream.toString();
    assertThat(output).contains("LISTE DES TYPES D'ANIMAUX");
    assertThat(output).contains("Aucun type d'animal trouvé.");
    assertThat(output).contains("👋 Merci d'avoir utilisé Pet Store !");
    assertThat(output).contains("À bientôt !");
  }

  @Test
  @DisplayName("Récupérer un type d'animal par ID")
  void shouldFindPetTypeById() throws Exception {
    // Given
    String input = "1\n2\n" + EXISTING_PETTYPE_ID_1 + "\n\n0\n0\n";
    System.setIn(new ByteArrayInputStream(input.getBytes()));
    consoleMenu = new ConsoleMenu(petTypeService);

    // When
    consoleMenu.run();

    // Then
    String output = outputStream.toString();

    assertThat(output).contains("RECHERCHER UN TYPE ANIMAL");
    assertThat(output).contains("DÉTAILS DU TYPE D'ANIMAL");
    assertThat(output).contains("ID       : " + EXISTING_PETTYPE_ID_1);
    assertThat(output).contains("Nom      : Dog");
    assertThat(output).contains("👋 Merci d'avoir utilisé Pet Store !");
    assertThat(output).contains("À bientôt !");
  }

  @Test
  @DisplayName("Ne doit pas Récupérer un type d'animal par ID")
  void shouldNotFindPetTypeById() throws Exception {
    // Given
    String input = "1\n2\n" + NON_EXISTING_PETTYPE_ID + "\n\n0\n0\n";
    System.setIn(new ByteArrayInputStream(input.getBytes()));
    consoleMenu = new ConsoleMenu(petTypeService);

    // When
    consoleMenu.run();

    // Then
    String output = outputStream.toString();
    assertThat(output).contains("RECHERCHER UN TYPE ANIMAL");
    assertThat(output).contains("❌ Aucun type d'animal trouvé avec l'ID: " + NON_EXISTING_PETTYPE_ID);
    assertThat(output).contains("👋 Merci d'avoir utilisé Pet Store !");
    assertThat(output).contains("À bientôt !");
  }

  @ParameterizedTest
  @CsvSource({ "oui,716d28f5-fb62-4a7f-9ce0-360d1893aed5", "o,716d28f5-fb62-4a7f-9ce0-360d1893aed6" })
  @DisplayName("Supprimer un type d'animal valide")
  void shouldDeletePetType(String confirmation, String uuid) throws Exception {
    // Given
    String input = "1\n5\n" + uuid + "\n" + confirmation + "\n\n0\n0\n";
    System.setIn(new ByteArrayInputStream(input.getBytes()));
    consoleMenu = new ConsoleMenu(petTypeService);

    // When
    consoleMenu.run();

    // Then
    String output = outputStream.toString();

    assertThat(output).contains("SUPPRIMER UN TYPE D'ANIMAL");
    assertThat(output).contains("✅ Type d'animal supprimé avec succès !");
    assertThat(output).contains("👋 Merci d'avoir utilisé Pet Store !");
    assertThat(output).contains("À bientôt !");
  }

  @Test
  @DisplayName("Ne doit pas supprimer un type d'animal invalide")
  void shouldNotDeletePetTypeWithInvalidID() throws Exception {
    // Given
    String input = "1\n5\n" + NON_EXISTING_PETTYPE_ID + "\noui\n0\n0\n";
    System.setIn(new ByteArrayInputStream(input.getBytes()));
    consoleMenu = new ConsoleMenu(petTypeService);

    // When
    consoleMenu.run();

    // Then
    String output = outputStream.toString();

    assertThat(output).contains("SUPPRIMER UN TYPE D'ANIMAL");
    assertThat(output).contains("❌ Aucun type d'animal trouvé avec l'ID: " + NON_EXISTING_PETTYPE_ID);
    assertThat(output).contains("👋 Merci d'avoir utilisé Pet Store !");
    assertThat(output).contains("À bientôt !");
  }

  @Test
  @DisplayName("Ne doit pas supprimer un type d'animal invalide avec annulation de la confirmation")
  void shouldNotDeletePetTypeWithConfirmationCanceled() throws Exception {
    // Given
    String input = "1\n5\n" + EXISTING_PETTYPE_ID_1 + "\nnon\n\n0\n0\n";
    System.setIn(new ByteArrayInputStream(input.getBytes()));
    consoleMenu = new ConsoleMenu(petTypeService);

    // When
    consoleMenu.run();

    // Then
    String output = outputStream.toString();

    assertThat(output).contains("SUPPRIMER UN TYPE D'ANIMAL");
    assertThat(output).contains("❌ Suppression annulée.");
    assertThat(output).contains("👋 Merci d'avoir utilisé Pet Store !");
    assertThat(output).contains("À bientôt !");
  }

  @Test
  @DisplayName("Doit créer un type d'animal ")
  void shouldCreatePetType() throws Exception {
    // Given
    String input = "1\n3\nBird\n\n0\n0\n";
    System.setIn(new ByteArrayInputStream(input.getBytes()));
    consoleMenu = new ConsoleMenu(petTypeService);

    // When
    consoleMenu.run();

    // Then
    String output = outputStream.toString();

    assertThat(output).contains("CRÉER UN NOUVEAU TYPE D'ANIMAL");
    assertThat(output).contains("✅ Type d'animal créé avec succès !");
    assertThat(output).contains("👋 Merci d'avoir utilisé Pet Store !");
    assertThat(output).contains("À bientôt !");
  }

  @Test
  @DisplayName("Ne doit pas créer un type d'animal ")
  void shouldNotCreatePetType() throws Exception {
    // Given
    String input = "1\n3\n\n\n0\n0\n";
    System.setIn(new ByteArrayInputStream(input.getBytes()));
    consoleMenu = new ConsoleMenu(petTypeService);

    // When
    consoleMenu.run();

    // Then
    String output = outputStream.toString();

    assertThat(output).contains("CRÉER UN NOUVEAU TYPE D'ANIMAL");
    assertThat(output).contains("Erreur : Impossible de créer ce type d'animal !!!");
    assertThat(output).contains("👋 Merci d'avoir utilisé Pet Store !");
    assertThat(output).contains("À bientôt !");
  }

  @Test
  @DisplayName("Doit mettre à jour un type d'animal existant")
  void shouldUpdateExistingPetType() throws Exception {
    // Given
    String input = "1\n4\n" + EXISTING_PETTYPE_ID_1 + "\nDog_Upodated\n\n0\n0\n";
    System.setIn(new ByteArrayInputStream(input.getBytes()));
    consoleMenu = new ConsoleMenu(petTypeService);

    // When
    consoleMenu.run();

    // Then
    String output = outputStream.toString();

    assertThat(output).contains(" MODIFIER UN TYPE D'ANIMAL");
    assertThat(output).contains("Animal actuel:");
    assertThat(output).contains("Nouvelles valeurs (Entrée pour conserver)");
    assertThat(output).contains("✅ Type d'animal modifié avec succès !");
    assertThat(output).contains("ID       : " + EXISTING_PETTYPE_ID_1);
    assertThat(output).contains("Nom      : Dog_Upodated");
    assertThat(output).contains("👋 Merci d'avoir utilisé Pet Store !");
    assertThat(output).contains("À bientôt !");
  }

  @Test
  @DisplayName("Ne doit pas mettre à jour un type d'animal inexistant")
  void shouldNotUpdateNonExistingPetType() throws Exception {
    // Given
    String input = "1\n4\n" + NON_EXISTING_PETTYPE_ID + "\n\n0\n0\n";
    System.setIn(new ByteArrayInputStream(input.getBytes()));
    consoleMenu = new ConsoleMenu(petTypeService);

    // When
    consoleMenu.run();

    // Then
    String output = outputStream.toString();

    assertThat(output).contains(" MODIFIER UN TYPE D'ANIMAL");
    assertThat(output).contains("Aucun type d'animal trouvé avec l'ID: " + NON_EXISTING_PETTYPE_ID);
    assertThat(output).contains("👋 Merci d'avoir utilisé Pet Store !");
    assertThat(output).contains("À bientôt !");
  }
}
