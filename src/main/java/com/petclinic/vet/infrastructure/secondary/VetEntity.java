package com.petclinic.vet.infrastructure.secondary;

import com.petclinic.shared.generation.domain.ExcludeFromGeneratedCodeCoverage;
import com.petclinic.vet.domain.Firstname;
import com.petclinic.vet.domain.Lastname;
import com.petclinic.vet.domain.Specialty;
import com.petclinic.vet.domain.Vet;
import com.petclinic.vet.domain.VetId;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Entity
@Table(name = "vets")
class VetEntity {

  @Id
  @Column(name = "id")
  private UUID id;

  @Column(name = "first_name")
  private String firstName;

  @Column(name = "last_name")
  private String lastName;

  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(name = "vet_specialties", joinColumns = @JoinColumn(name = "vet_id"))
  @Enumerated(EnumType.STRING)
  @Column(name = "specialty")
  private Set<Specialty> specialties = new HashSet<>();

  Vet toDomain() {
    return Vet.builder()
      .id(new VetId(id))
      .firstname(new Firstname(firstName))
      .lastname(new Lastname(lastName))
      .specialties(specialties)
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

    VetEntity other = (VetEntity) obj;

    return new EqualsBuilder().append(id, other.id).isEquals();
  }
}
