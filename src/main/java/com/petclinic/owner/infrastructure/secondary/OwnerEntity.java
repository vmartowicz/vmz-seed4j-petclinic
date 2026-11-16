package com.petclinic.owner.infrastructure.secondary;

import com.petclinic.owner.domain.Address;
import com.petclinic.owner.domain.City;
import com.petclinic.owner.domain.Firstname;
import com.petclinic.owner.domain.Lastname;
import com.petclinic.owner.domain.Owner;
import com.petclinic.owner.domain.OwnerId;
import com.petclinic.owner.domain.Telephone;
import com.petclinic.shared.generation.domain.ExcludeFromGeneratedCodeCoverage;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Entity
@Table(name = "owners")
class OwnerEntity {

  @Id
  @Column(name = "id")
  private UUID id;

  @Column(name = "first_name")
  private String firstName;

  @Column(name = "last_name")
  private String lastName;

  @Column(name = "address")
  private String address;

  @Column(name = "city")
  private String city;

  @Column(name = "telephone")
  private String telephone;

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "owner_id", nullable = false)
  private List<PetEntity> pets = new ArrayList<>();

  static OwnerEntity from(Owner owner) {
    OwnerEntity entity = new OwnerEntity();
    entity.id = owner.id().get();
    entity.firstName = owner.firstname().get();
    entity.lastName = owner.lastname().get();
    entity.address = owner.address().get();
    entity.city = owner.city().get();
    entity.telephone = owner.telephone().get();
    entity.pets = new ArrayList<>(owner.pets().stream().map(PetEntity::from).toList());

    return entity;
  }

  Owner toDomain() {
    return Owner.builder()
      .id(new OwnerId(id))
      .firstname(new Firstname(firstName))
      .lastname(new Lastname(lastName))
      .address(new Address(address))
      .city(new City(city))
      .telephone(new Telephone(telephone))
      .pets(pets.stream().map(PetEntity::toDomain).toList())
      .build();
  }

  @Override
  @ExcludeFromGeneratedCodeCoverage
  public int hashCode() {
    return new HashCodeBuilder().append(id).hashCode();
  }

  @Override
  @ExcludeFromGeneratedCodeCoverage
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }

    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }

    OwnerEntity other = (OwnerEntity) obj;

    return new EqualsBuilder().append(id, other.id).isEquals();
  }
}
