package com.petclinic.owner.infrastructure.secondary.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "specialties")
public class SpecialtyEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
  @SequenceGenerator(name = "sequenceGenerator")
  @Column(name = "id")
  private Long id;

  @NotNull
  @Size(max = 80)
  @Column(name = "name", length = 80, nullable = false)
  private String name;

  @ManyToMany(fetch = FetchType.LAZY, mappedBy = "specialties")
  @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
  @JsonIgnoreProperties(value = { "specialties" }, allowSetters = true)
  private Set<VetEntity> vets = new HashSet<>();

  // seed4j-needle-entity-add-field - JHipster will add fields here

  public Long getId() {
    return this.id;
  }

  public SpecialtyEntity id(Long id) {
    this.setId(id);
    return this;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return this.name;
  }

  public SpecialtyEntity name(String name) {
    this.setName(name);
    return this;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Set<VetEntity> getVets() {
    return this.vets;
  }

  public void setVets(Set<VetEntity> vets) {
    if (this.vets != null) {
      this.vets.forEach(i -> i.removeSpecialties(this));
    }
    if (vets != null) {
      vets.forEach(i -> i.addSpecialties(this));
    }
    this.vets = vets;
  }

  public SpecialtyEntity vets(Set<VetEntity> vets) {
    this.setVets(vets);
    return this;
  }

  public SpecialtyEntity addVets(VetEntity vet) {
    this.vets.add(vet);
    vet.getSpecialties().add(this);
    return this;
  }

  public SpecialtyEntity removeVets(VetEntity vet) {
    this.vets.remove(vet);
    vet.getSpecialties().remove(this);
    return this;
  }
}
