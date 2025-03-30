package com.yada.model;

public class Ingredient {
    private int foodId;
    private int servings;

    public Ingredient(int foodId, int servings) {
        this.foodId = foodId;
        this.servings = servings;
    }

    public int getFoodId() {
        return foodId;
    }

    public int getServings() {
        return servings;
    }
}