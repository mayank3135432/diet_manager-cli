package com.yada.model;

import java.util.List;

public class BasicFood extends Food {
    public BasicFood(int id, String name, List<String> keywords, int calories) {
        super(id, name, keywords, calories, false);
    }
}