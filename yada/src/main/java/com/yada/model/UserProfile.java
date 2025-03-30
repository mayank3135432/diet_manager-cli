package com.yada.model;

import java.util.ArrayList;
import java.util.List;

public class UserProfile {
    private int id;
    private String name;
    private String gender;
    private int height;
    private int age;
    private String activityLevel;
    private List<WeightHistory> weightHistory;
    private CalorieGoal calorieGoal;

    public UserProfile() {
        this.weightHistory = new ArrayList<>();
    }
    
    public UserProfile(int id, String name, String gender, int height, int age, String activityLevel,
                       List<WeightHistory> weightHistory, CalorieGoal calorieGoal) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.height = height;
        this.age = age;
        this.activityLevel = activityLevel;
        this.weightHistory = weightHistory;
        this.calorieGoal = calorieGoal;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public int getHeight() {
        return height;
    }

    public int getAge() {
        return age;
    }

    public String getActivityLevel() {
        return activityLevel;
    }

    public List<WeightHistory> getWeightHistory() {
        return weightHistory;
    }

    public CalorieGoal getCalorieGoal() {
        return calorieGoal;
    }
}