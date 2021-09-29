package com.techelevator.dao;

import com.techelevator.model.Destination;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;


import java.sql.Types;
import java.util.*;

@Component
public class JdbcDestinationDao implements DestinationDao {

    private JdbcTemplate template;
    public JdbcDestinationDao (JdbcTemplate template){
        this.template = template;
    }

    @Override
    public Destination createDestination(Destination d) {
        String sql = "INSERT INTO destinations(location_name,passport_required,expense_id,time_of_year_id,user_id) "+
                     "VALUES (?,?,?,?,?) returning destination_id;";
        try {
            int id = template.queryForObject(sql, Integer.class, d.getLocationName(), d.isPassportRequired(), d.getExpense(), d.getTimeOfYear(),d.getUserId());
            d.setDestinationId(id);
        }
        catch (Exception e){
            System.out.println("Error inserting destination "+d.getDestinationId()+" "+d.getLocationName());
            System.out.println(e.getMessage());
            d=null;
            throw e;
        }
        return d;
    }

    @Override
    public List<Destination> getDestinationsForUser(int user_id) {
        List<Destination> list = new ArrayList<>();
        String sql = "SELECT destination_id, user_id, location_name,passport_required,expense_id,time_of_year_id,completed "+
                     "FROM destinations "+
                     "WHERE user_id = ?";
        SqlRowSet results = template.queryForRowSet(sql,user_id);
        while (results.next()){
            list.add(mapRowToDestination(results,false));
        }
        return list;
    }

    private Destination mapRowToDestination(SqlRowSet results,boolean includeUserName) {
        Destination d = new Destination(
                results.getInt("destination_id"),
                results.getInt("user_id"),
                results.getString("location_name"),
                results.getBoolean("passport_required"),
                results.getInt("expense_id"),
                results.getInt("time_of_year_id"),
                results.getBoolean("completed")
                );
        if (includeUserName) {
            d.setCreatorName(results.getString("username"));
        }
        return d;
    }

    @Override
    public boolean updateDestination(Destination d) {
        String sql = " UPDATE destinations "+
                     " SET location_name=?,passport_required=?,expense_id=?,time_of_year_id=?, completed=?" +
                     " WHERE destination_id=?; ";
        try {
            int count = template.update(sql, d.getLocationName(), d.isPassportRequired(),
                    d.getExpense(), d.getTimeOfYear(), d.isCompleted(),d.getDestinationId());
            return count==1;
        }
        catch (Exception e){
            System.out.println("Error updating destination "+d.getDestinationId()+" "+d.getLocationName());
            System.out.println(e.getMessage());
            return false;
        }

    }

    @Override
    public boolean deleteDestination(int destination_id) {
        String sql = "DELETE from destinations WHERE destination_id = ?;";
        try {
            return template.update(sql,destination_id)==1;
        }
        catch (Exception e){
            System.out.println("Error deleting destination "+destination_id);
            System.out.println(e.getMessage());
            return false;
        }
    }

    private int getAnytimeId(){List<Destination> list = new ArrayList<>();
        String sql = "SELECT toy_id " +
                " FROM time_of_year "+
                " WHERE description = 'Anytime'";
        int result = template.queryForObject(sql,Integer.class);
        return result;
    }
    @Override
    public Destination generateRandomDestination(List<Integer> expense, List<Integer> timeOfYear, List<String> guests, String passport) {
        int anytimeId = getAnytimeId();

        String expenseSql = " expense_id=? OR ";
        expenseSql=expenseSql.repeat(expense.size());
        expenseSql=expenseSql.substring(0, expenseSql.lastIndexOf("OR"));

        String guestsSql = " users.username=? OR ";
        guestsSql=guestsSql.repeat(guests.size());
        guestsSql=guestsSql.substring(0, guestsSql.lastIndexOf("OR"));

        String timeOfYearSql = " time_of_year_id=? OR ";
        timeOfYearSql=timeOfYearSql.repeat(timeOfYear.size());
        timeOfYearSql=timeOfYearSql.substring(0, timeOfYearSql.lastIndexOf("OR"));

        String fromAndWhere = String.format(" FROM destinations " +
                " JOIN users ON destinations.user_id = users.user_id" +
                " WHERE completed=false AND (%s) AND (%s) AND (%s) ", expenseSql, guestsSql,timeOfYearSql);
        if (timeOfYear.contains(anytimeId)){
            fromAndWhere = String.format(" FROM destinations " +
                    " JOIN users ON destinations.user_id = users.user_id" +
                    " WHERE completed=false AND (%s) AND (%s)  ", expenseSql, guestsSql);
        }
        if (passport.toLowerCase().equals("yes")){
            fromAndWhere+=" AND passport_required=TRUE;";
        } else if (passport.toLowerCase().equals("no")){
            fromAndWhere+=" AND passport_required=FALSE;";
        }
        else {
            fromAndWhere+=";";
        }

        String sqlCount = "SELECT COUNT(*) AS matches" + fromAndWhere;

        List<Object> objList = new ArrayList<>();
        objList.addAll(expense);
        objList.addAll(guests);
        if (!timeOfYear.contains(anytimeId)) {
            objList.addAll(timeOfYear);
        }
        Object[] params = objList.toArray();

        int []argTypes = new int[expense.size()+ guests.size()+ (timeOfYear.contains(anytimeId)?0:timeOfYear.size())];
        int index=0;
        for(int x : expense){
            argTypes[index++]=Types.INTEGER;
        }
        for(String x : guests){
            argTypes[index++]=Types.VARCHAR;
        }
        if (!timeOfYear.contains(anytimeId)) {
            for (int x : timeOfYear) {
                argTypes[index++] = Types.INTEGER;
            }
        }

        SqlRowSet result = template.queryForRowSet(sqlCount,params,argTypes);
        if (result.next()){
            System.out.println("Success!");
            int count = result.getInt("matches");
            int randomIndex = new Random().nextInt(count);
            String sql = "SELECT destination_id, users.user_id, username, location_name,passport_required,expense_id,time_of_year_id," +
                    "completed "+  fromAndWhere;

            SqlRowSet results = template.queryForRowSet(sql,params,argTypes);
            index =0;
            while (results.next() ){
                if (index++==randomIndex){
                    return mapRowToDestination(results,true);
                }
            }
        }
        return null;
    }
}
