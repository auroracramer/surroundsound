package com.nullpandaexception.locality;

import org.json.json.JSONObject;

import android.location.*;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import com.nullpandaexception.locality.*;

public class GeoAnalysis {
    
    public static GeoAnalysis instance = null;
    
    protected GeoAnalysis() {
        
    }
    
    public static GeoAnalysis getInstance() {
        if(instance == null) {
           instance = new GeoAnalysis();
        }
        return instance;
     }
    
    public static GeoData getData() {
        Location loc = LocationHandler.getInstance().location;
        return new GeoData((float)loc.getLongitude(), (float)loc.getLatitude());
    }
    
}

