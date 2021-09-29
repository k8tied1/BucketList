package com.techelevator.model;

import java.util.List;

public class VacaPlanDTO {
    private int vacationPlanId;
    private List<Integer> expense;
    private List<Integer> timeOfYear;
    private List<String> guests;
    private String passport;
    private Destination destination;
    private String status;
    private int creator_id;

    public VacaPlanDTO(int vacationPlanId, List<Integer> expense, List<Integer> timeOfYear, List<String> guests, String passport, Destination destination, String status, int creator_id) {
        this.vacationPlanId = vacationPlanId;
        this.expense = expense;
        this.timeOfYear = timeOfYear;
        this.guests = guests;
        this.passport = passport;
        this.destination = destination;
        this.status = status;
        this.creator_id = creator_id;
    }

    public VacaPlanDTO(){}

    public List<Integer> getExpense() {
        return expense;
    }

    public void setExpense(List<Integer> expense) {
        this.expense = expense;
    }

    public List<Integer> getTimeOfYear() {
        return timeOfYear;
    }

    public void setTimeOfYear(List<Integer> timeOfYear) {
        this.timeOfYear = timeOfYear;
    }

    public List<String> getGuests() {
        return guests;
    }

    public void setGuests(List<String> guests) {
        this.guests = guests;
    }

    public Destination getDestination() {
        return destination;
    }

    public void setDestination(Destination destinations) {
        this.destination = destinations;
    }

    public int getVacationPlanId() {
        return vacationPlanId;
    }

    public void setVacationPlanId(int vacationPlanId) {
        this.vacationPlanId = vacationPlanId;
    }

    public String getPassport() {
        return passport;
    }

    public void setPassport(String passport) {
        this.passport = passport;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCreator_id() {
        return creator_id;
    }

    public void setCreator_id(int creator_id) {
        this.creator_id = creator_id;
    }
}
