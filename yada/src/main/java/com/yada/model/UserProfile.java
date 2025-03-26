package com.yada.model;

import java.io.Serializable;

public class UserProfile implements Serializable {
    private String gender;
    private double height;
    private double weight;
    private int age;
    private String activityLevel;

    public UserProfile(String gender, double height, double weight, int age, String activityLevel) {
        this.gender = gender;
        this.height = height;
        this.weight = weight;
        this.age = age;
        this.activityLevel = activityLevel;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public void setActivityLevel(String activityLevel) {
        this.activityLevel = activityLevel;
    }

    // Getters
    public String getGender() { return gender; }
    public double getHeight() { return height; }
    public double getWeight() { return weight; }
    public int getAge() { return age; }
    public String getActivityLevel() { return activityLevel; }
}



