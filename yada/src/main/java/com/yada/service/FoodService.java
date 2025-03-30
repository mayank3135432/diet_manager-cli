package com.yada.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.yada.model.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FoodService {
    private List<Food> foods;
    private static final String DATABASE_FILE = "database.json";
    private final ObjectMapper mapper;

    public FoodService() {
        this.foods = new ArrayList<>();
        this.mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule()); // Register JavaTimeModule for LocalDate
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    public void addBasicFood(String name, List<String> keywords, int calories) {
        int id = foods.size() + 1;
        BasicFood food = new BasicFood(id, name, keywords, calories);
        foods.add(food);
    }

    public void addCompositeFood(String name, List<Ingredient> ingredients) {
        int id = foods.size() + 1;
        List<String> keywords = ingredients.stream()
            .map(ingredient -> getFoodById(ingredient.getFoodId()))
            .filter(food -> food != null)
            .flatMap(food -> food.getKeywords().stream())
            .distinct()
            .collect(Collectors.toList());

        int totalCalories = ingredients.stream()
            .mapToInt(ingredient -> {
                Food food = getFoodById(ingredient.getFoodId());
                return food != null ? food.getCalories() * ingredient.getServings() : 0;
            })
            .sum();

        CompositeFood compositeFood = new CompositeFood(id, name, keywords, totalCalories, ingredients);
        foods.add(compositeFood);
    }

    public Food getFoodById(int id) {
        return foods.stream().filter(food -> food.getId() == id).findFirst().orElse(null);
    }

    public List<Food> searchFood(List<String> keywords) {
        return foods.stream()
            .filter(food -> keywords.stream().anyMatch(keyword ->
                food.getKeywords().stream().anyMatch(foodKeyword ->
                    foodKeyword.toLowerCase().contains(keyword.toLowerCase())
                )
            ))
            .collect(Collectors.toList());
    }

    public List<Food> getAllFoods() {
        return foods;
    }

    

    public void saveDatabase() {
        try {
            Database database = loadDatabaseFile();
            database.setFoods(foods);
            mapper.writeValue(new File(DATABASE_FILE), database);
        } catch (IOException e) {
            System.err.println("Error saving food database: " + e.getMessage());
        }
    }

    public void loadDatabase() {
        try {
            Database database = loadDatabaseFile();
            this.foods = database.getFoods();
        } catch (IOException e) {
            System.err.println("Error loading food database: " + e.getMessage());
            this.foods = new ArrayList<>();
        }
    }

    private Database loadDatabaseFile() throws IOException {
        File file = new File(DATABASE_FILE);
        if (!file.exists() || file.length() == 0) { // Check if file exists and is non-empty
            System.out.println("Food database file is empty or missing. Initializing with default values.");
            return new Database(); // Return an empty database object
        }
        return mapper.readValue(file, Database.class);
    }
}