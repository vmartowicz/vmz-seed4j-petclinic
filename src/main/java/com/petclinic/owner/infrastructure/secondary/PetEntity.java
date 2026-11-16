package com.petclinic.owner.infrastructure.secondary;

import com.petclinic.owner.domain.Pet;
import com.petclinic.owner.domain.PetId;
import com.petclinic.owner.domain.PetName;
import com.petclinic.owner.domain.PetType;
import com.petclinic.shared.generation.domain.ExcludeFromGeneratedCodeCoverage;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Entity
@Table(name = "pets")
class PetEntity {

  @Id
  @Column(name = "id")
  private UUID id;

  @Column(name = "name")
  private String name;

  @Column(name = "birth_date")
  private LocalDate birthDate;

  @Enumerated(EnumType.STRING)
  @Column(name = "type")
  private PetType type;

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "pet_id", nullable = false)
  private List<VisitEntity> visits = new ArrayList<>();

  static PetEntity from(Pet pet) {
    PetEntity entity = new PetEntity();
    entity.id = pet.id().get();
    entity.name = pet.name().get();
    entity.birthDate = pet.birthDate();
    entity.type = pet.type();
    entity.visits = new ArrayList<>(pet.visits().stream().map(VisitEntity::from).toList());

    return entity;
  }

  Pet toDomain() {
    return Pet.builder()
      .id(new PetId(id))
      .name(new PetName(name))
      .birthDate(birthDate)
      .type(type)
      .visits(visits.stream().map(VisitEntity::toDomain).toList())
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

    PetEntity other = (PetEntity) obj;

    return new EqualsBuilder().append(id, other.id).isEquals();
  }
}
