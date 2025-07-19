package com.projeto.dailytime.repositories;

import com.projeto.dailytime.domain.task.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {

    Page<Task> findAllByUserId(String userId, Pageable pageable);

}
