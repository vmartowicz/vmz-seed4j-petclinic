package com.petclinic.owner.infrastructure.primary.console;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.petclinic.owner.application.PetTypesApplicationService;
import com.petclinic.owner.infrastructure.secondary.repository.inmemory.PetTypeInMemoryRepository;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ConsoleMenuTest {

  private ConsoleMenu consoleMenu;
  private PetTypesApplicationService petTypeService;

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
  void shouldInitialize() throws Exception {
    // Given
    consoleMenu = new ConsoleMenu(petTypeService);

    // Then
    assertNotNull(consoleMenu);
  }

  @Test
  @DisplayName("Doit sortir avec un choix invalide")
  void shouldExitWithInvalidChoice() throws Exception {
    // Given
    String input = "99\n0\n";
    System.setIn(new ByteArrayInputStream(input.getBytes()));
    consoleMenu = new ConsoleMenu(petTypeService);
    consoleMenu.run();

    // Then
    String output = outputStream.toString();
    assertThat(output).contains("PET STORE - Console Mode");
    assertThat(output).contains("Architecture Hexagonale en Action");
    assertThat(output).contains("1. 🐕 Gestion des type Animaux (PetTypes)");
    assertThat(output).contains("2. 🐕 Gestion des Animaux (Pets)");
    assertThat(output).contains("3. 👤 Gestion des Clients (Customers)");
    assertThat(output).contains("4. 🛒 Gestion des Commandes (Orders)");
    assertThat(output).contains("0. 🚪 Quitter");
    assertThat(output).contains("❌ Choix invalide ! Veuillez réessayer!");
    assertThat(output).contains("👋 Merci d'avoir utilisé Pet Store !");
    assertThat(output).contains("À bientôt !");
  }

  @Test
  @DisplayName("Menu devrait lister tous les sous-menus")
  void shouldListAllSubMenus() throws Exception {
    // Given
    String input = "0\n";
    System.setIn(new ByteArrayInputStream(input.getBytes()));
    consoleMenu = new ConsoleMenu(petTypeService);
    consoleMenu.run();

    // Then
    String output = outputStream.toString();
    assertNotNull(consoleMenu);
    assertThat(output).contains("PET STORE - Console Mode");
    assertThat(output).contains("Architecture Hexagonale en Action");
    assertThat(output).contains("1. 🐕 Gestion des type Animaux (PetTypes)");
    assertThat(output).contains("2. 🐕 Gestion des Animaux (Pets)");
    assertThat(output).contains("3. 👤 Gestion des Clients (Customers)");
    assertThat(output).contains("4. 🛒 Gestion des Commandes (Orders)");
    assertThat(output).contains("0. 🚪 Quitter");
    assertThat(output).contains("👋 Merci d'avoir utilisé Pet Store !");
    assertThat(output).contains("À bientôt !");
  }
}
