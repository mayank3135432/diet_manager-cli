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

    private void displayMainMenu() {
        System.out.println("\n--- Main Menu ---");
        System.out.println("1. Manage Food Database");
        System.out.println("2. Manage Daily Log");
        System.out.println("3. Manage User Profile");
        System.out.println("4. View Daily Nutrition Summary");
        System.out.println("5. Exit");
        System.out.print("Enter your choice: ");
    }
    private void manageFoodDatabase() throws Exception {
        while (true) {
            System.out.println("\n--- Manage Food Database ---");
            System.out.println("1. Add Basic Food");
            System.out.println("2. Add Composite Food");
            System.out.println("3. View All Foods");
            System.out.println("4. Back to Main Menu");
            System.out.print("Enter your choice: ");
    
            int choice = readIntInput();
            switch (choice) {
                case 1:
                    addBasicFood();
                    break;
                case 2:
                    addCompositeFood();
                    break;
                case 3:
                    viewAllFoods();
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }
    
    private void addBasicFood() throws Exception {
        System.out.print("Enter food name: ");
        String name = reader.readLine();
    
        System.out.print("Enter keywords (comma-separated): ");
        List<String> keywords = Arrays.asList(reader.readLine().split(","));
    
        System.out.print("Enter calories: ");
        int calories = Integer.parseInt(reader.readLine());
    
        foodService.addBasicFood(name, keywords, calories);
        System.out.println("Basic food added successfully!");
    }
    
    private void addCompositeFood() throws Exception {
        System.out.print("Enter composite food name: ");
        String name = reader.readLine();
    
        List<Ingredient> ingredients = new ArrayList<>();
        while (true) {
            System.out.println("Select an ingredient (or 0 to finish):");
            List<Food> allFoods = foodService.getAllFoods();
            for (int i = 0; i < allFoods.size(); i++) {
                System.out.printf("%d. %s\n", i + 1, allFoods.get(i).getName());
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
    
            ingredients.add(new Ingredient(selectedFood.getId(), servings));
        }
    
        List<String> keywords = new ArrayList<>();
        System.out.print("Enter keywords for the composite food (comma-separated): ");
        keywords.addAll(Arrays.asList(reader.readLine().split(",")));
    
        foodService.addCompositeFood(name, ingredients);
        System.out.println("Composite food added successfully!");
    }
    
    private void viewAllFoods() {
        List<Food> allFoods = foodService.getAllFoods();
        System.out.println("\n--- All Foods ---");
        for (Food food : allFoods) {
            System.out.println(food.getName() + " (" + food.getCalories() + " calories)");
        }
    }

    private void manageDailyLog() throws Exception {
        while (true) {
            System.out.println("\n--- Manage Daily Log ---");
            System.out.println("1. Add Food to Log");
            System.out.println("2. View Today's Log");
            System.out.println("3. Back to Main Menu");
            System.out.print("Enter your choice: ");
    
            int choice = readIntInput();
            switch (choice) {
                case 1:
                    addFoodToLog();
                    break;
                case 2:
                    viewTodaysLog();
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }
    
    private void addFoodToLog() throws Exception {
        List<Food> allFoods = foodService.getAllFoods();
    
        System.out.println("Select a food to add:");
        for (int i = 0; i < allFoods.size(); i++) {
            System.out.printf("%d. %s\n", i + 1, allFoods.get(i).getName());
        }
    
        int foodChoice = readIntInput();
        if (foodChoice < 1 || foodChoice > allFoods.size()) {
            System.out.println("Invalid selection.");
            return;
        }
    
        Food selectedFood = allFoods.get(foodChoice - 1);
    
        System.out.print("Enter number of servings: ");
        int servings = Integer.parseInt(reader.readLine());
    
        logService.addFoodToLog(currentUser.getId(), currentDate, selectedFood.getId(), servings);
        System.out.println("Food added to log successfully!");
    }
    
    private void viewTodaysLog() {
        List<LogEntry> dailyEntries = logService.getDailyLog(currentUser.getId(), currentDate);
        System.out.println("\n--- Today's Log ---");
        for (LogEntry entry : dailyEntries) {
            Food food = foodService.getFoodById(entry.getFoodId());
            if (food != null) {
                System.out.println(food.getName() + " - " + entry.getServings() + " servings");
            }
        }
    }

    private void manageUserProfile() throws Exception {
        System.out.println("\n--- Manage User Profile ---");
        System.out.println("1. View Profile");
        System.out.println("2. Update Weight");
        System.out.println("3. Back to Main Menu");
        System.out.print("Enter your choice: ");
    
        int choice = readIntInput();
        switch (choice) {
            case 1:
                viewUserProfile();
                break;
            case 2:
                updateWeight();
                break;
            case 3:
                return;
            default:
                System.out.println("Invalid choice. Try again.");
        }
    }
    
    private void viewUserProfile() {
        System.out.println("\n--- User Profile ---");
        System.out.println("Name: " + currentUser.getName());
        System.out.println("Gender: " + currentUser.getGender());
        System.out.println("Height: " + currentUser.getHeight() + " cm");
        System.out.println("Age: " + currentUser.getAge());
        System.out.println("Activity Level: " + currentUser.getActivityLevel());
        System.out.println("Weight History:");
        for (WeightHistory history : currentUser.getWeightHistory()) {
            System.out.println(history.getDate() + " - " + history.getWeight() + " kg");
        }
    }
    
    private void updateWeight() throws Exception {
        System.out.print("Enter your current weight (kg): ");
        double weight = Double.parseDouble(reader.readLine());
        currentUser.getWeightHistory().add(new WeightHistory(LocalDate.now(), weight));
        System.out.println("Weight updated successfully!");
    }

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
            logService.loadLogs();
            userProfileService.loadUserProfiles();

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
                        logService.saveLogs();
                        userProfileService.saveUserProfiles();
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
        if (userProfileService.getAllUsers().isEmpty()) {
            System.out.println("Welcome to YADA! Let's set up your profile.");

            System.out.print("Enter your name: ");
            String name = reader.readLine();

            System.out.print("Enter Gender (Male/Female): ");
            String gender = reader.readLine();

            System.out.print("Enter Height (cm): ");
            int height = Integer.parseInt(reader.readLine());

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

            List<WeightHistory> weightHistory = new ArrayList<>();
            System.out.print("Enter your current weight (kg): ");
            double weight = Double.parseDouble(reader.readLine());
            weightHistory.add(new WeightHistory(LocalDate.now(), weight));

            CalorieGoal calorieGoal = new CalorieGoal("BMR", 0);

            UserProfile profile = new UserProfile(
                1, name, gender, height, age, activityLevel, weightHistory, calorieGoal
            );
            userProfileService.addUserProfile(profile);
        }

        currentUser = userProfileService.getAllUsers().get(0);
    }

    private void viewDailyNutritionSummary() {
        List<LogEntry> dailyEntries = logService.getDailyLog(currentUser.getId(), currentDate);
        int totalCaloriesConsumed = dailyEntries.stream()
            .mapToInt(entry -> {
                Food food = foodService.getFoodById(entry.getFoodId());
                return food != null ? food.getCalories() * entry.getServings() : 0;
            })
            .sum();

        // Use BMRCalculator for calorie calculation
        CalorieCalculator calculator = new BMRCalculator();
        int targetCalories = calculator.calculateTargetCalories(currentUser);

        System.out.println("\n--- Daily Nutrition Summary ---");
        System.out.println("Date: " + currentDate);
        System.out.println("Total Calories Consumed: " + totalCaloriesConsumed);
        System.out.println("Target Calories: " + targetCalories);
        System.out.println("Calories Difference: " + (targetCalories - totalCaloriesConsumed));
    }

    private int readIntInput() throws Exception {
        String input = reader.readLine();
        if (input == null || input.trim().isEmpty()) {
            System.out.println("Input cannot be empty. Please enter a valid number.");
            return readIntInput();
        }
        try {
            return Integer.parseInt(input.trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid number.");
            return readIntInput();
        }
    }

    public static void main(String[] args) {
        new YADAConsoleApp().start();
    }
}