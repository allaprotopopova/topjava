package ru.javawebinar.topjava.service;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.DataJpaProfileResolver;
import ru.javawebinar.topjava.Profiles;


@ActiveProfiles(resolver = DataJpaProfileResolver.class)
public class MealServiceDataJpaTest extends MealServiceTest {

}
