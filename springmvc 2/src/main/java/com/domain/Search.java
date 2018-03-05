package com.domain;

import java.sql.Time;
import java.util.Date;

public class Search {

    String srcStation;
    String desStation;
    String ticketType;
    int noOfPassenger;
    String noOfConnections;
    int isRoundtrip;
    Date departDate;
    Date returnDate;
    Time depTime;
    Time depTimeRoundTrip;
    boolean isExactTime;
    boolean isExactTimeDeparture;
    boolean isExactTimeArrival;
    public Search(){
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

    public String getTicketType() {
        return ticketType;
    }

    public void setTicketType(String ticketType) {
        this.ticketType = ticketType;
    }

    public int getNoOfPassenger() {
        return noOfPassenger;
    }

    public void setNoOfPassenger(int noOfPassenger) {
        this.noOfPassenger = noOfPassenger;
    }

    public String getNoOfConnections() {
        return noOfConnections;
    }

    public void setNoOfConnections(String noOfConnections) {
        this.noOfConnections = noOfConnections;
    }

    public Date getDepartDate() {
        return departDate;
    }

    public void setDepartDate(Date departDate) {
        this.departDate = departDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    public Time getDepTime() {
        return depTime;
    }

    public void setDepTime(Time depTime) {
        this.depTime = depTime;
    }

    public int getIsRoundtrip() {
        return isRoundtrip;
    }

    public void setIsRoundtrip(int isRoundtrip) {
        this.isRoundtrip = isRoundtrip;
    }

    public boolean isExactTime() {
        return isExactTime;
    }

    public void setExactTime(boolean exactTime) {
        isExactTime = exactTime;
    }

    public Time getDepTimeRoundTrip() {
        return depTimeRoundTrip;
    }

    public void setDepTimeRoundTrip(Time depTimeRoundTrip) {
        this.depTimeRoundTrip = depTimeRoundTrip;
    }

    public boolean isExactTimeDeparture() {
        return isExactTimeDeparture;
    }

    public void setExactTimeDeparture(boolean exactTimeDeparture) {
        isExactTimeDeparture = exactTimeDeparture;
    }

    public boolean isExactTimeArrival() {
        return isExactTimeArrival;
    }

    public void setExactTimeArrival(boolean exactTimeArrival) {
        isExactTimeArrival = exactTimeArrival;
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
