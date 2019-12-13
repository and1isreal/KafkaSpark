package ru.kafkaspark.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kafkaspark.model.Limit;
import ru.kafkaspark.repository.LimitRepository;

import java.util.List;

@RestController
@RequestMapping("/limits")
public class LimitApi {

    @Autowired
    private LimitRepository limitRepository;


    @GetMapping
    public List<Limit> getLimits() {
        return limitRepository.findAll();
    }

}
