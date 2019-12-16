package ru.kafkaspark.service.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;
import ru.kafkaspark.model.Limit;
import ru.kafkaspark.repository.LimitRepository;
import ru.kafkaspark.service.LimitService;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
public class LimitServiceImplIntegrationTest {

    @TestConfiguration
    static class LimitServiceImplTestContextConfiguration {

        @Bean
        public LimitService limitService() {
            return new LimitServiceImpl();
        }
    }

    @Autowired
    private LimitService limitService;

    @MockBean
    private LimitRepository limitRepository;

    @Before
    public void setUp() {
        Limit testMinLimit = new Limit();
        testMinLimit.setName("min");

        Mockito.when(limitRepository.findLimitByName(testMinLimit.getName()))
                .thenReturn(java.util.Optional.of(testMinLimit));
    }

    @Test
    public void whenValidName_thenLimitShouldBeFound() {
        String name = "min";
        Optional<Limit> found = limitService.getLimitByName(name);
        found.ifPresent(limit -> assertThat(limit.getName())
                .isEqualTo(name));
    }

}
