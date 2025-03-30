package com.yada.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

@JsonDeserialize(using = FoodDeserializer.class)
public abstract class Food {
    private int id;
    private String name;
    private List<String> keywords;
    private int calories;
    private boolean isComposite;

    public Food(int id, String name, List<String> keywords, int calories, boolean isComposite) {
        this.id = id;
        this.name = name;
        this.keywords = keywords;
        this.calories = calories;
        this.isComposite = isComposite;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public int getCalories() {
        return calories;
    }

    public boolean isComposite() {
        return isComposite;
    }
}