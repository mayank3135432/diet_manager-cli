package com.yada.cli;

import com.yada.model.*;
import com.yada.service.*;

import java.util.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class YADAConsoleApp {
    private FoodService foodService;
    private LogService logService;
    private UserProfileService userProfileService;
    private BufferedReader reader;
    private UserProfile currentUser;
    private LocalDate currentDate;
    private Stack<Command> commandHistory = new Stack<>();

    // Add the Command interface for undo functionality
    private interface Command {
        void execute();
        void undo();
    }

    private void displayMainMenu() {
        System.out.println("\n--- Main Menu ---");
        System.out.println("1. Manage Food Database");
        System.out.println("2. Manage Daily Log");
        System.out.println("3. Manage User Profile");
        System.out.println("4. View Daily Nutrition Summary");
        System.out.println("5. Undo Last Action"); // New undo option
        System.out.println("6. Exit");
        System.out.print("Enter your choice: ");
    }
    
    // Implementing the undo functionality
    private void undoLastAction() {
        if (commandHistory.isEmpty()) {
            System.out.println("Nothing to undo.");
            return;
        }
        
        Command lastCommand = commandHistory.pop();
        lastCommand.undo();
        System.out.println("Last action undone successfully.");
    }
    private void manageDailyLog() throws Exception {
        while (true) {
            System.out.println("\n--- Manage Daily Log ---");
            System.out.println("Current selected date: " + currentDate);
            System.out.println("1. Add Food to Log");
            System.out.println("2. View Today's Log");
            System.out.println("3. Delete Food from Log"); // New option
            System.out.println("4. Change Selected Date"); // New option
            System.out.println("5. Back to Main Menu");
            System.out.print("Enter your choice: ");
    
            int choice = readIntInput();
            switch (choice) {
                case 1:
                    addFoodToLog();
                    break;
                case 2:
                    viewCurrentDateLog();
                    break;
                case 3:
                    deleteFoodFromLog(); // New method
                    break;
                case 4:
                    changeSelectedDate(); // New method
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }
        private void changeSelectedDate() throws Exception {
        System.out.println("Enter date (YYYY-MM-DD): ");
        String dateString = reader.readLine();
        
        try {
            LocalDate newDate = LocalDate.parse(dateString);
            currentDate = newDate;
            System.out.println("Date changed to: " + currentDate);
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format. Please use YYYY-MM-DD.");
        }
    }
    private void deleteFoodFromLog() throws Exception {
        List<LogEntry> dailyEntries = logService.getDailyLog(currentUser.getId(), currentDate);
        if (dailyEntries.isEmpty()) {
            System.out.println("No entries to delete for " + currentDate);
            return;
        }
        
        System.out.println("\n--- Food Entries for " + currentDate + " ---");
        for (int i = 0; i < dailyEntries.size(); i++) {
            LogEntry entry = dailyEntries.get(i);
            Food food = foodService.getFoodById(entry.getFoodId());
            if (food != null) {
                System.out.printf("%d. %s - %d servings\n", i + 1, food.getName(), entry.getServings());
            }
        }
        
        System.out.print("Enter number of entry to delete (0 to cancel): ");
        int entryIndex = readIntInput();
        
        if (entryIndex == 0) {
            return;
        }
        
        if (entryIndex < 1 || entryIndex > dailyEntries.size()) {
            System.out.println("Invalid selection.");
            return;
        }
        
        final LogEntry entryToDelete = dailyEntries.get(entryIndex - 1);
        final Food deletedFood = foodService.getFoodById(entryToDelete.getFoodId());
        
        // Create a command for deletion with undo capability
        Command deleteCommand = new Command() {
            @Override
            public void execute() {
                logService.removeFoodFromLog(currentUser.getId(), currentDate, entryIndex - 1);
            }
            @Override
            public void undo() {
                logService.addFoodToLog(currentUser.getId(), currentDate, 
                                        entryToDelete.getFoodId(), entryToDelete.getServings());
            }
        };
        
        // Execute and store the command
        deleteCommand.execute();
        commandHistory.push(deleteCommand);
        System.out.println("Food entry deleted: " + deletedFood.getName());
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


    
    private void addFoodToLog() throws Exception {
        List<Food> allFoods = foodService.getAllFoods();
        if (allFoods.isEmpty()) {
            System.out.println("No foods available. Please add some foods first.");
            return;
        }
    
        System.out.println("Select a food to add:");
        for (int i = 0; i < allFoods.size(); i++) {
            System.out.printf("%d. %s\n", i + 1, allFoods.get(i).getName());
        }
    
        int foodChoice = readIntInput();
        if (foodChoice < 1 || foodChoice > allFoods.size()) {
            System.out.println("Invalid selection.");
            return;
        }
    
        final Food selectedFood = allFoods.get(foodChoice - 1);
    
        System.out.print("Enter number of servings: ");
        final int servings = Integer.parseInt(reader.readLine());
        
        // Create a command for adding food with undo capability
        Command addFoodCommand = new Command() {
            @Override
            public void execute() {
                logService.addFoodToLog(currentUser.getId(), currentDate, selectedFood.getId(), servings);
            }
            
            @Override
            public void undo() {
                // Find and remove the last entry for this food
                List<LogEntry> entries = logService.getDailyLog(currentUser.getId(), currentDate);
                for (int i = entries.size() - 1; i >= 0; i--) {
                    LogEntry entry = entries.get(i);
                    if (entry.getFoodId() == selectedFood.getId() && entry.getServings() == servings) {
                        logService.removeFoodFromLog(currentUser.getId(), currentDate, i);
                        break;
                    }
                }
            }
        };
        
        // Execute and store the command
        addFoodCommand.execute();
        commandHistory.push(addFoodCommand);
        System.out.println("Food added to log successfully: " + selectedFood.getName());
    }
    
    private void viewCurrentDateLog() {
        List<LogEntry> dailyEntries = logService.getDailyLog(currentUser.getId(), currentDate);
        System.out.println("\n--- Food Log for " + currentDate + " ---");
        if (dailyEntries.isEmpty()) {
            System.out.println("No entries for this date.");
            return;
        }
        
        for (int i = 0; i < dailyEntries.size(); i++) {
            LogEntry entry = dailyEntries.get(i);
            Food food = foodService.getFoodById(entry.getFoodId());
            if (food != null) {
                System.out.printf("%d. %s - %d servings (%d calories)\n", 
                    i + 1, food.getName(), entry.getServings(), food.getCalories() * entry.getServings());
            }
        }
    }

    private void manageUserProfile() throws Exception {
        System.out.println("\n--- Manage User Profile ---");
        System.out.println("1. View Profile");
        System.out.println("2. Update Weight");
        System.out.println("3. Update Activity Level"); // New option
        System.out.println("4. Update Age"); // New option
        System.out.println("5. Change Calorie Calculation Method"); // New option
        System.out.println("6. Back to Main Menu");
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
                updateActivityLevel(); // New method
                break;
            case 4:
                updateAge(); // New method
                break;
            case 5:
                changeCalorieCalculationMethod(); // New method
                break;
            case 6:
                return;
            default:
                System.out.println("Invalid choice. Try again.");
        }
    }
    private void updateActivityLevel() throws Exception {
        System.out.println("Select New Activity Level:");
        System.out.println("1. Sedentary");
        System.out.println("2. Lightly Active");
        System.out.println("3. Moderately Active");
        System.out.println("4. Very Active");
        System.out.println("5. Extra Active");
        int activityChoice = readIntInput();
    
        final String oldActivityLevel = currentUser.getActivityLevel();
        final String newActivityLevel;
        
        switch (activityChoice) {
            case 1: newActivityLevel = "Sedentary"; break;
            case 2: newActivityLevel = "Lightly Active"; break;
            case 3: newActivityLevel = "Moderately Active"; break;
            case 4: newActivityLevel = "Very Active"; break;
            case 5: newActivityLevel = "Extra Active"; break;
            default: 
                System.out.println("Invalid choice. Activity level unchanged.");
                return;
        }
        
        // Create command with undo capability
        Command updateActivityCommand = new Command() {
            @Override
            public void execute() {
                userProfileService.updateActivityLevel(currentUser.getId(), newActivityLevel);
            }
            
            @Override
            public void undo() {
                userProfileService.updateActivityLevel(currentUser.getId(), oldActivityLevel);
            }
        };
        
        updateActivityCommand.execute();
        commandHistory.push(updateActivityCommand);
        System.out.println("Activity level updated to: " + newActivityLevel);
    }
    
    private void updateAge() throws Exception {
        System.out.print("Enter new age: ");
        final int newAge = Integer.parseInt(reader.readLine());
        final int oldAge = currentUser.getAge();
        
        // Create command with undo capability
        Command updateAgeCommand = new Command() {
            @Override
            public void execute() {
                userProfileService.updateAge(currentUser.getId(), newAge);
            }
            
            @Override
            public void undo() {
                userProfileService.updateAge(currentUser.getId(), oldAge);
            }
        };
        
        updateAgeCommand.execute();
        commandHistory.push(updateAgeCommand);
        System.out.println("Age updated successfully!");
    }
    
    private void changeCalorieCalculationMethod() throws Exception {
        System.out.println("Select Calorie Calculation Method:");
        System.out.println("1. Harris-Benedict Equation (BMR)");
        System.out.println("2. Mifflin-St Jeor Equation");
        int methodChoice = readIntInput();
        
        final String oldMethod = currentUser.getCalorieGoal().getMethod();
        final String newMethod;
        
        switch (methodChoice) {
            case 1: newMethod = "BMR"; break;
            case 2: newMethod = "Mifflin-St Jeor"; break;
            default:
                System.out.println("Invalid choice. Calculation method unchanged.");
                return;
        }
        
        // Create command with undo capability
        Command changeMethodCommand = new Command() {
            @Override
            public void execute() {
                userProfileService.updateCalorieCalculationMethod(currentUser.getId(), newMethod);
            }
            
            @Override
            public void undo() {
                userProfileService.updateCalorieCalculationMethod(currentUser.getId(), oldMethod);
            }
        };
        
        changeMethodCommand.execute();
        commandHistory.push(changeMethodCommand);
        System.out.println("Calorie calculation method changed to: " + newMethod);
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
    
    private void updateWeight() throws Exception {
        System.out.print("Enter your current weight (kg): ");
        final double weight = Double.parseDouble(reader.readLine());
        final LocalDate today = LocalDate.now();
        
        // Create command for weight update with undo capability
        Command updateWeightCommand = new Command() {
            private WeightHistory newWeightHistory;
            
            @Override
            public void execute() {
                newWeightHistory = new WeightHistory(today, weight);
                currentUser.getWeightHistory().add(newWeightHistory);
            }
            
            @Override
            public void undo() {
                currentUser.getWeightHistory().remove(newWeightHistory);
            }
        };
        
        // Execute and store the command
        updateWeightCommand.execute();
        commandHistory.push(updateWeightCommand);
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
                    case 5: undoLastAction(); break; // New undo option
                    case 6: // Exit (previously case 5)
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
    
        // Use the appropriate calculator based on user's preference
        CalorieCalculator calculator;
        String method = currentUser.getCalorieGoal().getMethod();
        if (method.equals("Mifflin-St Jeor")) {
            calculator = new AlternateCalculator();
        } else {
            calculator = new BMRCalculator(); // Default to BMR
        }
        
        int targetCalories = calculator.calculateTargetCalories(currentUser);
        int caloriesDifference = targetCalories - totalCaloriesConsumed;
        
        // Update the calorieGoal in the user profile
        userProfileService.updateTargetCalories(currentUser.getId(), targetCalories);
    
        System.out.println("\n--- Daily Nutrition Summary ---");
        System.out.println("Date: " + currentDate);
        System.out.println("Calculation Method: " + method);
        System.out.println("Total Calories Consumed: " + totalCaloriesConsumed);
        System.out.println("Target Calories: " + targetCalories);
        
        if (caloriesDifference < 0) {
            System.out.println("Calories Over Target: " + Math.abs(caloriesDifference) + " (excess)");
        } else {
            System.out.println("Calories Available: " + caloriesDifference);
        }
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