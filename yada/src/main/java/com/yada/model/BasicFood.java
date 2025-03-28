package com.yada.model;

import java.util.List;
public class BasicFood extends Food {
  private int calories;

  // Default constructor for Jackson
  public BasicFood() {
      super(null, null);
  }

  public BasicFood(String identifier, List<String> keywords, int calories) {
      super(identifier, keywords);
      this.calories = calories;
  }

  @Override
  public int getCalories() {
      return calories;
  }

  @Override
  public List<String> getKeywords() {
      return keywords;
  }
}