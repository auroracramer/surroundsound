package com.nullpandaexception.locality;

import org.json.json.JSONObject;
import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;

public class WeatherAnalysis {

    public static WeatherAnalysis instance = null;
    
    protected WeatherAnalysis() {
        
    }
    
    public static WeatherAnalysis getInstance() {
        if (instance == null) {
            instance = new WeatherAnalysis();
        }
        return instance;
    }
    
    public static WeatherData getData(JSONObject data) {
        data = data.getJSONObject("simpleforecast").getJSONObject("forecastday");
        int hightemp = data.getInt("high");
        int lowtemp = data.getInt("low");
        String conditions = data.getString("conditions");
        int windspeed = data.getInt("avewind");
        int humidity = data.getInt("avehumidity");
        
        return new WeatherData(hightemp, lowtemp, conditions, windspeed, humidity);
    }
}
