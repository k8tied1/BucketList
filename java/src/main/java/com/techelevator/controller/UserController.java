package com.techelevator.controller;

import com.techelevator.dao.UserDao;
import com.techelevator.dao.UserVisiblityDao;
import com.techelevator.model.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@CrossOrigin
@PreAuthorize("isAuthenticated()")
public class UserController {
    private UserDao udao;
    private UserVisiblityDao uvdao;

    public UserController(UserDao udao, UserVisiblityDao uvdao){
        this.udao = udao;
        this.uvdao = uvdao;
    }

    /**
     * Get the List of all users that the current logged in user has access to see
     * @return List of Users
     */
    @RequestMapping(value = "/users", method = RequestMethod.GET)
    List<User> getUserList(Principal p){
        return udao.findAll(udao.findIdByUsername(p.getName()));
    }

    /**
     * Get the List of all users who have access to see the current user
     * @return List of Users
     */
    @RequestMapping(value = "/userswhocanseeme", method = RequestMethod.GET)
    List<User> getUserListAccessToMe(Principal p){
        return udao.findAllAccessToMe(udao.findIdByUsername(p.getName()));
    }

    /**
     * Returns a list of all the users
     * @param p
     * @return
     */
    @RequestMapping(value = "/allusers", method = RequestMethod.GET)
    List<User> getAllusers(Principal p){
        return udao.findAll();
    }

    /**
     * Grants access to the user in the request body to select the current user
     * for vacation plans
     * @param toUser
     * @param  current logged in user - auto filled by the token
     */
    @RequestMapping(value = "/users/grant_access/{toUser}", method = RequestMethod.POST)
    void giveUserAccessToSeeMe(@PathVariable int toUser, Principal p){
        uvdao.grantAccessTo(toUser, udao.findIdByUsername(p.getName()));
    }

}
