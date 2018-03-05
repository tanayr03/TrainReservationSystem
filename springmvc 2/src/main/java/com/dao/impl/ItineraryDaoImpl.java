package com.dao.impl;

import com.dao.ItineraryDAO;
import com.domain.Itinerary;
import com.domain.Search;
import com.domain.Train;
import javax.sql.DataSource;

import com.domain.User;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Time;
import java.time.LocalTime;
import java.util.*;


/* Tanay Rashinkar */
public class ItineraryDaoImpl implements ItineraryDAO {

    private JdbcTemplate jdbcTemplate;

    public ItineraryDaoImpl(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public List<Train> getTrainsById(List<Integer> trainIds) {

        List<Train> trainList = new ArrayList<Train>();
        String addToSql = "";
        int count = 0;

        for (int id : trainIds) {
            if (count == 0) {
                addToSql = addToSql + " where train_id=" + id;
            } else {
                addToSql = addToSql + " or train_id=" + id;
            }
            count = 1;
        }
        String sql = "select * from train_table" + addToSql;
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);

            for (Map row : rows) {
                Train train = new Train();
                train.setName((String) row.get("train_name"));
                train.setId((int) row.get("train_id"));
                train.setStatus((String) row.get("status"));
                train.setTrainType((String) row.get("train_type"));
                trainList.add(train);
            }
        return trainList;
    }

    /* Select all trains from given start terminal where departure from that terminal is within 30 minutes of
    departure specified. */
    @Override
    public List<Train> getTrainsBySearch(Search search) {

        List<Train> trains = new ArrayList<>();

        Character sourceStation =  search.getSrcStation().toUpperCase().charAt(0);
        int ascii = (int) sourceStation;
        int stationId = ascii - 65 + 1;

        Time searchTime =  search.getDepTime();

        LocalTime localtimeBefore = searchTime.toLocalTime();
        localtimeBefore = localtimeBefore.minusMinutes(1);
        localtimeBefore = localtimeBefore.plusSeconds(1);

        LocalTime localTimeAfter = searchTime.toLocalTime();
        localTimeAfter = localTimeAfter.plusMinutes(60);
        localTimeAfter = localTimeAfter.plusSeconds(1);

        Time timeBefore = java.sql.Time.valueOf(localtimeBefore.toString());
        Time timeAfter = java.sql.Time.valueOf(localTimeAfter.toString());

        String sql = "";
        System.out.println("comes here"+sql);

        if(search.isExactTime() == true){
            System.out.println("query"+sql);
            sql = "select * from train_schedule WHERE station_id =" + stationId+" and departure_time BETWEEN '"+searchTime.toLocalTime().toString()+"' and '"+searchTime.toLocalTime().toString()+"'";
        }
        else{
             System.out.println("query"+sql);
             sql = "select * from train_schedule WHERE station_id =" + stationId+" and departure_time BETWEEN '"+timeBefore.toString()+"' and '"+timeAfter.toString()+"'";
        }



        System.out.println(sql);
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);

        for (Map row : rows) {
            Train train = new Train();
            train.setId((int) row.get("train_id"));
            Time departure_time =  (Time) row.get("departure_time");
            LocalTime localDeparture_Time = departure_time.toLocalTime();
            localDeparture_Time = localDeparture_Time.plusHours(8);
            Time departure_time_gmt = java.sql.Time.valueOf(localDeparture_Time.toString()+":00");
            train.setDeparture_time(departure_time_gmt);
            trains.add(train);
        }
        return trains;
    }


    @Override
    public String insertIntoUsers(User user) {
        String username = user.getFirstName();
        String email = user.getEmail();
        String password = user.getPassword();
        String sql_insert = "insert into user_table values('"+username+"','"+email+"','"+password+"');";
        jdbcTemplate.update(sql_insert);
        return "Sucess";
    }

    @Override
    public User checkLoginCredentials(User user) {

        User usr = new User();
        String result = "Logged in";
        String email = user.getEmail();
        String password = user.getPassword();
        String sql_check = "select * from user_table where email = '" + email+"' and password = '"+password+"' ;";
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql_check);

        for (Map row : rows) {
            usr.setFirstName((String) row.get("username"));
            usr.setEmail((String) row.get("email"));
            usr.setPassword((String) row.get("password"));
        }
        return usr;
    }


    @Override
    public int getBookingCount() {
        String sql_count = "select max(booking_id) from booking_count";
        List<Map<String, Object>> res = jdbcTemplate.queryForList(sql_count);
        int x=0;
        for (Map row : res) {
            x = (int) row.get("max(booking_id)");
        }
        return x;
        }

    @Override
    public void updateBookingCount(int booking_id) {
        String sql_update = "insert into booking_count value("+String.valueOf(booking_id+1) +");";
        System.out.println("Update the record" +jdbcTemplate.update(sql_update));
    }

    @Override
    public void bookATicket(Itinerary itinerary, int booking_id) {
        System.out.println("checking no of passengers in booking"+itinerary.getNoOfPassenger());
        for(Train train: itinerary.getTrainList()){
            String sql = "insert into booking_table values("+booking_id+",'"+ itinerary.getEmail()+"','"+train.getName()+"','"+train.getSrcStation()+"','"+train.getDisplayDepartureTime()+"','"+train.getDesStation()+
                    "','"+train.getDisplayArrivalTime() +"','"+itinerary.getDisplayDate()+"',"+itinerary.getTotalFare()+",'"+"confirmed',"+itinerary.getNoOfPassenger()+");";
            insertBooking(sql);
        }
    }

    @Override
    public List<Itinerary> getUserBookings(String email) {
        List<Itinerary> itineraries = new ArrayList<>();
        String sql = "select * from booking_table where email = '" + email+"';";
        System.out.println("sql"+sql);

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);

        for (Map row : rows) {
            Itinerary itinerary = new Itinerary();
            List<Train> trains = new ArrayList<>();
            Train train = new Train();
            itinerary.setBooking_id((int) row.get("booking_id"));
            itinerary.setEmail((String) row.get("email"));

            if(null!=row.get("no_of_persons")){
                itinerary.setNoOfPassenger((int) row.get("no_of_persons"));
            }
            else{
                itinerary.setNoOfPassenger(1);
            }

            itinerary.setDisplayDate((String) row.get("date"));
            itinerary.setTotalFare((int) row.get("fare"));
            itinerary.setEmail((String) row.get("email"));
            train.setName((String) row.get("train_name"));
            train.setSrcStation((String) row.get("source"));
            train.setDesStation((String) row.get("destination"));

            Time departureTime =  (Time) row.get("departure_time");
            LocalTime localDepartureTime = departureTime.toLocalTime();
            localDepartureTime = localDepartureTime.plusHours(8);
            Time departure_time_gmt = java.sql.Time.valueOf(localDepartureTime.toString()+":00");
            train.setDisplayDepartureTime(departure_time_gmt.toLocalTime().toString());

            Time arrival_time =  (Time) row.get("arrival_time");
            LocalTime localArrivalTime = arrival_time.toLocalTime();
            localArrivalTime = localArrivalTime.plusHours(8);
            Time local_time_gmt = java.sql.Time.valueOf(localArrivalTime.toString()+":00");
            train.setDisplayArrivalTime(local_time_gmt.toLocalTime().toString());

            //train.setDisplayDepartureTime( row.get("departure_time").toLocalTime().toString());
            //train.setDisplayArrivalTime( row.get("arrival_time").toString());
            itinerary.setBookingStatus((String) row.get("status"));
            trains.add(train);
            itinerary.setTrainList(trains);
            itineraries.add(itinerary);
        }
        return itineraries;
    }

    @Override
    public void cancelTicket(int booking_id) {
        String sql_test = "UPDATE booking_table SET status = 'cancelled' WHERE booking_id ="+booking_id+";";
        insertBooking(sql_test);
    }

    private String insertBooking(String sql){
        jdbcTemplate.update(sql);
        return "confirmed";
    }
}
