package app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="meal_plans")
public class MealPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull
    @Column(nullable = false)
    private UUID userId;

    @NotBlank
    @Column(nullable = false)
    private String mealName;

    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MealType mealType;

    @NotNull
    @Column(nullable = false)
    private LocalDate plannedDate;

     private Integer calories;

    @NotNull
    @Column(nullable = false)
    private UUID recipeId;


   private boolean deleted = false;
}