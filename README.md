# Meal Planning Microservice 

A microservice for weekly meal planning, part of the Recipe Buddy application.

## Tech Stack

- Java 17
- Spring Boot 3.4.0
- MySQL
- Port: 8081

## Entity

**MealPlan** - Contains userId, mealName, mealType, plannedDate, calories, recipeId

## API Endpoints

- `POST /api/v1/meal-plans` - Create meal plan
- `GET /api/v1/meal-plans/weekly` - Get weekly meal plans for a user
- `DELETE /api/v1/meal-plans/{id}` - Delete meal plan

## Setup

1. Create database: `meal_planning_db`
2. Configure `application.properties`
3. Run: `mvn spring-boot:run`

## Integration

Main application consumes this microservice using **Feign Client**.

---

**Spring Advanced @ SoftUni - October 2025**
