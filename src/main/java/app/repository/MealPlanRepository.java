package app.repository;

import app.model.MealPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface MealPlanRepository extends JpaRepository<MealPlan, UUID> {

    @Query("SELECT m FROM MealPlan m " +
            "WHERE m.userId = :userId " +
            "AND m.plannedDate BETWEEN :startDate AND :endDate " +
            "AND m.deleted = false " +
            "ORDER BY m.plannedDate, m.mealType")
    List<MealPlan> findWeeklyPlans(
            @Param("userId") UUID userId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    }