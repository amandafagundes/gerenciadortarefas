package com.strider.desafio.repository;

import com.strider.desafio.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface TaskRepository extends JpaRepository<Task, Integer> {

    List<Task> findByStatus(String status);
}
