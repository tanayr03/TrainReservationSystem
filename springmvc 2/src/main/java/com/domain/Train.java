package com.domain;

import java.sql.Time;
import java.util.Date;

public class Train {

    private int id;
    private String name;
    private String srcStation;
    private String desStation;
    private int fare;
    private String status;
    private String trainType;
    private Time departure_time;
    private Time arrival_time;
    private String displayDepartureTime;
    private String displayArrivalTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSrcStation() {
        return srcStation;
    }

    public void setSrcStation(String srcStation) {
        this.srcStation = srcStation;
    }

    public String getDesStation() {
        return desStation;
    }

    public void setDesStation(String desStation) {
        this.desStation = desStation;
    }

    public int getFare() {
        return fare;
    }

    public void setFare(int fare) {
        this.fare = fare;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTrainType() {
        return trainType;
    }

    public void setTrainType(String trainType) {
        this.trainType = trainType;
    }

    public Time getDeparture_time() {
        return departure_time;
    }

    public void setDeparture_time(Time departure_time) {
        this.departure_time = departure_time;
    }

    public Time getArrival_time() {
        return arrival_time;
    }

    public void setArrival_time(Time arrival_time) {
        this.arrival_time = arrival_time;
    }

    public String getDisplayDepartureTime() {
        return displayDepartureTime;
    }

    public void setDisplayDepartureTime(String displayDepartureTime) {
        this.displayDepartureTime = displayDepartureTime;
    }

    public String getDisplayArrivalTime() {
        return displayArrivalTime;
    }

    public void setDisplayArrivalTime(String displayArrivalTime) {
        this.displayArrivalTime = displayArrivalTime;
    }
}
