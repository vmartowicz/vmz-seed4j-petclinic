package com.petclinic.owner.infrastructure.secondary;

import static com.petclinic.owner.domain.OwnersFixture.*;
import static org.assertj.core.api.Assertions.*;

import com.petclinic.IntegrationTest;
import com.petclinic.owner.domain.Address;
import com.petclinic.owner.domain.City;
import com.petclinic.owner.domain.Firstname;
import com.petclinic.owner.domain.Lastname;
import com.petclinic.owner.domain.LastnameStart;
import com.petclinic.owner.domain.Owner;
import com.petclinic.owner.domain.OwnerToCreate;
import com.petclinic.owner.domain.Telephone;
import com.petclinic.shared.pagination.domain.Seed4jpetclinicPage;
import com.petclinic.shared.pagination.domain.Seed4jpetclinicPageable;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@IntegrationTest
class SpringDataOwnersRepositoryIT {

  @Autowired
  private SpringDataOwnersRepository owners;

  @Autowired
  private JpaOwnersRepository jpaOwners;

  @Test
  void shouldSaveAndGetOwnerWithPetsAndVisits() {
    owners.save(george());

    Owner saved = owners.get(georgeId()).orElseThrow();

    assertThat(saved).usingRecursiveComparison().ignoringFields("pets").isEqualTo(george());
    assertThat(saved.pets()).usingRecursiveFieldByFieldElementComparator().containsExactlyElementsOf(george().pets());
  }

  @Test
  void shouldNotGetUnknownOwner() {
    assertThat(owners.get(ownerToCreate().create().id())).isEmpty();
  }

  @Test
  void shouldSearchByLastnamePrefixIgnoringCase() {
    String prefix = uniquePrefix();
    saved(prefix + "son");
    saved(prefix + "berg");

    Seed4jpetclinicPage<Owner> page = owners.search(new LastnameStart(prefix.toUpperCase()), new Seed4jpetclinicPageable(0, 5));

    assertThat(page.content())
      .extracting(owner -> owner.lastname().get())
      .containsExactly(prefix + "berg", prefix + "son");
  }

  @Test
  void shouldNotMatchLastnameInfix() {
    String prefix = uniquePrefix();
    saved("x" + prefix);

    assertThat(owners.search(new LastnameStart(prefix), new Seed4jpetclinicPageable(0, 5)).content()).isEmpty();
  }

  @Test
  void shouldNotUseWildcardsInLastnameStart() {
    String prefix = uniquePrefix();
    saved(prefix + "son");

    assertThat(owners.search(new LastnameStart(prefix.substring(0, 2) + "%"), new Seed4jpetclinicPageable(0, 5)).content()).isEmpty();
  }

  @Test
  void shouldReturnAllOwnersWithoutLastname() {
    saved(uniquePrefix());

    Seed4jpetclinicPage<Owner> page = owners.search(new LastnameStart(null), new Seed4jpetclinicPageable(0, 5));

    assertThat(page.totalElementsCount()).isEqualTo(jpaOwners.count());
  }

  @Test
  void shouldPaginateByFive() {
    String prefix = uniquePrefix();
    for (int i = 0; i < 7; i++) {
      saved(prefix + i);
    }

    Seed4jpetclinicPage<Owner> page = owners.search(new LastnameStart(prefix), new Seed4jpetclinicPageable(1, 5));

    assertThat(page.content())
      .extracting(owner -> owner.lastname().get())
      .containsExactly(prefix + 5, prefix + 6);
    assertThat(page.currentPage()).isEqualTo(1);
    assertThat(page.pageCount()).isEqualTo(2);
    assertThat(page.totalElementsCount()).isEqualTo(7);
  }

  private void saved(String lastname) {
    owners.save(
      new OwnerToCreate(
        new Firstname("Jean"),
        new Lastname(lastname),
        new Address("1 rue de Paris"),
        new City("Paris"),
        new Telephone("0123456789")
      ).create()
    );
  }

  private static String uniquePrefix() {
    return "Rp" + UUID.randomUUID().toString().substring(0, 8);
  }
}
