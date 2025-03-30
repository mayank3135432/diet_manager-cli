package com.yada.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FoodDeserializerTest {

    @Test
    public void testDeserializeBasicFood() throws Exception {
        String json = """
        {
            "id": 1,
            "name": "milk",
            "keywords": ["white", "milk", "solid"],
            "calories": 10,
            "composite": false
        }
        """;

        ObjectMapper mapper = new ObjectMapper();
        Food food = mapper.readValue(json, Food.class);

        assertTrue(food instanceof BasicFood);
        assertEquals("milk", food.getName());
        assertEquals(10, food.getCalories());
    }

    @Test
    public void testDeserializeCompositeFood() throws Exception {
        String json = """
        {
            "id": 3,
            "name": "coffee",
            "keywords": ["white", "milk", "solid", "coffee", "seeds", "tea"],
            "calories": 50,
            "ingredients": [
                { "foodId": 1, "servings": 1 },
                { "foodId": 2, "servings": 4 }
            ],
            "composite": true
        }
        """;

        ObjectMapper mapper = new ObjectMapper();
        Food food = mapper.readValue(json, Food.class);

        assertTrue(food instanceof CompositeFood);
        assertEquals("coffee", food.getName());
        assertEquals(50, food.getCalories());
        CompositeFood compositeFood = (CompositeFood) food;
        assertEquals(2, compositeFood.getIngredients().size());
    }
}
