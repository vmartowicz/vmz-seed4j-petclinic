package com.petclinic.owner.infrastructure.primary.console;

import com.petclinic.owner.application.PetTypesApplicationService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("console")
public class ConsoleAdapter implements CommandLineRunner {

  private final PetTypesApplicationService petTypesApplicationService;

  public ConsoleAdapter(PetTypesApplicationService petTypesApplicationService) {
    this.petTypesApplicationService = petTypesApplicationService;
  }

  @Override
  public void run(String... args) {
    ConsoleMenu menu = new ConsoleMenu(petTypesApplicationService);
    menu.run();
  }
}
