package app.service;

import app.model.MealPlan;
import app.repository.MealPlanRepository;
import app.web.dto.MealPlanRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;


@Slf4j
@Service
@Validated
public class MealPlanService {

    private final MealPlanRepository mealPlanRepository;

    @Autowired
    public MealPlanService(MealPlanRepository mealPlanRepository) {
        this.mealPlanRepository = mealPlanRepository;
    }

    public MealPlan addMealPlan(@Valid MealPlanRequest request) {


        MealPlan mealPlan = MealPlan.builder()
                .userId(request.getUserId())
                .mealName(request.getMealName())
                .mealType(request.getMealType())
                .plannedDate(request.getPlannedDate())
                .calories(request.getCalories())
                .recipeId(request.getRecipeId())
                .deleted(false)
                .build();

        MealPlan saved = mealPlanRepository.save(mealPlan);

        log.info("Added meal plan for user [{}]: {} on {}",
                request.getUserId(), request.getMealName(), request.getPlannedDate());


        return saved;
    }

}