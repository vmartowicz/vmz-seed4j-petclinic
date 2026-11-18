package com.petclinic.owner.application;

import com.petclinic.owner.domain.LastnameStart;
import com.petclinic.owner.domain.Owner;
import com.petclinic.owner.domain.OwnerId;
import com.petclinic.owner.domain.OwnerToCreate;
import com.petclinic.owner.domain.OwnerToUpdate;
import com.petclinic.owner.domain.OwnersRepository;
import com.petclinic.owner.domain.Pet;
import com.petclinic.owner.domain.PetId;
import com.petclinic.owner.domain.PetToCreate;
import com.petclinic.owner.domain.PetToUpdate;
import com.petclinic.owner.domain.PetType;
import com.petclinic.owner.domain.UnknownOwnerException;
import com.petclinic.owner.domain.Visit;
import com.petclinic.owner.domain.VisitToCreate;
import com.petclinic.shared.pagination.domain.Seed4jpetclinicPage;
import com.petclinic.shared.pagination.domain.Seed4jpetclinicPageable;
import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OwnersApplicationService {

  private static final int PAGE_SIZE = 5;

  private final OwnersRepository owners;
  private final Clock clock;

  public OwnersApplicationService(OwnersRepository owners, Clock clock) {
    this.owners = owners;
    this.clock = clock;
  }

  public Owner register(OwnerToCreate ownerToCreate) {
    Owner owner = ownerToCreate.create();
    owners.save(owner);

    return owner;
  }

  public Owner update(OwnerId id, OwnerToUpdate ownerToUpdate) {
    Owner owner = owner(id);
    owner.update(ownerToUpdate);
    owners.save(owner);

    return owner;
  }

  @Transactional(readOnly = true)
  public Seed4jpetclinicPage<Owner> search(LastnameStart lastname, int page) {
    return owners.search(lastname, new Seed4jpetclinicPageable(page, PAGE_SIZE));
  }

  @Transactional(readOnly = true)
  public Owner get(OwnerId id) {
    return owner(id);
  }

  public Pet addPet(OwnerId id, PetToCreate petToCreate) {
    Owner owner = owner(id);
    Pet pet = owner.addPet(petToCreate, today());
    owners.save(owner);

    return pet;
  }

  public Pet updatePet(OwnerId id, PetId pet, PetToUpdate petToUpdate) {
    Owner owner = owner(id);
    Pet updated = owner.updatePet(pet, petToUpdate, today());
    owners.save(owner);

    return updated;
  }

  public Visit scheduleVisit(OwnerId id, PetId pet, VisitToCreate visitToCreate) {
    Owner owner = owner(id);
    Visit visit = owner.scheduleVisit(pet, visitToCreate, today());
    owners.save(owner);

    return visit;
  }

  @Transactional(readOnly = true)
  public List<PetType> petTypes() {
    return List.of(PetType.values());
  }

  private Owner owner(OwnerId id) {
    return owners.get(id).orElseThrow(() -> new UnknownOwnerException(id));
  }

  private LocalDate today() {
    return LocalDate.now(clock);
  }
}
