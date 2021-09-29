package com.techelevator.model;

public class Destination {

    private int destinationId;
    private int userId;
    private String locationName;
    private boolean passportRequired;
    private int expense;
    private int timeOfYear;
    private boolean completed;
    private String creatorName;

    public Destination(int destinationId, int user_id, String locationName,
                       boolean passportRequired, int expenseLevel, int timeOfYear,
                       boolean completed){
        this.destinationId = destinationId;
        this.userId = user_id;
        this.locationName = locationName;
        this.passportRequired = passportRequired;
        this.expense = expenseLevel;
        this.timeOfYear = timeOfYear;
        this.completed = completed;
    }

    public Destination(){}

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public boolean isPassportRequired() {
        return passportRequired;
    }

    public void setPassportRequired(boolean passportRequired) {
        this.passportRequired = passportRequired;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public int getExpense() {
        return expense;
    }

    public void setExpense(int expense) {
        this.expense = expense;
    }

    public int getTimeOfYear() {
        return timeOfYear;
    }

    public void setTimeOfYear(int timeOfYear) {
        this.timeOfYear = timeOfYear;
    }

    public int getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(int destinationId) {
        this.destinationId = destinationId;
    }

    public int getUserId() {
        return userId;
    }
    public void setUserId(int id) {
        userId=id;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }
}

