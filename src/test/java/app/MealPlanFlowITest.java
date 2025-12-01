package app;

import app.model.MealType;
import app.web.dto.MealPlanRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class MealPlanFlowITest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void mealPlanFullFlow_shouldWork() throws Exception {
        UUID userId = UUID.randomUUID();
        LocalDate weekStart = LocalDate.of(2026, 1, 5);


        MealPlanRequest request = MealPlanRequest.builder()
                .userId(userId)
                .mealName("Healthy Breakfast")
                .mealType(MealType.BREAKFAST)
                .plannedDate(weekStart.plusDays(1))
                .recipeId(UUID.randomUUID())
                .calories(400)
                .build();

        mockMvc.perform(post("/api/v1/meal-plans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());


        mockMvc.perform(get("/api/v1/meal-plans/weekly")
                        .param("userId", userId.toString())
                        .param("weekStart", weekStart.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].mealName").value("Healthy Breakfast"))
                .andExpect(jsonPath("$[0].mealType").value("BREAKFAST"));
    }
}