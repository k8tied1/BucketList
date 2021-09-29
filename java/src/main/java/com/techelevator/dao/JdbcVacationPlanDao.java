package com.techelevator.dao;

import com.techelevator.model.Destination;
import com.techelevator.model.VacaPlanDTO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcVacationPlanDao implements VacationPlanDao{

    private JdbcTemplate template;
    public JdbcVacationPlanDao (JdbcTemplate template){
        this.template = template;
    }

    @Override
    public VacaPlanDTO addVacationPlan(VacaPlanDTO plan, int loggedInUserId) {

        //add the vacation plan
        String sql = "INSERT INTO vacation_plan(creator_id,destination_id) "+
                "VALUES (?,?) returning vacation_plan_id;";
        try {
            int id = template.queryForObject(sql, Integer.class, plan.getDestination().getUserId(),plan.getDestination().getDestinationId());
            plan.setVacationPlanId(id);
        }
        catch (Exception e){
            System.out.println("Error inserting vacation plan ");
            System.out.println(e.getMessage());
            throw e;
        }

        //add the guest list
        String sqlGuest = "INSERT INTO vacation_invites(vacation_plan_id,user_id) "+
                "VALUES (?,?) ";
        try {
            template.update(sqlGuest, plan.getVacationPlanId(),loggedInUserId);
        }
        catch (Exception e){
            System.out.println("Error adding guests to vacation plan ");
            System.out.println(e.getMessage());

            throw e;
        }

        return plan;
    }

    @Override
    public void markVacationPlanComplete(int planId) {

    }

    @Override
    public List<VacaPlanDTO> getVacationPlansForCreator(int userid) {
        return null;
    }

    @Override
    public List<VacaPlanDTO> getAllVacationPlansForUser(int userid) {
        List<VacaPlanDTO> list = new ArrayList<>();
        String sql = "WITH Include_VPs AS ( "+
                "   SELECT vp.vacation_plan_id as vpid " +
                "   FROM vacation_plan vp " +
                "   LEFT JOIN vacation_invites vi ON vp.vacation_plan_id = vi.vacation_plan_id " +
                "   WHERE vp.creator_id = ? OR vi.user_id=? )"+
                " SELECT vacation_plan_id, creator_id, status,  " +
                "   d.destination_id, user_id, location_name,passport_required,expense_id,time_of_year_id,completed" +
                " FROM vacation_plan vp " +
                " INNER JOIN Include_VPs ON vpid = vp.vacation_plan_id " +
                " INNER JOIN destinations d on d.destination_id=vp.destination_id" ;

        SqlRowSet results  = template.queryForRowSet(sql,userid,userid);
        while (results.next()){
            Destination d = new Destination(
                    results.getInt("destination_id"),
                    results.getInt("user_id"),
                    results.getString("location_name"),
                    results.getBoolean("passport_required"),
                    results.getInt("expense_id"),
                    results.getInt("time_of_year_id"),
                    results.getBoolean("completed")
            );
            VacaPlanDTO vacaPlanDTO = new VacaPlanDTO();
            vacaPlanDTO.setVacationPlanId(results.getInt("vacation_plan_id"));
            vacaPlanDTO.setCreator_id(results.getInt("creator_id"));
            vacaPlanDTO.setStatus((results.getString("status")));
            vacaPlanDTO.setDestination(d);
            list.add(vacaPlanDTO);

        }
        //get the guests list
        for(VacaPlanDTO vp : list){
            List<String> guests= new ArrayList<>();
            sql = "SELECT username " +
                    "FROM users " +
                    "INNER JOIN vacation_invites ON users.user_id = vacation_invites.user_id " +
                    "WHERE vacation_invites.vacation_plan_id = ?";
             results  = template.queryForRowSet(sql,vp.getVacationPlanId());
             while(results.next()){
                 guests.add(results.getString("username"));
             }
             vp.setGuests(guests);
        }
        return list;
    }
}
