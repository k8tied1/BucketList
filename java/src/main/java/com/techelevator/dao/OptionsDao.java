package com.techelevator.dao;

import com.techelevator.model.Options;

import java.util.List;

public interface OptionsDao {
    List<Options> getExpenseLevels();
    List<Options> getTimeOfYearOptions();
}
