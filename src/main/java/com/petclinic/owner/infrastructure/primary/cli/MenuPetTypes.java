package com.petclinic.owner.infrastructure.primary.cli;

import com.petclinic.owner.application.PetTypesApplicationService;
import com.petclinic.owner.domain.pettype.PetType;
import com.petclinic.owner.domain.pettype.PetTypeId;
import com.petclinic.owner.domain.pettype.PetTypeName;
import com.petclinic.owner.domain.pettype.PetTypeToCreate;
import java.util.Optional;
import java.util.Scanner;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class MenuPetTypes /*implements CommandLineRunner*/ {

  private final PetTypesApplicationService service;
  private final Scanner scanner = new Scanner(System.in);

  public MenuPetTypes(PetTypesApplicationService service) {
    this.service = service;
  }

  //@Override
  public void run(String... args) {
    while (true) {
      System.out.println(
        """
        ==== PetStore PetType CLI ====
        1. Create PetType
        2. Find PetType
        3. Update PetType
        4. Delete PetType
        5. Exit
        Enter your choice:
        """
      );
      switch (scanner.nextLine()) {
        case "1" -> createPetType();
        case "2" -> findPetType();
        case "3" -> updatePetType();
        case "4" -> deletePetType();
        case "5" -> {
          return;
        }
        default -> System.out.println("Invalid choice.");
      }
    }
  }

  private void createPetType() {
    System.out.println("Enter pet type data (name):");
    String[] parts = scanner.nextLine().split("-");
    try {
      // TODO CliPetTypeToCreate ?
      service.create(new PetTypeToCreate(new PetTypeName(parts[0])));
      System.out.println("PetType created.");
    } catch (Exception e) {
      System.out.println("Error: " + e.getMessage());
    }
  }

  private void findPetType() {
    System.out.println("Enter pet type id:");
    try {
      Optional<PetType> petType = service.find(new PetTypeId(UUID.fromString(scanner.nextLine())));
      petType.ifPresent(p -> System.out.println(petType.get()));
    } catch (Exception e) {
      System.out.println("Error: " + e.getMessage());
    }
  }

  private void updatePetType() {
    System.out.println("Enter updated data (same format as creation):");
    createPetType(); // same logic
  }

  private void deletePetType() {
    System.out.println("Enter pet type id:");
    try {
      service.remove(new PetTypeId(UUID.fromString(scanner.nextLine())));
      System.out.println("PetType deleted.");
    } catch (Exception e) {
      System.out.println("Error: " + e.getMessage());
    }
  }
}
