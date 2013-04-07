package com.nullpandaexception.locality;

public class WeatherData {
    public int hightemp;
    public int lowtemp;
    public String conditions;
    public int windspeed;
    public int humidity;
    
    public WeatherData(int ht, int lt, String cond, int wind, int hum) {
        hightemp = ht;
        lowtemp = lt;
        conditions = cond;
        windspeed = wind;
        humidity = hum;
    }
}
