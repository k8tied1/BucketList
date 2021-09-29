package com.techelevator.dao;

import com.techelevator.model.VacaPlanDTO;

import java.util.List;

public interface VacationPlanDao {
    VacaPlanDTO addVacationPlan(VacaPlanDTO plan, int loggedInUserId);
    void markVacationPlanComplete(int planId);
    List<VacaPlanDTO> getVacationPlansForCreator(int userid);
    List<VacaPlanDTO> getAllVacationPlansForUser(int userid);
}
