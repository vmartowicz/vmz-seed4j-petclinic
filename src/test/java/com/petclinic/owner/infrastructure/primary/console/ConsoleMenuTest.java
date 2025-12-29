package com.petclinic.owner.infrastructure.primary.console;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.petclinic.owner.application.PetTypesApplicationService;
import com.petclinic.owner.infrastructure.secondary.repository.inmemory.PetTypeInMemoryRepository;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ConsoleMenuTest {

  private ConsoleMenu consoleMenu;
  private PetTypesApplicationService petTypeService;

  private final InputStream originalSystemIn = System.in;
  private final PrintStream originalSystemOut = System.out;
  private ByteArrayOutputStream outputStream;

  @BeforeEach
  void setUp() {
    // Créer les vraies implémentations
    PetTypeInMemoryRepository petRepository = new PetTypeInMemoryRepository();

    petTypeService = new PetTypesApplicationService(petRepository);

    // Capturer la sortie console
    outputStream = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outputStream));
  }

  @Test
  @DisplayName("Menu devrait s'initialiser correctement")
  void menuShouldInitializeCorrectly() {
    // Given & When
    consoleMenu = new ConsoleMenu(petTypeService);

    // Then
    assertNotNull(consoleMenu);
  }

  @Test
  @DisplayName("Option 1: Devrait lister tous les animaux")
  void shouldDisplayMainMenu() throws Exception {
    // Given
    String input = "1\n1\n\n0\n0\n";
    System.setIn(new ByteArrayInputStream(input.getBytes()));
    consoleMenu = new ConsoleMenu(petTypeService);

    // When
    consoleMenu.run();

    // Then
    String output = outputStream.toString();
    assertTrue(output.contains("LISTE DES ANIMAUX"));
    assertTrue(output.contains("Rex") || output.contains("Minou")); // Au moins un pet du seed
    assertTrue(output.contains("Au revoir"));
  }
}
