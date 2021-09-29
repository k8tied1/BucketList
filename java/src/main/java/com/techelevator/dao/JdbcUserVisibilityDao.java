package com.techelevator.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class JdbcUserVisibilityDao implements UserVisiblityDao{
    private JdbcTemplate template;

    public JdbcUserVisibilityDao(JdbcTemplate template) {
        this.template = template;
    }

    @Override
    public void grantAccessTo(int userGettingAccess, int userGrantingAccess) {
        String sql = "INSERT INTO users_visibility(user_current,can_see_user) "+
                "VALUES (?,?);";
        template.update(sql,userGettingAccess,userGrantingAccess);

    }
}
