package ru.kafkaspark.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kafkaspark.model.Limit;

public interface LimitRepository extends JpaRepository<Limit, Integer> {
}
