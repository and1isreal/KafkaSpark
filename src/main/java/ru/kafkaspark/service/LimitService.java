package ru.kafkaspark.service;

import ru.kafkaspark.model.Limit;

import java.util.Optional;

public interface LimitService {
    Optional<Limit> getLimitByName(String name);
    void updateLimit(Limit limit);
}
