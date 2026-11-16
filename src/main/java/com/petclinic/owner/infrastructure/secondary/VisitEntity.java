package com.petclinic.owner.infrastructure.secondary;

import com.petclinic.owner.domain.Visit;
import com.petclinic.owner.domain.VisitDescription;
import com.petclinic.owner.domain.VisitId;
import com.petclinic.shared.generation.domain.ExcludeFromGeneratedCodeCoverage;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.UUID;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Entity
@Table(name = "visits")
class VisitEntity {

  @Id
  @Column(name = "id")
  private UUID id;

  @Column(name = "visit_date")
  private LocalDate date;

  @Column(name = "description")
  private String description;

  static VisitEntity from(Visit visit) {
    VisitEntity entity = new VisitEntity();
    entity.id = visit.id().get();
    entity.date = visit.date();
    entity.description = visit.description().get();

    return entity;
  }

  Visit toDomain() {
    return new Visit(new VisitId(id), date, new VisitDescription(description));
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

    VisitEntity other = (VisitEntity) obj;

    return new EqualsBuilder().append(id, other.id).isEquals();
  }
}
