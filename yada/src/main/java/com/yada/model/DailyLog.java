package com.yada.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class DailyLog implements Serializable {
    private List<LogEntry> entries;
    private Stack<List<LogEntry>> undoStack;

    public DailyLog() {
        this.entries = new ArrayList<>();
        this.undoStack = new Stack<>();
    }

    public void addFood(LogEntry entry) {
        undoStack.push(new ArrayList<>(entries));
        entries.add(entry);
    }

    public void removeFood(LogEntry entry) {
        undoStack.push(new ArrayList<>(entries));
        entries.remove(entry);
    }

    public void undo() {
        if (!undoStack.isEmpty()) {
            entries = undoStack.pop();
        }
    }

    public List<LogEntry> getEntries() {
        return entries;
    }
}