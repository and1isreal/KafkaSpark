package ru.kafkaspark.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
import ru.kafkaspark.model.Limit;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@DataJpaTest
public class LimitRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private LimitRepository limitRepository;

    @Test
    public void whenFindByName_thenReturnEmployeeLimit() {
        Limit testMinLimit = new Limit();
        testMinLimit.setId(1);
        testMinLimit.setName("min");
        entityManager.persist(testMinLimit);
        entityManager.flush();

        // when
        Optional<Limit> found = limitRepository.findLimitByName(testMinLimit.getName());

        // then
        found.ifPresent(limit -> assertThat(limit.getName().equals(testMinLimit.getName())));
    }
}
