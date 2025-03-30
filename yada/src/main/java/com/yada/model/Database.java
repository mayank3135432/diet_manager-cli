package com.yada.model;

import java.util.ArrayList;
import java.util.List;

public class Database {
    private List<Food> foods;
    private List<DailyLog> dailyLogs;
    private List<UserProfile> users;

    public Database() {
        this.foods = new ArrayList<>();
        this.dailyLogs = new ArrayList<>();
        this.users = new ArrayList<>();
    }

    public List<Food> getFoods() {
        return foods;
    }

    public void setFoods(List<Food> foods) {
        this.foods = foods;
    }

    public List<DailyLog> getDailyLogs() {
        return dailyLogs;
    }

    public void setDailyLogs(List<DailyLog> dailyLogs) {
        this.dailyLogs = dailyLogs;
    }

    public List<UserProfile> getUsers() {
        return users;
    }

    public void setUsers(List<UserProfile> users) {
        this.users = users;
    }
}