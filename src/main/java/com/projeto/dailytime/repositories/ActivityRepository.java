package com.projeto.dailytime.repositories;

import com.projeto.dailytime.domain.activity.Activity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActivityRepository extends JpaRepository<Activity, Long> {
    List<Activity> findAllByTaskId(Long id);
}
