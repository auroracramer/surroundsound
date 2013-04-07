package com.nullpandaexception.locality;

import org.opencv.core.Mat;

public class CameraData {
    public Mat freqs;
    public double[] colors;
    
    public CameraData(Mat f, double[] c) {
        freqs = f;
        colors = c;
    }
}
