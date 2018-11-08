package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface CrudMealRepository extends JpaRepository<Meal, Integer> {

    @Override
    @Transactional
    Meal save(Meal meal);

    @Override
    Optional<Meal> findById(Integer integer);

    @Transactional
    @Modifying
    @Query("DELETE FROM Meal m WHERE m.id=?1 AND m.user.id=?2")
    int delete(int id, int userId);

    List<Meal> findAllByUserIdOrderByDateTimeDesc(int userId);

    @Query("SELECT m FROM Meal m JOIN FETCH m.user WHERE m.id=?1 and m.user.id=?2")
    Meal findByIdAndUserId(Integer id, Integer user_id);

    List<Meal> findByUserIdAndDateTimeBetweenOrderByDateTimeDesc(int userId, LocalDateTime startDate, LocalDateTime endDate);

}
