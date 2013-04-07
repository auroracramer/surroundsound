package com.nullpandaexception.locality;

import org.opencv.core.Mat;

public class CameraData {
    public Mat freqs;
    public float[] colors;
    
    public CameraData(Mat f, float[] c) {
        freqs = f;
        colors = c;
    }
}
