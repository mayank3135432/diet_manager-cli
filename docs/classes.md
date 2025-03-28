@startuml Diet Assistant Class Diagram

' Abstract class and Interface
abstract class Food {
  +String identifier
  +List<String> keywords
  +int getCalories()
}

interface CalorieCalculator {
  +int calculateTargetCalories(UserProfile user)
}

' Food hierarchy
class BasicFood extends Food {
  +String identifier
  +List<String> keywords
  +int calories
  +int getCalories()
  +List<String> getKeywords()
}

class CompositeFood extends Food {
  +String name
  +List<Food> ingredients
  +int getCalories()
  +void addIngredient(Food food, int servings)
}

' Database management
class FoodDatabase {
  +List<Food> foods
  +void addBasicFood(String identifier, List<String> keywords, int calories)
  +void addCompositeFood(String name, List<Food> ingredients)
  +List<Food> searchFood(List<String> keywords)
  +void saveDatabase()
  +void loadDatabase()
}

' Logging classes
class LogEntry {
  +Date date
  +Food food
  +int servings
  +int getTotalCalories()
}

class DailyLog {
  +List<LogEntry> entries
  +void addFood(Food food, int servings)
  +void removeFood(Food food)
  +void undo()
  +void saveLog()
  +void loadLog()
}

' User profile and calculators
class UserProfile {
  +String gender
  +double height
  +double weight
  +int age
  +String activityLevel
  +void setWeight(double weight)
  +void setActivityLevel(String level)
}

class BMRCalculator implements CalorieCalculator {
  +int calculateTargetCalories(UserProfile user)
}

class AlternateCalculator implements CalorieCalculator {
  +int calculateTargetCalories(UserProfile user)
}

' Relationships
CompositeFood o-- "0..*" Food : aggregates
DailyLog *-- "0..*" LogEntry : composition
UserProfile --> CalorieCalculator : uses
FoodDatabase --> "0..*" Food : manages
LogEntry --> "1" Food : references

@enduml