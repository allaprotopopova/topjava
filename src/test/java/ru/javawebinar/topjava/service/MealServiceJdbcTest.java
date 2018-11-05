package ru.javawebinar.topjava.service;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.JdbcProfileResolver;


@ActiveProfiles(resolver = JdbcProfileResolver.class)
public class MealServiceJdbcTest extends MealServiceTest {

}
