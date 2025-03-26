package com.yada.model;

public class AlternateCalculator implements CalorieCalculator {
  @Override
  public int calculateTargetCalories(UserProfile user) {
      // Mifflin-St Jeor Equation (simplified)
      double bmr;
      if (user.getGender().equalsIgnoreCase("M")) {
          bmr = (10 * user.getWeight()) + (6.25 * user.getHeight()) 
                 - (5 * user.getAge()) + 5;
      } else {
          bmr = (10 * user.getWeight()) + (6.25 * user.getHeight()) 
                 - (5 * user.getAge()) - 161;
      }

      // Adjust for activity level
      switch (user.getActivityLevel()) {
          case "Sedentary": return (int)(bmr * 1.2);
          case "Lightly Active": return (int)(bmr * 1.375);
          case "Moderately Active": return (int)(bmr * 1.55);
          case "Very Active": return (int)(bmr * 1.725);
          case "Extra Active": return (int)(bmr * 1.9);
          default: return (int)(bmr * 1.2);
      }
  }
}