package app.service;

import app.model.MealPlan;
import app.model.MealType;
import app.repository.MealPlanRepository;
import app.web.dto.MealPlanRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MealPlanServiceUTest {

    @Mock
    private MealPlanRepository mealPlanRepository;

    @InjectMocks
    private MealPlanService mealPlanService;




    @Test
    public void whenAddMealPlan_thenSaveMealPlanSuccessfully(){


               MealPlanRequest request = MealPlanRequest.builder()
                .userId(UUID.randomUUID())
                .mealName("Breakfast Pizza")
                .mealType(MealType.BREAKFAST)
                 .plannedDate(LocalDate.now())
                .calories(500)
                .recipeId(UUID.randomUUID())
                .build();

        MealPlan savedMealPlan = MealPlan.builder()
                .id(UUID.randomUUID())
                .userId(request.getUserId())
                .mealName("Breakfast Pizza")
                .build();

        when(mealPlanRepository.save(any(MealPlan.class))).thenReturn(savedMealPlan);


        MealPlan result = mealPlanService.addMealPlan(request);


        assertThat(result).isNotNull();
        assertThat(result.getMealName()).isEqualTo("Breakfast Pizza");
        assertThat(result.getUserId()).isEqualTo(request.getUserId());
        verify(mealPlanRepository).save(any(MealPlan.class));
    }



}
