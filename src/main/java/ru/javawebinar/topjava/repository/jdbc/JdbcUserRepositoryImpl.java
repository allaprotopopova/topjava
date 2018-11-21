package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
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
import java.util.*;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepositoryImpl implements UserRepository {

    private static final ResultSetExtractor<List<User>> RESULT_SET_EXTRACTOR = new UserRowmapper();

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
        List<Role> roles = new ArrayList<>(user.getRoles());
        if (!user.isNew()) {
            jdbcTemplate.execute("delete from user_roles where user_id=" + user.getId());
        }
        jdbcTemplate.batchUpdate("Insert into user_roles(user_id, role) values (?, ?)", new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setInt(1, user.getId());
                ps.setString(2, roles.get(i).name());

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
        List<User> users = jdbcTemplate.query("SELECT u.*, u_r.role FROM users u  LEFT JOIN user_roles u_r on u.id = u_r.user_id WHERE id=?", RESULT_SET_EXTRACTOR, id);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public User getByEmail(String email) {
        List<User> users = jdbcTemplate.query("SELECT u.*, u_r.role FROM users u  LEFT JOIN user_roles u_r on u.id = u_r.user_id WHERE email=?", RESULT_SET_EXTRACTOR, email);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query("SELECT u.*, u_r.role FROM users u  LEFT JOIN user_roles u_r on u.id = u_r.user_id ORDER BY u.name, u.email", RESULT_SET_EXTRACTOR);
    }

    private static class UserRowmapper implements ResultSetExtractor<List<User>> {
        private Map<Integer, User> users = new LinkedHashMap<>();


        @Override
        public List<User> extractData(ResultSet rs) throws SQLException {
            users.clear();
            BeanPropertyRowMapper<User> mapper = new BeanPropertyRowMapper<>(User.class);
            while (rs.next()) {
                User userExceptRoles = mapper.mapRow(rs, rs.getRow());
                Role role = Role.valueOf(rs.getString("role"));
                userExceptRoles.setRoles(Collections.singleton(role));

                User user = users.computeIfAbsent(userExceptRoles.getId(), u -> userExceptRoles);
                user.getRoles().add(role);
            }

            return new ArrayList<>(new ArrayList<>(users.values()));
        }
    }

}



