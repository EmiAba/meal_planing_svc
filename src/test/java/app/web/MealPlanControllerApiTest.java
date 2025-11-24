package app.web;

import app.model.MealPlan;
import app.model.MealType;
import app.service.MealPlanService;
import app.web.dto.MealPlanRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MealPlanController.class)
@ActiveProfiles("test")
public class MealPlanControllerApiTest {

    @MockitoBean
    private MealPlanService mealPlanService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getWeeklyMealPlans_shouldReturn200AndMealPlansList() throws Exception {
        UUID userId = UUID.randomUUID();
        LocalDate weekStart = LocalDate.of(2025, 11, 24);
        MealPlan mealPlan = createMealPlan(userId);

        when(mealPlanService.getWeeklyMealPlans(userId, weekStart))
                .thenReturn(List.of(mealPlan));

        MockHttpServletRequestBuilder httpRequest = get("/api/v1/meal-plans/weekly")
                .param("userId", userId.toString())
                .param("weekStart", weekStart.toString());

        mockMvc.perform(httpRequest)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(mealPlanService, times(1)).getWeeklyMealPlans(userId, weekStart);
    }

    @Test
    void addMealPlan_withValidData_shouldReturn201() throws Exception {
        MealPlanRequest dto = MealPlanRequest.builder()
                .userId(UUID.randomUUID())
                .mealName("Lunch")
                .mealType(MealType.LUNCH)
                .plannedDate(LocalDate.of(2025, 11, 24))
                .recipeId(UUID.randomUUID())
                .calories(500)
                .build();

        MealPlan mealPlan = createMealPlan(dto.getUserId());

        when(mealPlanService.addMealPlan(any())).thenReturn(mealPlan);

        MockHttpServletRequestBuilder request = post("/api/v1/meal-plans")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(dto));

        mockMvc.perform(request)
                .andExpect(status().isCreated());

        verify(mealPlanService).addMealPlan(any());
    }


    private MealPlan createMealPlan(UUID userId) {
        return MealPlan.builder()
                .id(UUID.randomUUID())
                .userId(userId)
                .mealName("Lunch")
                .mealType(MealType.LUNCH)
                .recipeId(UUID.randomUUID())
                .plannedDate(LocalDate.now())
                .calories(500)
                .deleted(false)
                .build();
    }
}