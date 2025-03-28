package com.yada.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.yada.model.*;
import java.util.*;
import java.time.LocalDate;
import java.io.*;

public class LogService {
    private Map<LocalDate, DailyLog> logs;
    private static final String LOG_FILE = "logs.json";
    private final ObjectMapper mapper;
  
    public LogService() {
        this.logs = new HashMap<>();
        this.mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.activateDefaultTyping(mapper.getPolymorphicTypeValidator(), 
                                    ObjectMapper.DefaultTyping.NON_FINAL);
    }

    public void addFood(LocalDate date, Food food, int servings) {
        DailyLog dailyLog = logs.computeIfAbsent(date, k -> new DailyLog());
        dailyLog.addFood(new LogEntry(date, food, servings));
    }

    public void removeFood(LocalDate date, LogEntry entry) {
        DailyLog dailyLog = logs.get(date);
        if (dailyLog != null) {
            dailyLog.removeFood(entry);
        }
    }

    public void undo() {
        logs.values().forEach(DailyLog::undo);
    }

    public List<LogEntry> getDailyLog(LocalDate date) {
        DailyLog dailyLog = logs.get(date);
        return dailyLog != null ? dailyLog.getEntries() : new ArrayList<>();
    }

    public void saveLog() {
        try {
            mapper.writeValue(new File(LOG_FILE), logs);
        } catch (IOException e) {
            System.err.println("Error saving log: " + e.getMessage());
        }
    }

    public void loadLog() {
        File file = new File(LOG_FILE);
        if (!file.exists()) {
            logs = new HashMap<>();
            return;
        }
        
        try {
            logs = mapper.readValue(file, 
                    mapper.getTypeFactory().constructMapType(
                            HashMap.class, LocalDate.class, DailyLog.class));
        } catch (IOException e) {
            System.err.println("Error loading log: " + e.getMessage());
            logs = new HashMap<>();
        }
    }
}