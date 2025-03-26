package com.yada.model;

import java.io.Serializable;
import java.time.LocalDate;
public class LogEntry implements Serializable {
  private LocalDate date;
  private Food food;
  private int servings;

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