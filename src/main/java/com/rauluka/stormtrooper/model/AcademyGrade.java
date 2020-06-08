package com.rauluka.stormtrooper.model;

public enum AcademyGrade {
  MISSED_THE_WHOLE_POINT("Excellent", 999, 99),
  MISS_BY_A_MILE("Good", 900, 90),
  HIT_AND_MISS("So, so", 700, 70),
  NOT_MISS_MUCH("Try harder", 600, 60);

  private String earthGrade;
  private int missingRate;
  private int margin;

  AcademyGrade(String earthGrade, int missingRate, int margin) {
    this.earthGrade = earthGrade;
    this.missingRate = missingRate;
    this.margin = margin;
  }

  public String getEarthGrade() {
    return earthGrade;
  }

  public int getMissingRate() {
    return missingRate;
  }

  public int getMargin() {
    return margin;
  }
}
