package app.service;

import app.exceptions.MealPlanNotFoundException;
import app.exceptions.UnauthorizedAccessException;
import app.model.MealPlan;
import app.repository.MealPlanRepository;
import app.web.dto.MealPlanRequest;
import app.web.dto.MealPlanResponse;
import app.web.mapper.MealPlanMapper;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


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


    public List<MealPlan> getWeeklyMealPlans(UUID userId, LocalDate weekStart) {
        LocalDate weekEnd = weekStart.plusDays(6);
        List<MealPlan> weekPlans = mealPlanRepository.findWeeklyPlans(userId, weekStart, weekEnd);

        log.info("Found {} plans for user {} starting {}", weekPlans.size(), userId, weekStart);

        return weekPlans;
    }

    public void deleteMealPlan(UUID mealPlanId, UUID userId) {
        MealPlan mealPlan = mealPlanRepository.findById(mealPlanId)
                .orElseThrow(() -> new MealPlanNotFoundException("Meal plan not found"));

        if (!mealPlan.getUserId().equals(userId)) {
            throw new UnauthorizedAccessException("You can only delete your own meal plans");
        }

        mealPlan.setDeleted(true);
        mealPlanRepository.save(mealPlan);

        log.info("Deleted meal plan [{}] for user [{}]", mealPlanId, userId);
    }


}