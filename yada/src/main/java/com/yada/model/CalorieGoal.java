package com.yada.model;

public class CalorieGoal {
    private String method;
    private int targetCalories;

    // Default constructor for Jackson
    public CalorieGoal() {
    }

    public CalorieGoal(String method, int targetCalories) {
        this.method = method;
        this.targetCalories = targetCalories;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public int getTargetCalories() {
        return targetCalories;
    }

    public void setTargetCalories(int targetCalories) {
        this.targetCalories = targetCalories;
    }
}