package com.yada.model;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FoodDeserializer extends StdDeserializer<Food> {

    public FoodDeserializer() {
        super(Food.class);
    }

    @Override
    public Food deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        JsonNode node = parser.getCodec().readTree(parser);

        int id = node.get("id").asInt();
        String name = node.get("name").asText();
        List<String> keywords = new ArrayList<>();
        node.get("keywords").forEach(keyword -> keywords.add(keyword.asText()));
        int calories = node.get("calories").asInt();
        boolean isComposite = node.get("composite").asBoolean();

        if (isComposite) {
            List<Ingredient> ingredients = new ArrayList<>();
            node.get("ingredients").forEach(ingredientNode -> {
                int foodId = ingredientNode.get("foodId").asInt();
                int servings = ingredientNode.get("servings").asInt();
                ingredients.add(new Ingredient(foodId, servings));
            });
            return new CompositeFood(id, name, keywords, calories, ingredients);
        } else {
            return new BasicFood(id, name, keywords, calories);
        }
    }
}