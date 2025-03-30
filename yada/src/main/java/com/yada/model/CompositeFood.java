package com.yada.model;

import java.util.List;

public class CompositeFood extends Food {
    private List<Ingredient> ingredients;

    public CompositeFood(int id, String name, List<String> keywords, int calories, List<Ingredient> ingredients) {
        super(id, name, keywords, calories, true);
        this.ingredients = ingredients;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }
}