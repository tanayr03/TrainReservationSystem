package com.utility;

import java.sql.Time;

public class Utility {
    public static long subtractTimes( Time jsqlT1, Time jsqlT2){
        long differenceInMinutes = (jsqlT1.getTime()  - jsqlT2.getTime()) / 60000;
        return differenceInMinutes;
    }
}

