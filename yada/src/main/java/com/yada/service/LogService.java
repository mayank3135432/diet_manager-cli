package com.yada.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.yada.model.*;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LogService {
    private List<DailyLog> dailyLogs;
    private static final String DATABASE_FILE = "database.json";
    private final ObjectMapper mapper;

    public LogService() {
        this.dailyLogs = new ArrayList<>();
        this.mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule()); // Register JavaTimeModule for LocalDate
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    public void addFoodToLog(int userId, LocalDate date, int foodId, int servings) {
        DailyLog dailyLog = dailyLogs.stream()
            .filter(log -> log.getUserId() == userId && log.getDate().equals(date))
            .findFirst()
            .orElseGet(() -> {
                DailyLog newLog = new DailyLog(userId, date);
                dailyLogs.add(newLog);
                return newLog;
            });

        dailyLog.addEntry(new LogEntry(foodId, servings));
    }

    public List<LogEntry> getDailyLog(int userId, LocalDate date) {
        return dailyLogs.stream()
            .filter(log -> log.getUserId() == userId && log.getDate().equals(date))
            .map(DailyLog::getEntries)
            .findFirst()
            .orElse(new ArrayList<>());
    }

    public void saveLogs() {
        try {
            Database database = loadDatabaseFile();
            database.setDailyLogs(dailyLogs);
            mapper.writeValue(new File(DATABASE_FILE), database);
        } catch (IOException e) {
            System.err.println("Error saving logs: " + e.getMessage());
        }
    }

    public void loadLogs() {
        try {
            Database database = loadDatabaseFile();
            this.dailyLogs = database.getDailyLogs();
        } catch (IOException e) {
            System.err.println("Error loading logs: " + e.getMessage());
            this.dailyLogs = new ArrayList<>();
        }
    }

    private Database loadDatabaseFile() throws IOException {
        File file = new File(DATABASE_FILE);
        if (!file.exists() || file.length() == 0) { // Check if file exists and is non-empty
            System.out.println("Log database file is empty or missing. Initializing with default values.");
            return new Database(); // Return an empty database object
        }
        return mapper.readValue(file, Database.class);
    }
}