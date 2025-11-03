package app.web;

import app.model.MealPlan;
import app.service.MealPlanService;
import app.web.dto.MealPlanRequest;
import app.web.dto.MealPlanResponse;
import app.web.mapper.MealPlanMapper;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@Slf4j
@RestController
@RequestMapping("/api/v1/meal-plans")
public class MealPlanController {

    private final MealPlanService mealPlanService;

    @Autowired
    public MealPlanController(MealPlanService mealPlanService) {
        this.mealPlanService = mealPlanService;
    }


    @PostMapping
    public ResponseEntity<MealPlanResponse> addMealPlan(@Valid @RequestBody MealPlanRequest request) {
        MealPlan created = mealPlanService.addMealPlan(request);

        MealPlanResponse response = MealPlanMapper.toResponse(created);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @DeleteMapping("/{mealPlanId}")
    public ResponseEntity<Void> deleteMealPlan( @PathVariable UUID mealPlanId, @RequestParam UUID userId) {
        mealPlanService.deleteMealPlan(mealPlanId, userId);
        return ResponseEntity.noContent().build();
    }




}