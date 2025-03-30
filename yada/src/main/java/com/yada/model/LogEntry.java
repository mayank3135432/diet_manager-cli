package com.yada.model;

public class LogEntry {
    private int foodId;
    private int servings;

    // Default constructor for Jackson
    public LogEntry() {
    }

    public LogEntry(int foodId, int servings) {
        this.foodId = foodId;
        this.servings = servings;
    }

    public int getFoodId() {
        return foodId;
    }

    public void setFoodId(int foodId) {
        this.foodId = foodId;
    }

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }
}