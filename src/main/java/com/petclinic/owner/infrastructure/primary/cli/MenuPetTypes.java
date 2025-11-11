package com.petclinic.owner.infrastructure.primary.cli;

import com.petclinic.owner.application.PetTypesApplicationService;
import com.petclinic.owner.domain.pettype.*;
import java.util.Optional;
import java.util.Scanner;
import java.util.UUID;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("cli")
public class MenuPetTypes implements CommandLineRunner {

  private final PetTypesApplicationService service;
  private final Scanner scanner = new Scanner(System.in);

  public MenuPetTypes(PetTypesApplicationService service) {
    this.service = service;
  }

  @Override
  public void run(String... args) {
    while (true) {
      System.out.println(
        """
        ==== PetStore PetType CLI ====
        1. Create PetType
        2. Find PetType
        3. Find all PetType
        4. Update PetType
        5. Delete PetType
        6. Exit
        Enter your choice:
        """
      );
      switch (scanner.nextLine()) {
        case "1" -> createPetType();
        case "2" -> findPetType();
        case "3" -> findAllPetType();
        case "4" -> updatePetType();
        case "5" -> deletePetType();
        case "6" -> {
          return;
        }
        default -> System.out.println("Invalid choice.");
      }
    }
  }

  private void createPetType() {
    System.out.println("\n\n\t---   Create a Pet Type   ---");
    System.out.println("\tUsage   : Name");
    System.out.println("\tExample : Cat\n");
    String[] parts = scanner.nextLine().split("-");

    try {
      CliPetType petType = CliPetType.from(service.create(new PetTypeToCreate(new PetTypeName(parts[0]))));
      System.out.println("\n\tInfo : PetType created. " + petType + "\nPress enter to continue...");
    } catch (Exception e) {
      System.out.println("\n\tError : Cannot create this Pet Type !!!\n\t" + e.getMessage());
    }
  }

  private void findPetType() {
    System.out.println("\n\n\t---   Find a Pet Type   ---");
    System.out.println("\tUsage : Pet Type Id\n");
    String id = scanner.nextLine();
    try {
      Optional<PetType> petType = service.find(new PetTypeId(UUID.fromString(id)));
      petType.ifPresentOrElse(
        p -> {
          System.out.println(CliPetType.from(p));
          System.out.println("\n\tPress enter to continue...");
        },
        () -> System.out.println("\n\tWarning : This Pet Type {" + id + "} doesn't exist !")
      );
    } catch (Exception e) {
      System.out.println("\n\tError : Cannot find this Pet Type {" + id + "}!!! \n\t" + e.getMessage());
    }
  }

  private void findAllPetType() {
    System.out.println("\n\n\t---   Find all Pet Types   ---");
    try {
      CliPetTypes petTypes = CliPetTypes.from(service.findAllPetTypes());
      petTypes.getPettypes().forEach(System.out::println);
      System.out.println("\n\tPress enter to continue...");
    } catch (Exception e) {
      System.out.println("\n\tError : Cannot find Pet Types !!! \n\t" + e.getMessage());
    }
  }

  private void updatePetType() {
    System.out.println("\n\n\t---   Update a Pet Type   ---");
    System.out.println("\tUsage : Pet Type Id\n");
    String id = scanner.nextLine();

    try {
      Optional<PetType> petType = service.find(new PetTypeId(UUID.fromString(id)));

      petType.ifPresentOrElse(
        p -> {
          System.out.println(CliPetType.from(p));
          String[] parts = scanner.nextLine().split("-");
          CliPetType petTypeUpdated = CliPetType.from(
            service.update(new PetTypeToUpdate(new PetTypeId(UUID.fromString(id)), new PetTypeName(parts[0])))
          );
          System.out.println("\n\tInfo : PetType updated " + petTypeUpdated + "\nPress enter to continue...");
        },
        () -> System.out.println("\n\tWarning : This Pet Type {" + id + "} doesn't exist !")
      );
    } catch (Exception e) {
      System.out.println("\n\tError : Cannot find this Pet Id {" + id + "} !!! \n\t" + e.getMessage());
    }
  }

  private void deletePetType() {
    System.out.println("\n\n\t---   Delete a Pet Type   ---");
    System.out.println("\tUsage : Pet Type Id\n");
    String id = scanner.nextLine();
    try {
      Optional<PetType> petType = service.find(new PetTypeId(UUID.fromString(id)));
      petType.ifPresentOrElse(
        p -> {
          service.remove(new PetTypeId(UUID.fromString(id)));
          System.out.println("PetType deleted.");
          System.out.println("\n\tInfo : PetType {" + id + "} deleted. Press enter to continue...");
        },
        () -> System.out.println("\n\tWarning : This Pet Type {" + id + "} doesn't exist !")
      );
    } catch (Exception e) {
      System.out.println("\n\tError : Cannot find this Pet Id {" + id + "} !!! \n\t" + e.getMessage());
    }
  }
}
