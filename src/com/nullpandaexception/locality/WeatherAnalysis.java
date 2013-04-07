package com.nullpandaexception.locality;

import org.json.json.JSONArray;
import org.json.json.JSONObject;
import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;

import android.util.Log;

public class WeatherAnalysis {

    public static WeatherAnalysis instance = null;
    private static WeatherData cachedWeather;
    protected static boolean newDataAvailable;
    
    protected WeatherAnalysis() {
        newDataAvailable = true;
        cachedWeather = null;
    }
    
    public static WeatherAnalysis getInstance() {
        if (instance == null) {
            instance = new WeatherAnalysis();
        }
        return instance;
    }
    
    public static WeatherData getData(JSONObject data) {
        if (!newDataAvailable) {
            return cachedWeather;
        }
        newDataAvailable = false;

        data = data.getJSONObject("forecast").getJSONObject("simpleforecast").getJSONArray("forecastday").getJSONObject(0);
        int hightemp = Integer.parseInt(data.getJSONObject("high").getString("fahrenheit"));
        int lowtemp = Integer.parseInt(data.getJSONObject("low").getString("fahrenheit"));
        String conditions = data.getString("conditions");
        int windspeed = data.getJSONObject("avewind").getInt("kph");
        int humidity = data.getInt("avehumidity");
        cachedWeather = new WeatherData(hightemp, lowtemp, conditions, windspeed, humidity);
        return cachedWeather;
    }
}
