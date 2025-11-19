package app.service;

import app.exceptions.UnauthorizedAccessException;
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
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
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



    @Test
    public void whenGetWeeklyMealPlans_thenReturnPlansForWeek(){

        UUID userId = UUID.randomUUID();
        LocalDate weekStart= LocalDate.now();
        LocalDate weekEnd= weekStart.plusDays(6);

        MealPlan plan1 = MealPlan.builder().mealName("Breakfast").build();
        MealPlan plan2 = MealPlan.builder().mealName("Lunch").build();

       when(mealPlanRepository.findWeeklyPlans(userId, weekStart, weekEnd)).thenReturn(List.of(plan1, plan2));


        List<MealPlan> result= mealPlanService.getWeeklyMealPlans(userId, weekStart);

        assertThat(result).hasSize(2);
        assertThat(result).contains(plan1, plan2);
        verify(mealPlanRepository).findWeeklyPlans(userId, weekStart, weekEnd);

    }


    @Test
    public void whenNonOwnerDeletesMealPlan_thenThrowException(){
        UUID ownerId = UUID.randomUUID();
        UUID mealPlanId = UUID.randomUUID();
        UUID nonOwnerId = UUID.randomUUID();

        MealPlan mealPlan = MealPlan.builder()
                .id(mealPlanId)
                .userId(ownerId)
                .mealName("Breakfast Pizza")
                .build();


        when(mealPlanRepository.findById(mealPlanId)).thenReturn(Optional.of(mealPlan));

        assertThrows(UnauthorizedAccessException.class, () -> mealPlanService.deleteMealPlan(mealPlanId, nonOwnerId));

        }


    @Test
    public void whenOwnerDeletesMealPlan_thenSetDeletedToTrue(){
        UUID ownerId = UUID.randomUUID();
        UUID mealPlanId = UUID.randomUUID();

        MealPlan mealPlan = MealPlan.builder()
                .id(mealPlanId)
                .userId(ownerId)
                .mealName("Breakfast Pizza")
                .build();

        when(mealPlanRepository.findById(mealPlanId)).thenReturn(Optional.of(mealPlan));

        mealPlanService.deleteMealPlan(mealPlanId, ownerId);

        assertThat(mealPlan.isDeleted()).isTrue();
        verify(mealPlanRepository).save(mealPlan);

    }

}
