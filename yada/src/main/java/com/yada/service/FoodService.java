package com.yada.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.yada.model.*;
import java.util.*;
import java.io.*;
import java.util.stream.Collectors;

public class FoodService {
    private List<Food> foods;
    private static final String DATABASE_FILE = "foods.json";
    private final ObjectMapper mapper;
  
    public FoodService() {
        this.foods = new ArrayList<>();
        this.mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.activateDefaultTyping(mapper.getPolymorphicTypeValidator(), 
                                    ObjectMapper.DefaultTyping.NON_FINAL);
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
        try {
            mapper.writeValue(new File(DATABASE_FILE), foods);
        } catch (IOException e) {
            System.err.println("Error saving food database: " + e.getMessage());
        }
    }

    public void loadDatabase() {
        File file = new File(DATABASE_FILE);
        if (!file.exists()) {
            foods = new ArrayList<>();
            return;
        }
        
        try {
            TypeFactory typeFactory = mapper.getTypeFactory();
            foods = mapper.readValue(file, 
                    typeFactory.constructCollectionType(List.class, Food.class));
        } catch (IOException e) {
            System.err.println("Error loading food database: " + e.getMessage());
            foods = new ArrayList<>();
        }
    }
}