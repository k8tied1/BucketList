package com.techelevator.dao;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.techelevator.model.User;

@Service
public class JdbcUserDao implements UserDao {

    private JdbcTemplate jdbcTemplate;

    public JdbcUserDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int findIdByUsername(String username) {
        return jdbcTemplate.queryForObject("select user_id from users where username = ?", int.class, username);
    }

	@Override
	public User getUserById(Long userId) {
		String sql = "SELECT * FROM users WHERE user_id = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
		if(results.next()) {
			return mapRowToUser(results);
		} else {
			throw new RuntimeException("userId "+userId+" was not found.");
		}
	}

    @Override
    public List<User> findAll(int currentLoggedInUser) {
        List<User> users = new ArrayList<>();

        String sql = "select user_id, username, 'No' as password_hash, email, role " +
                " FROM users u " +
                " INNER JOIN users_visibility uv ON u.user_id = uv.can_see_user " +
                " WHERE user_current=?";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql,currentLoggedInUser);
        while(results.next()) {
            User user = mapRowToUser(results);
            users.add(user);
        }

        return users;
    }

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();

        String sql = "select user_id, username, 'No' as password_hash, email, role " +
                " FROM users ;";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        while(results.next()) {
            User user = mapRowToUser(results);
            users.add(user);
        }

        return users;
    }

    @Override
    public User findByUsername(String username) throws UsernameNotFoundException {

        String sql = "select user_id, username, password_hash, email, role " +
                " FROM users " +
                " WHERE LOWER(username)=?";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql,username.toLowerCase(Locale.ROOT));
        if (results.next()) {
            return mapRowToUser(results);
        }
        throw new UsernameNotFoundException("User " + username + " was not found.");
    }

    @Override
    public boolean create(String username, String password, String role, String email) {
        boolean userCreated = false;

        // create user
        String insertUser = "insert into users (username,password_hash,role,email) values(?,?,?,?)";
        String password_hash = new BCryptPasswordEncoder().encode(password);
        String ssRole = "ROLE_" + role.toUpperCase();

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        String id_column = "user_id";
        userCreated = jdbcTemplate.update(con -> {
                    PreparedStatement ps = con.prepareStatement(insertUser, new String[]{id_column});
                    ps.setString(1, username);
                    ps.setString(2, password_hash);
                    ps.setString(3, ssRole);
                    ps.setString(4, email);
                    return ps;
                }
                , keyHolder) == 1;
        int newUserId = (int) keyHolder.getKeys().get(id_column);

        return userCreated;
    }

    @Override
    public List<User> findAllAccessToMe(int idByUsername) {
        List<User> users = new ArrayList<>();

        String sql = "select user_id, username, 'No' as password_hash, email, role " +
                " FROM users u " +
                " INNER JOIN users_visibility uv ON u.user_id = uv.user_current " +
                " WHERE uv.can_see_user=?";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql,idByUsername);
        while(results.next()) {
            User user = mapRowToUser(results);
            users.add(user);
        }

        return users;
    }


    private User mapRowToUser(SqlRowSet rs) {
        User user = new User();
        user.setId(rs.getLong("user_id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password_hash"));
        user.setEmail(rs.getString("email"));
        user.setAuthorities(rs.getString("role"));
        user.setActivated(true);
        return user;
    }
}
