package com.yada.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DailyLog {
    private int userId;
    private LocalDate date;
    private List<LogEntry> entries;

    public DailyLog() {
        this.entries = new ArrayList<>();
    }
    
    public DailyLog(int userId, LocalDate date) {
        this.userId = userId;
        this.date = date;
        this.entries = new ArrayList<>();
    }

    public int getUserId() {
        return userId;
    }

    public LocalDate getDate() {
        return date;
    }

    public List<LogEntry> getEntries() {
        return entries;
    }

    public void addEntry(LogEntry entry) {
        entries.add(entry);
    }
}