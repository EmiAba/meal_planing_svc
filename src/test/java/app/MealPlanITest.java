package app;

import app.model.MealPlan;
import app.model.MealType;
import app.repository.MealPlanRepository;
import app.web.dto.MealPlanRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class MealPlanITest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MealPlanRepository mealPlanRepository;

    @Test
    void mealPlanFullFlow_shouldWork() throws Exception {

        mealPlanRepository.deleteAll();

        UUID userId = UUID.randomUUID();
        LocalDate weekStart = LocalDate.of(2026, 12, 15);

        MealPlanRequest request = MealPlanRequest.builder()
                .userId(userId)
                .mealName("Breakfast")
                .mealType(MealType.BREAKFAST)
                .plannedDate(weekStart.plusDays(2))
                .recipeId(UUID.randomUUID())
                .calories(350)
                .build();

        MockHttpServletRequestBuilder postRequest = post("/api/v1/meal-plans")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request));

        mockMvc.perform(postRequest)
                .andExpect(status().isCreated());

        MockHttpServletRequestBuilder getRequest = get("/api/v1/meal-plans/weekly")
                .param("userId", userId.toString())
                .param("weekStart", weekStart.toString());

        mockMvc.perform(getRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].mealName").value("Breakfast"))
                .andExpect(jsonPath("$[0].calories").value(350));

        MealPlan saved = mealPlanRepository.findAll().get(0);

                MockHttpServletRequestBuilder deleteRequest = delete("/api/v1/meal-plans/" + saved.getId())
                .param("userId", userId.toString());

        mockMvc.perform(deleteRequest)
                .andExpect(status().isNoContent());

        MealPlan deleted = mealPlanRepository.findById(saved.getId()).orElseThrow();
        assertTrue(deleted.isDeleted());
    }
}