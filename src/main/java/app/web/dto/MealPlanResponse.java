package app.web.dto;

import app.model.MealType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MealPlanResponse {
    private UUID id;
    private UUID userId;
    private String mealName;
    private MealType mealType;
    private LocalDate plannedDate;
    private Integer calories;
    private UUID recipeId;
}