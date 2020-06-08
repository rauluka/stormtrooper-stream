package com.rauluka.stormtrooper.model;

import java.util.Random;
import java.util.UUID;

public class StormtrooperAcademy {

  private static final Random RANDOM = new Random();
  private static final AcademyGrade[] ACADEMY_GRADES = AcademyGrade.values();
  private static final Planet[] ASTRONOMICAL_OBJECTS = Planet.values();

  public StormtrooperAcademy() {
  }

  public Stormtrooper educateStormtrooper() {
    UUID id = UUID.randomUUID();
    AcademyGrade grade = ACADEMY_GRADES[RANDOM.nextInt(ACADEMY_GRADES.length)];
    Planet preferredTerrain = ASTRONOMICAL_OBJECTS[RANDOM.nextInt(ASTRONOMICAL_OBJECTS.length)];

    return new Stormtrooper(id, grade, preferredTerrain);
  }

}
