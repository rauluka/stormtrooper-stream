package com.rauluka.stormtrooper.model;

import java.io.Serializable;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

/**
 * Every Stormtrooper achieved his academy grade and dreams about the terrain to patrol.
 */
public class Stormtrooper implements Serializable {
  private static final long serialVersionUID = 8599735961527643794L;

  private final UUID id;
  private final AcademyGrade academyGrade;
  private final Planet preferredPlanet;
  private final Random brain;

  Stormtrooper(UUID id, AcademyGrade academyGrade, Planet preferredPlanet) {
    this.id = id;
    this.academyGrade = academyGrade;
    this.preferredPlanet = preferredPlanet;
    this.brain = new Random();
  }

  public boolean tryToMissTarget() {
    int result = this.brain.nextInt(1000);
    return result < academyGrade.getMissingRate();
  }

  public UUID getId() {
    return id;
  }

  public AcademyGrade getAcademyGrade() {
    return academyGrade;
  }

  public Planet getPreferredPlanet() {
    return preferredPlanet;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Stormtrooper that = (Stormtrooper) o;
    return id.equals(that.id) &&
        academyGrade == that.academyGrade &&
        preferredPlanet == that.preferredPlanet;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, academyGrade, preferredPlanet);
  }
}
