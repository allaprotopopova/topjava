package ru.javawebinar.topjava.service;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.ActiveDbProfileResolver;
import ru.javawebinar.topjava.JdbcProfileResolver;
import ru.javawebinar.topjava.JpaProfileResolver;
import ru.javawebinar.topjava.Profiles;


@ActiveProfiles(resolver = JpaProfileResolver.class)
public class MealServiceJpaTest extends MealServiceTest {

}
