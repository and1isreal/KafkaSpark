package ru.kafkaspark.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kafkaspark.model.Limit;
import ru.kafkaspark.repository.LimitRepository;
import ru.kafkaspark.service.LimitService;

import java.util.Optional;

@Service
public class LimitServiceImpl implements LimitService {

    @Autowired
    private LimitRepository limitRepository;

    @Override
    public Optional<Limit> getLimitByName(String name) {
        return limitRepository.findLimitByName(name);
    }

    @Override
    public void updateLimit(Limit limit) {
        limitRepository.save(limit);
    }
}
