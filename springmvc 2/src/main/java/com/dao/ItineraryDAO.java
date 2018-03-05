package com.dao;

import com.domain.Itinerary;
import com.domain.Search;
import com.domain.Train;
import com.domain.User;

import java.util.List;

public interface ItineraryDAO {

    public List<Train> getTrainsBySearch(Search search);
    public List<Train> getTrainsById(List<Integer> trainIds);
    String insertIntoUsers(User user);
    User checkLoginCredentials(User user);
    int getBookingCount();
    void updateBookingCount(int booking_id);
    void bookATicket(Itinerary itinerary, int booking_id);
    List<Itinerary> getUserBookings(String email);
    void cancelTicket(int booking_id);
}
