package com.techelevator.dao;

import com.techelevator.model.Destination;
import com.techelevator.model.Options;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcOptionsDao implements OptionsDao{

     private JdbcTemplate template;
     public JdbcOptionsDao(JdbcTemplate template){
         this.template = template;
     }

    @Override
    public List<Options> getExpenseLevels() {
        List<Options> list = new ArrayList<>();
        String sql = "SELECT expense_id, description "+
                "FROM expense; ";
        SqlRowSet results = template.queryForRowSet(sql);
        while (results.next()){
            Options o = new Options(results.getInt("expense_id"), results.getString("description"));
            list.add(o);
        }
        return list;
    }

    @Override
    public List<Options> getTimeOfYearOptions() {
        List<Options> list = new ArrayList<>();
        String sql = "SELECT toy_id, description "+
                "FROM time_of_year; ";
        SqlRowSet results = template.queryForRowSet(sql);
        while (results.next()){
            Options o = new Options(results.getInt("toy_id"), results.getString("description"));
            list.add(o);
        }
        return list;
    }
}
