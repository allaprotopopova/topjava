package ru.javawebinar.topjava.service;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.DataJpaProfileResolver;


@ActiveProfiles(resolver = DataJpaProfileResolver.class)
public class MealServiceDataJpaTest extends MealServiceTest {

}
