package com.yada.model;

import java.util.*;
import java.io.Serializable;

public abstract class Food implements Serializable {
    protected String identifier;
    protected List<String> keywords;

    public Food(String identifier, List<String> keywords) {
        this.identifier = identifier;
        this.keywords = keywords;
    }

    public abstract int getCalories();
    public abstract List<String> getKeywords();

    public String getIdentifier() {
        return identifier;
    }
}



