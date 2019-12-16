package ru.kafkaspark.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class UpdateLimitsServiceTest {

    private UpdateLimitsService updateLimitsService;

    @Before
    public void init() {
        updateLimitsService = mock(UpdateLimitsService.class);
    }

    @Test
    public void updateLimitsTest() {
        verify(updateLimitsService, times(0)).updateLimits();
    }

    @Test
    public void runTest() {
        verify(updateLimitsService, times(0)).run();
    }
}
