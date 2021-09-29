package com.techelevator.dao;

import com.techelevator.model.Destination;

import java.util.List;

public interface DestinationDao {
    Destination createDestination(Destination d);
    List<Destination> getDestinationsForUser(int user_id);
    boolean updateDestination(Destination d);
    boolean deleteDestination(int destination_id);

    Destination generateRandomDestination(List<Integer> expense, List<Integer> timeOfYear, List<String> guests,String passport);
}
