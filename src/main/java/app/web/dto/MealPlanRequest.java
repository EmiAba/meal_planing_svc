package app.web.dto;

import app.model.MealType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.FutureOrPresent;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
public class MealPlanRequest {

    @NotNull(message = "User ID is required")
    private UUID userId;

    @NotBlank(message = "Meal name is required")
    private String mealName;

    @NotNull(message = "Meal type is required")
    private MealType mealType;

    @NotNull(message = "Planned date is required")
    @FutureOrPresent(message = "Cannot plan meals in the past")
    private LocalDate plannedDate;

    private Integer calories;

    @NotNull(message = "Recipe ID is required")
    private UUID recipeId;
}