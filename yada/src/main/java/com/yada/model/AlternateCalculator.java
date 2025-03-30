package com.yada.model;

import java.util.Comparator;

public class AlternateCalculator implements CalorieCalculator {
    @Override
    public int calculateTargetCalories(UserProfile user) {
        // Get the most recent weight from weight history
        double weight = user.getWeightHistory().stream()
            .max(Comparator.comparing(WeightHistory::getDate))
            .map(WeightHistory::getWeight)
            .orElse(0.0);

        // Mifflin-St Jeor Equation (simplified)
        double bmr;
        if (user.getGender().equalsIgnoreCase("Male") || user.getGender().equalsIgnoreCase("M")) {
            bmr = (10 * weight) + (6.25 * user.getHeight())
                 - (5 * user.getAge()) + 5;
        } else {
            bmr = (10 * weight) + (6.25 * user.getHeight())
                 - (5 * user.getAge()) - 161;
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