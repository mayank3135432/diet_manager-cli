package com.yada.model;

import java.util.Comparator;

public class BMRCalculator implements CalorieCalculator {
    @Override
    public int calculateTargetCalories(UserProfile user) {
        // Get the most recent weight from weight history
        double weight = user.getWeightHistory().stream()
            .max(Comparator.comparing(WeightHistory::getDate))
            .map(WeightHistory::getWeight)
            .orElse(0.0);

        // Harris-Benedict Equation
        double bmr;
        if (user.getGender().equalsIgnoreCase("Male")) {
            bmr = 88.362 + (13.397 * weight)
                 + (4.799 * user.getHeight())
                 - (5.677 * user.getAge());
        } else {
            bmr = 447.593 + (9.247 * weight)
                 + (3.098 * user.getHeight())
                 - (4.330 * user.getAge());
        }

        // Adjust for activity level
        switch (user.getActivityLevel()) {
            case "Sedentary": return (int) (bmr * 1.2);
            case "Lightly Active": return (int) (bmr * 1.375);
            case "Moderately Active": return (int) (bmr * 1.55);
            case "Very Active": return (int) (bmr * 1.725);
            case "Extra Active": return (int) (bmr * 1.9);
            default: return (int) (bmr * 1.2);
        }
    }
}