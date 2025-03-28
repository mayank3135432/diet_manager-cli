package com.yada.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LogEntry implements Serializable {
  private LocalDate date;
  private Food food;
  private int servings;

  // Default constructor for Jackson
  public LogEntry() {
  }

  public LogEntry(LocalDate date, Food food, int servings) {
      this.date = date;
      this.food = food;
      this.servings = servings;
  }

  public int getTotalCalories() {
      return food.getCalories() * servings;
  }

  public LocalDate getDate() {
      return date;
  }

  public Food getFood() {
      return food;
  }

  public int getServings() {
      return servings;
  }
}