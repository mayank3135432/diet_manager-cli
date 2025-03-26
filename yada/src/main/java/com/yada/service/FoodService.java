package com.yada.service;
import java.util.stream.Collectors;
import com.yada.model.*;
import java.util.*;
import java.time.LocalDate;
import java.io.*;
public class FoodService {
  private List<Food> foods;
  private static final String DATABASE_FILE = "foods.ser";

  public FoodService() {
      this.foods = new ArrayList<>();
  }

  public void addBasicFood(String identifier, List<String> keywords, int calories) {
      BasicFood food = new BasicFood(identifier, keywords, calories);
      foods.add(food);
  }

  public void addCompositeFood(String name, List<Food> ingredients) {
      List<String> keywords = new ArrayList<>();
      for (Food ingredient : ingredients) {
          keywords.addAll(ingredient.getKeywords());
      }
      CompositeFood compositeFood = new CompositeFood(name, keywords, ingredients);
      foods.add(compositeFood);
  }

  public List<Food> searchFood(List<String> keywords) {
      return foods.stream()
          .filter(food -> 
              keywords.stream()
                  .anyMatch(keyword -> 
                      food.getKeywords().stream()
                          .anyMatch(foodKeyword -> 
                              foodKeyword.toLowerCase().contains(keyword.toLowerCase())
                          )
                  )
          )
          .collect(Collectors.toList());
  }

  public List<Food> getAllFoods() {
      return foods;
  }

  public void saveDatabase() {
      try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(DATABASE_FILE))) {
          out.writeObject(foods);
      } catch (IOException e) {
          System.err.println("Error saving food database: " + e.getMessage());
      }
  }

  public void loadDatabase() {
      try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(DATABASE_FILE))) {
          foods = (List<Food>) in.readObject();
      } catch (FileNotFoundException e) {
          // First-time run, no database exists
          foods = new ArrayList<>();
      } catch (IOException | ClassNotFoundException e) {
          System.err.println("Error loading food database: " + e.getMessage());
          foods = new ArrayList<>();
      }
  }
}