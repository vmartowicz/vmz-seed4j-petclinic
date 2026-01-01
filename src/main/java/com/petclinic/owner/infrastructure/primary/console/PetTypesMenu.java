package com.petclinic.owner.infrastructure.primary.console;

import com.petclinic.owner.application.PetTypesApplicationService;
import com.petclinic.owner.domain.pettype.*;
import java.util.Optional;
import java.util.Scanner;
import java.util.UUID;

public class PetTypesMenu extends AbstactMenu {

  private static final String NAME = "Type d'animal";

  private final PetTypesApplicationService petTypeService;
  private final Scanner scanner;

  public PetTypesMenu(PetTypesApplicationService petTypeService, Scanner scanner) {
    this.petTypeService = petTypeService;
    this.scanner = scanner;
  }

  public void run() {
    boolean running = true;
    while (running) {
      displayPetTypeMenu();
      int choice = readInt(scanner, YOUR_CHOICE);

      switch (choice) {
        case 1 -> listAllPetTypes();
        case 2 -> findPetTypeById();
        case 3 -> createPetType();
        case 4 -> updatePetType();
        case 5 -> deletePetType();
        case 0 -> running = false;
        default -> displayInvalidChoice();
      }
    }
  }

  private void displayPetTypeMenu() {
    System.out.println("\n┌──────── 🐕 GESTION DES TYPES ANIMAUX ────────┐");
    System.out.println("│                                              │");
    System.out.println("│  1. 📋 Lister tous les types animaux         │");
    System.out.println("│  2. 🔍 Rechercher par ID                     │");
    System.out.println("│  3. ➕ Créer un nouveau type d'animal         │");
    System.out.println("│  4. ✏️  Modifier un type                     │");
    System.out.println("│  5. 🗑️  Supprimer un type                    │");
    System.out.println("│  0. ⬅️  Retour au menu principal             │");
    System.out.println("│                                        │");
    System.out.println("└────────────────────────────────────────┘");
  }

  private void listAllPetTypes() {
    System.out.println("\n📋 ═══════════ LISTE DES TYPES D'ANIMAUX ═══════════");
    PetTypes petTypes = petTypeService.findAllPetTypes();

    if (petTypes.pettypes().isEmpty()) {
      displayNotFound(NAME);
    } else {
      displayAllPetTypes(petTypes);
    }
    System.out.println("═".repeat(55));
    pressEnterToContinue(scanner);
  }

  private static void displayAllPetTypes(PetTypes petTypes) {
    System.out.println();
    System.out.printf("%-36s %-15s%n", "ID", "Nom");
    System.out.println("─".repeat(55));
    petTypes.pettypes().forEach(p -> System.out.printf("%-36s %-15s%n", p.id().get(), p.name()));
  }

  private void findPetTypeById() {
    System.out.println("\n🔍 ═══════════ RECHERCHER UN TYPE ANIMAL ═══════════");
    String id = readString(scanner, "ID du type d'animal: ");
    Optional<PetType> petType = petTypeService.find(new PetTypeId(UUID.fromString(id)));

    petType.ifPresentOrElse(p -> displayPetTypeDetails(CliPetType.from(p)), () -> displayNotFound(NAME, id));

    pressEnterToContinue(scanner);
  }

  private void displayPetTypeDetails(CliPetType petType) {
    System.out.println("\n┌─────────────────────────────────┐");
    System.out.println("│     DÉTAILS DU TYPE D'ANIMAL         │");
    System.out.println("├─────────────────────────────────┤");
    System.out.printf("│ ID       : %-20s│%n", petType.getId());
    System.out.printf("│ Nom      : %-20s│%n", petType.getName());
    System.out.println("└─────────────────────────────────┘");
  }

  private void createPetType() {
    System.out.println("\n➕ ═══════════ CRÉER UN NOUVEAU TYPE D'ANIMAL ═══════════");
    String name = readString(scanner, "Nom du type d'animal: ");
    try {
      PetTypeToCreate petTypeToCreate = new PetTypeToCreate(new PetTypeName(name));
      displayCreateSuccess(NAME);
      displayPetTypeDetails(CliPetType.from(petTypeService.create(petTypeToCreate)));
    } catch (Exception e) {
      displayCreateError(NAME, e);
    }
    pressEnterToContinue(scanner);
  }

  private void updatePetType() {
    System.out.println("\n✏️  ═══════════ MODIFIER UN TYPE D'ANIMAL ═══════════");

    String id = readString(scanner, "ID du type d'animal à modifier: ");
    Optional<PetType> petType = petTypeService.find(new PetTypeId(UUID.fromString(id)));

    petType.ifPresentOrElse(
      p -> {
        System.out.println("\nAnimal actuel:");
        displayPetTypeDetails(CliPetType.from(p));
        System.out.println("\n--- Nouvelles valeurs (Entrée pour conserver) ---");
        String name = readStringOrKeep(scanner, "Nouveau nom [" + p.name().name() + "]: ");
        if (!name.isEmpty()) {
          PetTypeToUpdate petTypeToUpdate = new PetTypeToUpdate(p.id(), new PetTypeName(name));
          PetType petTypeUpdated = petTypeService.update(petTypeToUpdate);
          System.out.println("\n✅ Type d'animal modifié avec succès !");
          displayPetTypeDetails(CliPetType.from(petTypeUpdated));
        }
      },
      () -> displayNotFound(NAME, id)
    );
    pressEnterToContinue(scanner);
  }

  private void deletePetType() {
    System.out.println("\n🗑️  ═══════════ SUPPRIMER UN TYPE D'ANIMAL ═══════════");

    String id = readString(scanner, "ID du type d'animal à supprimer: ");
    Optional<PetType> petType = petTypeService.find(new PetTypeId(UUID.fromString(id)));

    petType.ifPresentOrElse(
      p -> {
        displayPetTypeDetails(CliPetType.from(p));

        String confirm = readString(scanner, "\n⚠️  Confirmer la suppression ? (oui/non): ");
        if (confirm.equalsIgnoreCase("oui") || confirm.equalsIgnoreCase("o")) {
          petTypeService.remove(new PetTypeId(UUID.fromString(id)));
          displayDeleteSuccess(NAME);
        } else {
          displayDeleteCanceled();
        }
      },
      () -> displayNoModelFound(NAME, id)
    );

    pressEnterToContinue(scanner);
  }
}
