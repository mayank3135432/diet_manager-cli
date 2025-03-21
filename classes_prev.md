# Classes and Relationships

## Food Database

### Food (AbstractClass)

- `identifier`: String
- `keywords`: List<String>
- `getCalories()`: int

### BasicFood (Class, extends Food)

- `identifier`: String
- `keywords`: List<String>
- `calories`: int
- `getCalories()`: int
- `getKeywords()`: List<String>

### CompositeFood (Class, extends Food)

- `name`: String
- `ingredients`: List<Food> (aggregation of BasicFood & CompositeFood)
- `getCalories()`: int (sum of ingredient calories)
- `addIngredient(Food, servings: int)`

### FoodDatabase (Class)

- `foods`: List<Food>
- `addBasicFood(identifier: String, keywords: List<String>, calories: int)`
- `addCompositeFood(name: String, ingredients: List<Food>)`
- `searchFood(keywords: List<String>): List<Food>`
- `saveDatabase()`
- `loadDatabase()`

## Daily Logs

### LogEntry (Class)

- `date`: Date
- `food`: Food
- `servings`: int
- `getTotalCalories()`: int

### DailyLog (Class)

- `entries`: List<LogEntry> (composition with LogEntry)
- `addFood(food: Food, servings: int)`
- `removeFood(food: Food)`
- `undo()`
- `saveLog()`
- `loadLog()`

## Diet Goal Profile

### UserProfile (Class)

- `gender`: String
- `height`: double
- `weight`: double
- `age`: int
- `activityLevel`: String
- `setWeight(weight: double)`
- `setActivityLevel(level: String)`

### CalorieCalculator (Interface)

- `calculateTargetCalories(user: UserProfile): int`

### BMRCalculator (Class, Implements CalorieCalculator)

- `calculateTargetCalories(user: UserProfile): int`

### AlternateCalculator (Class, Implements CalorieCalculator)

- `calculateTargetCalories(user: UserProfile): int`

## Relationships

- **Aggregation**: CompositeFood aggregates multiple Food objects.
- **Composition**: DailyLog has a strong ownership of LogEntry objects.
- **Association**: UserProfile has an association with CalorieCalculator (Strategy Pattern for flexible calculations).
- **Generalization**: BMRCalculator and AlternateCalculator both implement CalorieCalculator.
