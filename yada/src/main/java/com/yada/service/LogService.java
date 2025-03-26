package com.yada.service;

import com.yada.model.*;
import java.util.*;
import java.time.LocalDate;
import java.io.*;
public class LogService {
  private Map<LocalDate, DailyLog> logs;
  private static final String LOG_FILE = "logs.ser";

  public LogService() {
      this.logs = new HashMap<>();
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
      try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(LOG_FILE))) {
          out.writeObject(logs);
      } catch (IOException e) {
          System.err.println("Error saving log: " + e.getMessage());
      }
  }

  public void loadLog() {
      try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(LOG_FILE))) {
          logs = (Map<LocalDate, DailyLog>) in.readObject();
      } catch (FileNotFoundException e) {
          // First-time run, no log exists
          logs = new HashMap<>();
      } catch (IOException | ClassNotFoundException e) {
          System.err.println("Error loading log: " + e.getMessage());
          logs = new HashMap<>();
      }
  }
}