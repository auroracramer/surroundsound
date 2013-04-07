package com.nullpandaexception.locality;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class TimeAnalysis {
    public static TimeAnalysis instance = null;
    
    protected TimeAnalysis(){
        
    }
    
    public static TimeAnalysis getInstance(){
        if (instance == null) {
            instance = new TimeAnalysis();
        }
        return instance;
    }
    
    public TimeData getData() {
        Calendar cal = new GregorianCalendar();
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int timeOfDay;
        
        if (hour >= 6 && hour < 12) {
            timeOfDay = 0;
        } else if (hour >= 12 && hour < 18) {
            timeOfDay = 1;
        } else {
            timeOfDay = 2;
        }
        
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        
        int season;
        int month = cal.get(Calendar.MONTH);
        if (month < 4) {
            season = 0;
        } else if (month < 8) {
            season = 1;
        } else {
            season = 2;
        }
        
        
        return new TimeData(timeOfDay, dayOfWeek, season);
    }
}
