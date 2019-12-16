package ru.kafkaspark.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kafkaspark.model.Limit;

import java.util.Optional;

public interface LimitRepository extends JpaRepository<Limit, Integer> {
    Optional<Limit> findLimitByName(String name);
}
