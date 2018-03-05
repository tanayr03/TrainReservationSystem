package com.lightrail.controllers;

import com.domain.Itinerary;
import com.domain.Search;
import com.domain.User;
import com.service.ItineraryService;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import java.util.List;


/* Tanay Rashinkar */

@RestController
public class ReservationControllerRest {

    @Autowired
    private ItineraryService itineraryService;


    /* - - - - get trains API - - - - */

    @CrossOrigin
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @RequestMapping(value = "/getTrains", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> getTrains(@RequestBody Search search ) {
        try {

            System.out.println("checking"+search.getDepartDate());
            List<Itinerary> itineraries = itineraryService.getItineraries(search);
            return new ResponseEntity(itineraries, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity("Invalid Request exception occured" + e +search.getSrcStation()+search.getNoOfConnections(), HttpStatus.BAD_REQUEST);
        }
    }

    /* - - - - get user bookings API - - - - */

    @CrossOrigin
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @RequestMapping(value = "/getBookings", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> getUserBookings(@RequestBody User user ) {
        try {
            String result = "";
            List<Itinerary> itineraries = itineraryService.getUserBookings(user);
            return new ResponseEntity(itineraries, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity("Invalid Request"+e, HttpStatus.BAD_REQUEST);
        }
    }

    /* Book A Ticket API */

    @CrossOrigin
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @RequestMapping(value = "/bookTicket", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> bookAnItinerary(@RequestBody Itinerary itinerary ) {
        try {
            String result = "Success";
            result = itineraryService.bookATicket(itinerary);
            return new ResponseEntity(result, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity("Unable to Book At this time."+e, HttpStatus.BAD_REQUEST);
        }
    }

    /* Cancel A Ticket API */

    @CrossOrigin
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @RequestMapping(value = "/cancelTicket", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> cancelTicket(@RequestBody Itinerary itinerary ) {
        try {
            String result = "Success";
            result = itineraryService.cancelTicket(itinerary);
            return new ResponseEntity(result, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity("Unable to Book At this time."+e, HttpStatus.BAD_REQUEST);
        }
    }

}
