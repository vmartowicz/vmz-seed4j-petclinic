package com.petclinic.owner.infrastructure.primary.console;

import com.petclinic.owner.application.PetTypesApplicationService;
import java.util.Scanner;

public class ConsoleMenu extends AbstactMenu {

  private final Scanner scanner;
  private final PetTypesMenu petTypesMenu;

  public ConsoleMenu(PetTypesApplicationService petTypeService) {
    this.scanner = new Scanner(System.in);
    this.petTypesMenu = new PetTypesMenu(petTypeService, scanner);
  }

  public void run(String... args) {
    printHeader();

    boolean running = true;
    while (running) {
      displayMainMenu();
      int choice = readInt(scanner, YOUR_CHOICE);

      switch (choice) {
        case 1 -> petTypesMenu.run();
        case 0 -> {
          displayExit();
          running = false;
        }
        default -> displayInvalidChoice();
      }
    }
  }

  private static void displayExit() {
    System.out.println("\n👋 Merci d'avoir utilisé Pet Store !");
    System.out.println("À bientôt !\n");
  }

  // ===========================
  // MENU PRINCIPAL
  // ===========================

  private void printHeader() {
    System.out.println("\n╔════════════════════════════════════════╗");
    System.out.println("║     🐾 PET STORE - Console Mode 🐾      ║");
    System.out.println("║    Architecture Hexagonale en Action   ║");
    System.out.println("╚════════════════════════════════════════╝\n");
  }

  private void displayMainMenu() {
    System.out.println("\n┌────────────── MENU PRINCIPAL ──────────────┐");
    System.out.println("│                                            │");
    System.out.println("│  1. 🐕 Gestion des type Animaux (PetTypes) │");
    System.out.println("│  2. 🐕 Gestion des Animaux (Pets)          │");
    System.out.println("│  3. 👤 Gestion des Clients (Customers)     │");
    System.out.println("│  4. 🛒 Gestion des Commandes (Orders)      │");
    System.out.println("│  0. 🚪 Quitter                             │");
    System.out.println("│                                            │");
    System.out.println("└────────────────────────────────────────────┘");
  }
}
