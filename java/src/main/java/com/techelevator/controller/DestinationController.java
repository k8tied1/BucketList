package com.techelevator.controller;

import com.techelevator.dao.DestinationDao;
import com.techelevator.dao.OptionsDao;
import com.techelevator.dao.UserDao;
import com.techelevator.model.Destination;
import com.techelevator.model.Options;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.security.Principal;
import java.util.List;

@RestController
@CrossOrigin
@PreAuthorize("isAuthenticated()")
public class DestinationController {
    private DestinationDao ddao;
    private UserDao udao;
    private OptionsDao odao;

    public DestinationController(DestinationDao ddao, UserDao udao, OptionsDao odao){
        this.ddao = ddao;
        this.udao = udao;
        this.odao = odao;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "/destination", method = RequestMethod.POST)
    Destination addDestination(@RequestBody Destination d, Principal p){
        d.setUserId(getCurrentUserId(p));
        return ddao.createDestination(d);
    }

    @RequestMapping(value = "/destination", method = RequestMethod.PUT)
    void updateDestination(@RequestBody Destination d, Principal p) throws Exception {
        if (d.getUserId() != getCurrentUserId(p))
            throw new Exception("not allowed");

        ddao.updateDestination(d);
    }

    @RequestMapping(value = "/destination/expenseLevels", method = RequestMethod.GET)
    List<Options> getExpenseLevels(){
       return odao.getExpenseLevels();
    }

    @RequestMapping(value = "/destination/timeOfYearOptions", method = RequestMethod.GET)
    List<Options> getTimeOfYear(){
        return odao.getTimeOfYearOptions();
    }

    @RequestMapping(value = "/destinations", method = RequestMethod.GET)
    List<Destination> getDestinations(Principal p){
        return ddao.getDestinationsForUser(getCurrentUserId(p));
    }

    @RequestMapping(value = "/destination/{id}", method = RequestMethod.DELETE)
    void deleteDestination(@PathVariable int id){
        ddao.deleteDestination(id);
    }

    private int getCurrentUserId(Principal principal) {
        long temp= udao.findByUsername(principal.getName()).getId();
        return (int)temp;
    }


}

