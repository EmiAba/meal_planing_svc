package app.web.mapper;

import app.model.MealPlan;
import app.web.dto.MealPlanResponse;

public class MealPlanMapper {

    public static MealPlanResponse toResponse(MealPlan mealPlan) {
        return MealPlanResponse.builder()
                .id(mealPlan.getId())
                .userId(mealPlan.getUserId())
                .mealName(mealPlan.getMealName())
                .mealType(mealPlan.getMealType())
                .plannedDate(mealPlan.getPlannedDate())
                .calories(mealPlan.getCalories())
                .recipeId(mealPlan.getRecipeId())
                .build();
    }
}