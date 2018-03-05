package com.service;

import com.domain.Itinerary;
import com.domain.Search;
import com.domain.User;

import java.util.List;

public interface ItineraryService {

    public List<Itinerary> getItineraries(Search search);

    public String insertIntoUsers(User user);

    User checkLoginCredentials(User user);

    String bookATicket(Itinerary itinerary);

    List<Itinerary> getUserBookings(User user);

    String cancelTicket(Itinerary itinerary);
}
