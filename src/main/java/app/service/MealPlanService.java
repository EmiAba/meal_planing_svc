package app.service;

import app.model.MealPlan;
import app.repository.MealPlanRepository;
import app.web.dto.MealPlanRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;


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
                .build();

        MealPlan savedMealPlan = mealPlanRepository.save(mealPlan);

        log.info("Added meal plan for user [{}]: {} on {}",
                request.getUserId(), request.getMealName(), request.getPlannedDate());


        return savedMealPlan;
    }


    public void deleteMealPlan(UUID mealPlanId, UUID userId) {
        MealPlan mealPlan = mealPlanRepository.findById(mealPlanId)
                .orElseThrow(() -> new RuntimeException("Meal plan not found"));

        if (!mealPlan.getUserId().equals(userId)) {
            throw new RuntimeException("You can only delete your own meal plans");
        }

        mealPlan.setDeleted(true);
        mealPlanRepository.save(mealPlan);

        log.info("Deleted meal plan [{}] for user [{}]", mealPlanId, userId);
    }


}