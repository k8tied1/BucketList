package com.techelevator.controller;

import com.techelevator.dao.*;
import com.techelevator.model.User;
import com.techelevator.model.VacaPlanDTO;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@PreAuthorize("isAuthenticated()")
public class VacationPlanController {
    private DestinationDao ddao;
    private UserDao udao;
    private OptionsDao odao;
    private VacationPlanDao vpdao;
    private UserVisiblityDao uvdao;

    public VacationPlanController(DestinationDao ddao, UserDao udao, OptionsDao odao, VacationPlanDao vpdao, UserVisiblityDao uvdao){
        this.ddao = ddao;
        this.udao = udao;
        this.odao = odao;
        this.vpdao = vpdao;
        this.uvdao = uvdao;
    }

    @RequestMapping(value="vacationPlans",method=RequestMethod.GET)
    public List<VacaPlanDTO> getAllMyVacationPlans(Principal p) {
        return vpdao.getAllVacationPlansForUser(getCurrentUserId(p));
    }

    @RequestMapping(value = "/vacationPlan", method = RequestMethod.POST)
    public VacaPlanDTO addVacationPlan(@RequestBody VacaPlanDTO vacationPlan,Principal p){
        if (vacationPlan.getGuests()==null){
            vacationPlan.setGuests(new ArrayList<>());
        }
        vacationPlan.getGuests().add(p.getName());

        //let's make sure that the didn't sneak anyone in that they don't have access to
        List<User> usersAllowed = udao.findAll(udao.findIdByUsername(p.getName()));
        List<String> usernamesAllowed = usersAllowed.stream().map(x -> x.getUsername()).collect(Collectors.toList());

        List<String> guests = vacationPlan.getGuests().stream()
                .distinct()
                .filter(usernamesAllowed::contains)
                .collect(Collectors.toList());

        vacationPlan.setDestination(ddao.generateRandomDestination(vacationPlan.getExpense(),vacationPlan.getTimeOfYear(),guests,vacationPlan.getPassport()));
        return vpdao.addVacationPlan(vacationPlan,getCurrentUserId(p));
    }
    private int getCurrentUserId(Principal principal) {
        long temp= udao.findByUsername(principal.getName()).getId();
        return (int)temp;
    }

}
