package com.service.impl;

import com.dao.ItineraryDAO;
import com.domain.Itinerary;
import com.domain.Search;
import com.domain.Train;
import com.domain.User;
import com.service.ItineraryService;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class ItineraryServiceImpl implements ItineraryService {

    @Autowired
    private ItineraryDAO itineraryDAO;

    @Override
    public List<Itinerary> getItineraries(Search search) {

        System.out.println("1"+search.getDepartDate());
        System.out.println("2"+search.getReturnDate());
        System.out.println("3"+search.getIsRoundtrip());
        System.out.println("4"+search.isExactTime());

        search.setTicketType(search.getTicketType().toLowerCase());
        search.setNoOfConnections("1");
        String hours = String.valueOf(search.getDepartDate().getHours());
        String minutes = String.valueOf(search.getDepartDate().getMinutes());
        String seconds = String.valueOf(search.getDepartDate().getSeconds());
        String departureTime = hours +":" + minutes + ":" + seconds;
        Time depTime = java.sql.Time.valueOf(departureTime);
        search.setDepTime(depTime);
        List<Itinerary> itineraries = new ArrayList<>();

        if(search.getTicketType().equalsIgnoreCase("regular")){
            itineraries = getOnlyRegularItineraries(search);
        }
        else if (search.getTicketType().equalsIgnoreCase("express")){

            search.setDesStation("P");
            try {
                Search search3 = (Search) search.clone();
            }
            catch(Exception e)
            {
                System.out.println(e.getMessage());
            }

            Search search2 = new Search();
            search2.setSrcStation("P");
            search2.setDesStation("U");
            search2.setTicketType("regular");
            search2.setNoOfPassenger(search.getNoOfPassenger());
            search2.setNoOfConnections(search.getNoOfConnections());
            search2.setIsRoundtrip(search.getIsRoundtrip());
            search2.setDepartDate(search.getDepartDate());
            search2.setReturnDate(search.getReturnDate());

            Time deparTime = java.sql.Time.valueOf("10:21:00");
            search2.setDepTime(deparTime);
            search2.setDepTimeRoundTrip(search.getDepTimeRoundTrip());
            search2.setExactTime(false);

            itineraries = getAtleastOneExpressItineraries(search);

            List<Itinerary> regularItineraries = new ArrayList<>();

            HashMap<Integer,Train> hmap = new HashMap<Integer, Train>();

            regularItineraries = getOnlyRegularItineraries(search2);
            int trainNum = 0;
            for(Itinerary i:regularItineraries){
                System.out.println("hey");
                for(Train train:i.getTrainList()){
                    hmap.put(trainNum,train);
                    trainNum ++;
                }

            }
            int i = 0;

            for(Itinerary itinerary:itineraries){
                if(i==3){
                    break;
                }
                System.out.println("this is one itinerary");
                System.out.println("size should be 1:"+itinerary.getTrainList().size());
                List<Train> tyList = itinerary.getTrainList();
                tyList.add(hmap.get(i));
                itinerary.setTrainList(tyList);
                i++;
            }

        }
        else{
            itineraries = getOnlyRegularItineraries(search);
        }

        itineraries = setTotalItineraryFare(itineraries,search.getNoOfPassenger());
        return itineraries;
    }

    private List<Itinerary> setTotalItineraryFare(List<Itinerary> itineraries,int noOfPassenger) {
        for(Itinerary itinerary:itineraries){
            int totalItineraryFare = 0;
            for(Train train:itinerary.getTrainList()){
                totalItineraryFare += train.getFare();
            }
            itinerary.setNoOfPassenger(noOfPassenger);
            itinerary.setTotalFare((totalItineraryFare * noOfPassenger) + 1);
        }
        return itineraries;
    }

    @Override
    public String insertIntoUsers(User user) {
        String result = itineraryDAO.insertIntoUsers(user);
        return result;
    }

    @Override
    public User checkLoginCredentials(User user) {
        return itineraryDAO.checkLoginCredentials(user);
    }

    @Override
    public String bookATicket(Itinerary itinerary) {
        String result = "Success";
        int booking_id = itineraryDAO.getBookingCount();
        itineraryDAO.updateBookingCount(booking_id);
        itineraryDAO.bookATicket(itinerary,booking_id);
        return result;
    }

    @Override
    public List<Itinerary> getUserBookings(User user) {
        List<Itinerary> itineraries = itineraryDAO.getUserBookings(user.getEmail());
        return itineraries;
    }

    @Override
    public String cancelTicket(Itinerary itinerary) {
        itineraryDAO.cancelTicket(itinerary.getBooking_id());
        return "success";
    }

    private List<Itinerary> getAllPossibleItineraries(Search search) {
        List<Itinerary> itineraries = new ArrayList<>();
        return itineraries;
    }

    private List<Itinerary> getAtleastOneExpressItineraries(Search search) {

        search.setExactTime(true);

        List<Itinerary> itineraries = new ArrayList<>();
        List<String> expressStations = new ArrayList<>();
        expressStations.add("A");
        expressStations.add("F");
        expressStations.add("K");
        expressStations.add("P");
        expressStations.add("u");
        expressStations.add("Z");

        int difference = (int) search.getDesStation().toUpperCase().charAt(0) - (int) search.getSrcStation().toUpperCase().charAt(0);
        difference = Math.abs(difference);
        if(difference < 5){
            return itineraries;
        }
        else if (difference == 5 || difference == 20 || difference == 15 ){

            if(expressStations.contains(search.getSrcStation().toUpperCase())){

                // Get the nearest express trains from that terminal

                //List<Itinerary> itineraries = new ArrayList<>();
                List<Train> trains = itineraryDAO.getTrainsBySearch(search);
                boolean isSouthBound = checkIfTrainIsSouthBound(search.getSrcStation(),search.getDesStation());

                HashMap<Integer,Time> idDepartureMap = new HashMap<>();
                List<Integer> trainIds = new ArrayList<>();

                for(Train train:trains){
                    if((train.getId() <= 61 && isSouthBound) || (train.getId() > 61 && !isSouthBound)) {
                        idDepartureMap.put(train.getId(), train.getDeparture_time());
                        trainIds.add(train.getId());
                    }
                }
                List<Train> trainList = itineraryDAO.getTrainsById(trainIds);
                // for each train
                for(Train train:trainList) {
                    Itinerary itinerary = new Itinerary();
                    List<Train> itineraryTrainList = new ArrayList<>();
                    train.setDeparture_time(idDepartureMap.get(train.getId()));
                    train.setArrival_time(calculateTrainArrival(train.getTrainType(), train.getDeparture_time(), search.getSrcStation(), search.getDesStation()));
                    train.setDisplayDepartureTime(train.getDeparture_time().toString());
                    train.setDisplayArrivalTime(train.getArrival_time().toString());
                    train.setSrcStation(search.getSrcStation());
                    train.setDesStation(search.getDesStation());
                    train.setFare(calculateFare(train));

                    if(train.getTrainType().equalsIgnoreCase("express")) {
                        itineraryTrainList.add(train);
                        itinerary.setTrainList(itineraryTrainList);
                        itinerary.setDisplayDate(search.getDepartDate().toString());
                        itineraries.add(itinerary);
                    }
                }

                return itineraries;
            }
            else{
                return itineraries;
            }
        }
        else if (difference>5 && difference<10){

            //calculate nearest express station


            return itineraries;
        }

        return itineraries;
    }

    private List<Itinerary> getOnlyRegularItineraries(Search search) {

        List<Itinerary> itineraries = new ArrayList<>();
        List<Train> trains = itineraryDAO.getTrainsBySearch(search);
        boolean isSouthBound = checkIfTrainIsSouthBound(search.getSrcStation(),search.getDesStation());

        HashMap<Integer,Time> idDepartureMap = new HashMap<>();
        List<Integer> trainIds = new ArrayList<>();

        for(Train train:trains){
            if((train.getId() <= 61 && isSouthBound) || (train.getId() > 61 && !isSouthBound)) {
                idDepartureMap.put(train.getId(), train.getDeparture_time());
                trainIds.add(train.getId());
            }
        }
        List<Train> trainList = itineraryDAO.getTrainsById(trainIds);
        // for each train
        for(Train train:trainList) {
            Itinerary itinerary = new Itinerary();
            List<Train> itineraryTrainList = new ArrayList<>();
            train.setDeparture_time(idDepartureMap.get(train.getId()));
            train.setArrival_time(calculateTrainArrival(train.getTrainType(), train.getDeparture_time(), search.getSrcStation(), search.getDesStation()));
            train.setDisplayDepartureTime(train.getDeparture_time().toLocalTime().toString());
            train.setDisplayArrivalTime(train.getArrival_time().toLocalTime().toString());
            train.setSrcStation(search.getSrcStation());
            train.setDesStation(search.getDesStation());
            train.setFare(calculateFare(train));

            if(train.getTrainType().equalsIgnoreCase("regular")) {
                itineraryTrainList.add(train);
                 itinerary.setTrainList(itineraryTrainList);
                itinerary.setDisplayDate(search.getDepartDate().toString());
                itineraries.add(itinerary);
            }
        }
        return itineraries;
    }

    /*This method checks if train is south bound the fare */
    private boolean checkIfTrainIsSouthBound(String srcStation, String desStation) {
        int difference = (int) desStation.toUpperCase().charAt(0) - (int) srcStation.toUpperCase().charAt(0);
        return difference>=0?true:false;
    }

    /*This method returns the fare */
    private int calculateFare(Train train) {
        int stations = (int) train.getDesStation().toUpperCase().charAt(0) - (int) train.getSrcStation().toUpperCase().charAt(0);
        stations = Math.abs(stations);
        int fare = (stations / 5) + 1;
        if(train.getTrainType().equalsIgnoreCase("express")){
            fare = fare * 2;
        }
        return fare;
    }

    /*This method returns the arrival time of any train from src to dest station*/
    private Time calculateTrainArrival(String trainType, Time start_time, String srcStation, String desStation) {
        Time arrivalTime = start_time;
        if(trainType.equalsIgnoreCase("regular")) {
            int difference = (int) desStation.toUpperCase().charAt(0) - (int) srcStation.toUpperCase().charAt(0);
            difference = Math.abs(difference);
            int timeToAdd = (8 * difference) - 3;
            LocalTime localArrivalTime = arrivalTime.toLocalTime();
            localArrivalTime = localArrivalTime.plusMinutes(timeToAdd);
            arrivalTime = java.sql.Time.valueOf(localArrivalTime.toString() + ":00");
        }
        else if(trainType.equalsIgnoreCase("express")){
            int difference = (int) desStation.toUpperCase().charAt(0) - (int) srcStation.toUpperCase().charAt(0);
            difference = Math.abs(difference);
            int timeToAdd = (5 * difference) + 3 * expressStationsInBetween(difference);
            LocalTime localArrivalTime = arrivalTime.toLocalTime();
            localArrivalTime = localArrivalTime.plusMinutes(timeToAdd);
            arrivalTime = java.sql.Time.valueOf(localArrivalTime.toString() + ":00");
        }
        return arrivalTime;
    }

    private int expressStationsInBetween(int difference) {
        return (difference/5) -1;
        }
}
