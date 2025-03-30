package com.yada.model;

import java.time.LocalDate;

public class WeightHistory {
    private LocalDate date;
    private double weight;

    // Default constructor for Jackson
    public WeightHistory() {
    }

    public WeightHistory(LocalDate date, double weight) {
        this.date = date;
        this.weight = weight;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}