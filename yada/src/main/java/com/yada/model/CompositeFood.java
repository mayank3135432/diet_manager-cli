package com.yada.model;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class CompositeFood extends Food {
  private List<Food> ingredients;
  private Map<Food, Integer> ingredientServings;

  public CompositeFood(String identifier, List<String> keywords, List<Food> ingredients) {
      super(identifier, keywords);
      this.ingredients = ingredients;
      this.ingredientServings = new HashMap<>();
      for (Food ingredient : ingredients) {
          ingredientServings.put(ingredient, 1); // default 1 serving
      }
  }

  public void addIngredient(Food food, int servings) {
      if (!ingredients.contains(food)) {
          ingredients.add(food);
      }
      ingredientServings.put(food, servings);
  }

  @Override
  public int getCalories() {
      return ingredients.stream()
          .mapToInt(ingredient -> 
              ingredient.getCalories() * ingredientServings.getOrDefault(ingredient, 1))
          .sum();
  }

  @Override
  public List<String> getKeywords() {
      return keywords;
  }
}