package com.yada.cli;

import com.yada.model.*;
import com.yada.service.*;

import java.util.*;
import java.time.LocalDate;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class YADAConsoleApp {
    private FoodService foodService;
    private LogService logService;
    private UserProfileService userProfileService;
    private BufferedReader reader;
    private UserProfile currentUser;
    private LocalDate currentDate;

    public YADAConsoleApp() {
        this.foodService = new FoodService();
        this.logService = new LogService();
        this.userProfileService = new UserProfileService();
        this.reader = new BufferedReader(new InputStreamReader(System.in));
        this.currentDate = LocalDate.now();
    }

    public void start() {
        try {
            // Load existing data
            foodService.loadDatabase();
            logService.loadLog();
            
            // Setup user profile if not exists
            setupUserProfile();

            // Main menu loop
            while (true) {
                displayMainMenu();
                int choice = readIntInput();
                
                switch (choice) {
                    case 1: manageFoodDatabase(); break;
                    case 2: manageDailyLog(); break;
                    case 3: manageUserProfile(); break;
                    case 4: viewDailyNutritionSummary(); break;
                    case 5: 
                        // Save and exit
                        foodService.saveDatabase();
                        logService.saveLog();
                        System.out.println("Exiting YADA. Goodbye!");
                        return;
                    default: 
                        System.out.println("Invalid choice. Try again.");
                }
            }
        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void setupUserProfile() throws Exception {
        if (userProfileService.getUserProfile() == null) {
            System.out.println("Welcome to YADA! Let's set up your profile.");
            
            System.out.print("Enter Gender (M/F): ");
            String gender = reader.readLine();
            
            System.out.print("Enter Height (cm): ");
            double height = Double.parseDouble(reader.readLine());
            
            System.out.print("Enter Weight (kg): ");
            double weight = Double.parseDouble(reader.readLine());
            
            System.out.print("Enter Age: ");
            int age = Integer.parseInt(reader.readLine());
            
            System.out.println("Select Activity Level:");
            System.out.println("1. Sedentary");
            System.out.println("2. Lightly Active");
            System.out.println("3. Moderately Active");
            System.out.println("4. Very Active");
            System.out.println("5. Extra Active");
            int activityChoice = readIntInput();
            
            String activityLevel;
            switch (activityChoice) {
                case 1: activityLevel = "Sedentary"; break;
                case 2: activityLevel = "Lightly Active"; break;
                case 3: activityLevel = "Moderately Active"; break;
                case 4: activityLevel = "Very Active"; break;
                case 5: activityLevel = "Extra Active"; break;
                default: activityLevel = "Sedentary";
            }
            
            UserProfile profile = new UserProfile(gender, height, weight, age, activityLevel);
            userProfileService.setUserProfile(profile);
        }
        
        currentUser = userProfileService.getUserProfile();
    }

    private void displayMainMenu() {
        System.out.println("\n--- YADA: Yet Another Diet Assistant ---");
        System.out.println("Current Date: " + currentDate);
        System.out.println("1. Manage Food Database");
        System.out.println("2. Manage Daily Log");
        System.out.println("3. Update User Profile");
        System.out.println("4. View Daily Nutrition Summary");
        System.out.println("5. Exit");
        System.out.print("Enter your choice: ");
    }

    private void manageFoodDatabase() throws Exception {
        while (true) {
            System.out.println("\n--- Food Database Management ---");
            System.out.println("1. Add Basic Food");
            System.out.println("2. Create Composite Food");
            System.out.println("3. Search Foods");
            System.out.println("4. View All Foods");
            System.out.println("5. Return to Main Menu");
            
            int choice = readIntInput();
            
            switch (choice) {
                case 1: addBasicFood(); break;
                case 2: createCompositeFood(); break;
                case 3: searchFoods(); break;
                case 4: viewAllFoods(); break;
                case 5: return;
                default: System.out.println("Invalid choice.");
            }
        }
    }

    private void addBasicFood() throws Exception {
        System.out.print("Enter food identifier: ");
        String identifier = reader.readLine();
        
        System.out.print("Enter keywords (comma-separated): ");
        List<String> keywords = Arrays.asList(reader.readLine().split(","));
        
        System.out.print("Enter calories per serving: ");
        int calories = Integer.parseInt(reader.readLine());
        
        foodService.addBasicFood(identifier, keywords, calories);
        System.out.println("Basic food added successfully!");
    }

    private void createCompositeFood() throws Exception {
        System.out.print("Enter composite food name: ");
        String name = reader.readLine();
        
        List<Food> ingredients = new ArrayList<>();
        while (true) {
            System.out.println("Select an ingredient (or 0 to finish):");
            List<Food> allFoods = foodService.getAllFoods();
            for (int i = 0; i < allFoods.size(); i++) {
                System.out.printf("%d. %s\n", i+1, allFoods.get(i).getIdentifier());
            }
            
            int ingredientChoice = readIntInput();
            if (ingredientChoice == 0) break;
            
            if (ingredientChoice < 1 || ingredientChoice > allFoods.size()) {
                System.out.println("Invalid selection.");
                continue;
            }
            
            Food selectedFood = allFoods.get(ingredientChoice - 1);
            
            System.out.print("Enter number of servings: ");
            int servings = Integer.parseInt(reader.readLine());
            
            ingredients.add(selectedFood);
        }
        
        foodService.addCompositeFood(name, ingredients);
        System.out.println("Composite food created successfully!");
    }

    private void searchFoods() throws Exception {
        System.out.print("Enter search keywords (comma-separated): ");
        List<String> keywords = Arrays.asList(reader.readLine().split(","));
        
        List<Food> results = foodService.searchFood(keywords);
        
        if (results.isEmpty()) {
            System.out.println("No foods found.");
            return;
        }
        
        System.out.println("Search Results:");
        for (int i = 0; i < results.size(); i++) {
            Food food = results.get(i);
            System.out.printf("%d. %s (Calories: %d)\n", 
                i+1, food.getIdentifier(), food.getCalories());
        }
    }

    private void viewAllFoods() {
        List<Food> allFoods = foodService.getAllFoods();
        if (allFoods.isEmpty()) {
            System.out.println("No foods in the database.");
            return;
        }
        
        System.out.println("All Foods:");
        for (Food food : allFoods) {
            System.out.printf("%s - %d calories\n", 
                food.getIdentifier(), food.getCalories());
        }
    }

    private void manageDailyLog() throws Exception {
        while (true) {
            System.out.println("\n--- Daily Log Management ---");
            System.out.println("1. Add Food to Log");
            System.out.println("2. Remove Food from Log");
            System.out.println("3. Undo Last Action");
            System.out.println("4. View Daily Log");
            System.out.println("5. Change Current Date");
            System.out.println("6. Return to Main Menu");
            
            int choice = readIntInput();
            
            switch (choice) {
                case 1: addFoodToLog(); break;
                case 2: removeFoodFromLog(); break;
                case 3: undoLastAction(); break;
                case 4: viewDailyLog(); break;
                case 5: changeCurrentDate(); break;
                case 6: return;
                default: System.out.println("Invalid choice.");
            }
        }
    }

    private void addFoodToLog() throws Exception {
        List<Food> allFoods = foodService.getAllFoods();
        
        System.out.println("Select a food to add:");
        for (int i = 0; i < allFoods.size(); i++) {
            System.out.printf("%d. %s\n", i+1, allFoods.get(i).getIdentifier());
        }
        
        int foodChoice = readIntInput();
        if (foodChoice < 1 || foodChoice > allFoods.size()) {
            System.out.println("Invalid selection.");
            return;
        }
        
        Food selectedFood = allFoods.get(foodChoice - 1);
        
        System.out.print("Enter number of servings: ");
        int servings = Integer.parseInt(reader.readLine());
        
        logService.addFood(currentDate, selectedFood, servings);
        System.out.println("Food added to log successfully!");
    }

    private void removeFoodFromLog() throws Exception {
        List<LogEntry> dailyEntries = logService.getDailyLog(currentDate);
        
        if (dailyEntries.isEmpty()) {
            System.out.println("No entries in the log for this date.");
            return;
        }
        
        System.out.println("Select an entry to remove:");
        for (int i = 0; i < dailyEntries.size(); i++) {
            LogEntry entry = dailyEntries.get(i);
            System.out.printf("%d. %s (%d servings)\n", 
                i+1, entry.getFood().getIdentifier(), entry.getServings());
        }
        
        int entryChoice = readIntInput();
        if (entryChoice < 1 || entryChoice > dailyEntries.size()) {
            System.out.println("Invalid selection.");
            return;
        }
        
        LogEntry selectedEntry = dailyEntries.get(entryChoice - 1);
        logService.removeFood(currentDate, selectedEntry);
        System.out.println("Food removed from log successfully!");
    }

    private void undoLastAction() {
        logService.undo();
        System.out.println("Last action undone.");
    }

    private void viewDailyLog() {
        List<LogEntry> dailyEntries = logService.getDailyLog(currentDate);
        
        if (dailyEntries.isEmpty()) {
            System.out.println("No entries in the log for this date.");
            return;
        }
        
        System.out.println("Daily Log for " + currentDate + ":");
        for (LogEntry entry : dailyEntries) {
            System.out.printf("%s - %d servings (Calories: %d)\n", 
                entry.getFood().getIdentifier(), 
                entry.getServings(), 
                entry.getTotalCalories());
        }
    }

    private void changeCurrentDate() throws Exception {
        System.out.print("Enter year (YYYY): ");
        int year = Integer.parseInt(reader.readLine());
        
        System.out.print("Enter month (1-12): ");
        int month = Integer.parseInt(reader.readLine());
        
        System.out.print("Enter day: ");
        int day = Integer.parseInt(reader.readLine());
        
        currentDate = LocalDate.of(year, month, day);
        System.out.println("Current date changed to: " + currentDate);
    }

    private void manageUserProfile() throws Exception {
        System.out.println("\n--- User Profile Management ---");
        System.out.println("1. Update Weight");
        System.out.println("2. Update Activity Level");
        System.out.println("3. View Current Profile");
        
        int choice = readIntInput();
        
        switch (choice) {
            case 1: updateWeight(); break;
            case 2: updateActivityLevel(); break;
            case 3: viewUserProfile(); break;
            default: System.out.println("Invalid choice.");
        }
    }

    private void updateWeight() throws Exception {
        System.out.print("Enter new weight (kg): ");
        double newWeight = Double.parseDouble(reader.readLine());
        currentUser.setWeight(newWeight);
        System.out.println("Weight updated successfully!");
    }

    private void updateActivityLevel() throws Exception {
        System.out.println("Select Activity Level:");
        System.out.println("1. Sedentary");
        System.out.println("2. Lightly Active");
        System.out.println("3. Moderately Active");
        System.out.println("4. Very Active");
        System.out.println("5. Extra Active");
        
        int choice = readIntInput();
        
        String activityLevel;
        switch (choice) {
            case 1: activityLevel = "Sedentary"; break;
            case 2: activityLevel = "Lightly Active"; break;
            case 3: activityLevel = "Moderately Active"; break;
            case 4: activityLevel = "Very Active"; break;
            case 5: activityLevel = "Extra Active"; break;
            default: activityLevel = "Sedentary";
        }
        
        currentUser.setActivityLevel(activityLevel);
        System.out.println("Activity level updated successfully!");
    }

    private void viewUserProfile() {
        System.out.println("\nUser Profile:");
        System.out.println("Gender: " + currentUser.getGender());
        System.out.println("Height: " + currentUser.getHeight() + " cm");
        System.out.println("Weight: " + currentUser.getWeight() + " kg");
        System.out.println("Age: " + currentUser.getAge());
        System.out.println("Activity Level: " + currentUser.getActivityLevel());
    }

    private void viewDailyNutritionSummary() {
        List<LogEntry> dailyEntries = logService.getDailyLog(currentDate);
        int totalCaloriesConsumed = dailyEntries.stream()
            .mapToInt(LogEntry::getTotalCalories)
            .sum();
        
        // Use default calculator for now
        CalorieCalculator calculator = new BMRCalculator();
        int targetCalories = calculator.calculateTargetCalories(currentUser);
        
        System.out.println("\n--- Daily Nutrition Summary ---");
        System.out.println("Date: " + currentDate);
        System.out.println("Total Calories Consumed: " + totalCaloriesConsumed);
        System.out.println("Target Calories: " + targetCalories);
        System.out.println("Calories Difference: " + (targetCalories - totalCaloriesConsumed));
    }

    private int readIntInput() throws Exception {
        return Integer.parseInt(reader.readLine());
    }

    public static void main(String[] args) {
        new YADAConsoleApp().start();
    }
}
