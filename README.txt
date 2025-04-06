# YADA: Yet Another Diet Assistant

YADA is a Java-based console application that helps users track their food intake, manage daily calorie consumption, and achieve their dietary goals.

## Features

- **User Profile Management**: Set up and manage your personal profile including gender, height, weight, age, and activity level
- **Food Database Management**: Create, store, and search for both basic and composite foods
- **Daily Food Logging**: Track your daily food intake with servings
- **Nutrition Summaries**: View daily calorie consumption against target goals
- **BMR Calculation**: Calculate your Basal Metabolic Rate using industry-standard formulas
- **Persistent Storage**: All data is saved between sessions using JSON files

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher

## Installation

1. **Clone the repository**:
   Clone the YADA repository
   ```bash
   git clone https://github.com/mayank3135432/diet_manager-cli.git
   cd diet_manager-cli
   ```
2. **Initialise data (optional)**
   ```bash
   mv -t yada database.json
   ```
3. **Build the project**:
   Use Maven to clean and build the project. This will compile the source code, run tests, and package the application into a JAR file.
   ```bash
   cd yada
   mvn clean package
   ```

4. **Run the application**:
   Execute the packaged JAR file using the Java runtime. This will start the YADA console application.
   ```bash
   java -jar target/diet-assistant-1.0-SNAPSHOT.jar
   ```
