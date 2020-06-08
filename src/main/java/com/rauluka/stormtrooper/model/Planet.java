package com.rauluka.stormtrooper.model;

public enum Planet {
  TATOOINE("Desert"),
  HOTH("Ice"),
  ENDOR("Jungle");

  private String terrain;

  Planet(String terrain) {
    this.terrain = terrain;
  }
}
