package com.petclinic.owner.infrastructure.primary.console;

import java.util.Scanner;

public abstract class AbstactMenu {

  protected static final String INVALID_CHOICE = "❌ Choix invalide ! Veuillez réessayer!";
  protected static final String YOUR_CHOICE = "Votre choix: ";

  // ===========================
  // UTILITAIRES
  // ===========================
  protected void displayInvalidChoice() {
    System.out.println("\n" + INVALID_CHOICE + "\n");
  }

  protected void displayCreateSuccess(String model) {
    System.out.println("\n✅ " + model + " créé avec succès !");
  }

  protected void displayCreateError(String model, Exception e) {
    System.out.println("\n\tErreur : Impossible de créer ce " + model.toLowerCase() + " !!!\n\t" + e.getMessage());
  }

  protected void displayNotFound(String model, String id) {
    System.out.println("\n❌ Aucun " + model.toLowerCase() + " trouvé avec l'ID: " + id);
  }

  protected void displayNotFound(String model) {
    System.out.println("\n❌ Aucun " + model.toLowerCase() + " trouvé.");
  }

  protected void displayDeleteSuccess(String model) {
    System.out.println("\n✅ " + model + " supprimé avec succès !");
  }

  protected void displayDeleteCanceled() {
    System.out.println("\n❌ Suppression annulée.");
  }

  protected void displayNoModelFound(String model, String id) {
    System.out.println("\n❌ Aucun " + model.toLowerCase() + " trouvé avec l'ID: " + id);
  }

  protected String readString(Scanner scanner, String prompt) {
    System.out.print(prompt);
    return scanner.nextLine().trim();
  }

  protected String readStringOrKeep(Scanner scanner, String prompt) {
    System.out.print(prompt);
    return scanner.nextLine().trim();
  }

  protected int readInt(Scanner scanner, String prompt) {
    while (true) {
      try {
        System.out.print(prompt);
        String input = scanner.nextLine().trim();
        return Integer.parseInt(input);
      } catch (NumberFormatException e) {
        System.out.println("❌ Veuillez entrer un nombre valide.");
      }
    }
  }

  protected Long readLong(Scanner scanner, String prompt) {
    while (true) {
      try {
        System.out.print(prompt);
        String input = scanner.nextLine().trim();
        return Long.parseLong(input);
      } catch (NumberFormatException e) {
        System.out.println("❌ Veuillez entrer un nombre valide.");
      }
    }
  }

  protected double readDouble(Scanner scanner, String prompt) {
    while (true) {
      try {
        System.out.print(prompt);
        String input = scanner.nextLine().trim();
        return Double.parseDouble(input);
      } catch (NumberFormatException e) {
        System.out.println("❌ Veuillez entrer un nombre décimal valide.");
      }
    }
  }

  protected void pressEnterToContinue(Scanner scanner) {
    System.out.print("\n[Appuyez sur Entrée pour continuer]");
    scanner.nextLine();
  }
}
