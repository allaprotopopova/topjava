package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.Profiles;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@Profile(Profiles.POSTGRES_DB)
public class JdbcMealReposytoryPostgresImpl extends JdbcMealRepositoryTemplate {
    public JdbcMealReposytoryPostgresImpl(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(jdbcTemplate, namedParameterJdbcTemplate);
    }

    @Override
    List<?> convertDateTime(LocalDateTime dateTime) {
        return List.of(dateTime);
    }


}
