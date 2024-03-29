package com.nullpandaexception.locality;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import com.nullpandaexception.locality.*;

public class Locality {
    
    public CameraAnalysis cam;
    public WeatherAnalysis weather;
    public GeoAnalysis geo;
    public TimeAnalysis time;
    private WeatherAPI weatherApi;
    
    private String weatherAPIKey = "296261cd190caa4a";
    
    public Locality(Context context) {
        LocationHandler.init(context);
        weatherApi = new WeatherAPI(weatherAPIKey, 600000);
        cam = CameraAnalysis.getInstance();
        weather = WeatherAnalysis.getInstance();
        geo = GeoAnalysis.getInstance();
        time = TimeAnalysis.getInstance();
    }
    
    public boolean canGetWeather(){
        return weatherApi.responseReady();
    }
    
    public WeatherData getWeather() {
        return weather.getData(weatherApi.getJSONResponse());
    }
}
