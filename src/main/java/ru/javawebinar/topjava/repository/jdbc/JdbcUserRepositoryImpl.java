package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepositoryImpl implements UserRepository {

    private static final RowMapper<User> ROW_MAPPER = new UserRowMapper();

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    @Autowired
    public JdbcUserRepositoryImpl(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    @Transactional
    public User save(User user) {
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);

        if (user.isNew()) {
//            user.setRoles(Collections.singleton(Role.ROLE_USER));
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
        } else if (namedParameterJdbcTemplate.update(
                "UPDATE users SET name=:name, email=:email, password=:password, " +
                        "registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id", parameterSource) == 0) {
            return null;
        }
        jdbcTemplate.execute("delete from user_roles where user_id="+user.getId());
        jdbcTemplate.batchUpdate("Insert into user_roles(user_id, role) values (?, ?)", new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setInt(1, user.getId());
                ps.setString(2, Role.values()[i].name());
            }

            @Override
            public int getBatchSize() {
                return user.getRoles().size();
            }
        });
        return user;
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        List<User> users = jdbcTemplate.query("SELECT u.*, u_r.role FROM users u  LEFT JOIN user_roles u_r on u.id = u_r.user_id WHERE id=?", ROW_MAPPER, id);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public User getByEmail(String email) {
        List<User> users = jdbcTemplate.query("SELECT u.*, u_r.role FROM users u  LEFT JOIN user_roles u_r on u.id = u_r.user_id WHERE email=?", ROW_MAPPER, email);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public List<User> getAll() {
        List<User> users =  jdbcTemplate.query("SELECT u.*, u_r.role FROM users u  LEFT JOIN user_roles u_r on u.id = u_r.user_id ORDER BY u.name, u.email", ROW_MAPPER);
        Set<User> result = new HashSet<>();
        users.forEach(u -> result.add(u));
        return result.stream().sorted(Comparator.comparing(User::getName).thenComparing(User::getEmail)).collect(Collectors.toList());
    }

}

final class UserRowMapper implements RowMapper<User> {

    private Map<Integer, User> users = new HashMap<>();

    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        String email = rs.getString("email");
        String password = rs.getString("password");
        int caloriesPerDay = rs.getInt("calories_per_day");
        boolean enabled = rs.getBoolean("enabled");
        Date registered = rs.getDate("registered");
        Role role = Role.valueOf(rs.getString("role"));

        User user = users.computeIfAbsent(id, u -> new User(id, name, email, password, caloriesPerDay, enabled, registered, Collections.singleton(role)));

        user.getRoles().add(role);

        return user;
    }
}
