package com.fitness.activity.repo;

import com.fitness.activity.dto.ActivityResponse;
import com.fitness.activity.entity.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {

    List<Activity> findByUserId(Long userId);

    boolean existsByUserId(Long userId);
}
