package com.nullpandaexception.locality;

import org.json.json.JSONObject;
import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.*;
import org.opencv.utils.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;

import android.graphics.Color;
import android.util.Log;

import com.nullpandaexception.locality.*;
import java.util.ArrayList;
import java.util.List;

public class CameraAnalysis {

    public static final int[] COLORS = {Color.RED, Color.YELLOW, Color.GREEN, Color.BLUE, Color.CYAN, Color.MAGENTA};
    
    public static CameraAnalysis instance = null;
    
    protected CameraAnalysis(){
    }
    
    public static CameraAnalysis getInstance() {
        if (instance == null) {
            instance = new CameraAnalysis();
        }
        return instance;
    }
    
    public Mat getSpectrum (CvCameraViewFrame inputFrame) {
        
        
        Mat grayMat = inputFrame.gray();
        
        int m = Core.getOptimalDFTSize(grayMat.rows());
        int n = Core.getOptimalDFTSize(grayMat.cols());
        Mat padded = new Mat(m,n,grayMat.type());       
        Imgproc.copyMakeBorder(grayMat, padded, 0, m - grayMat.rows(), 0, n - grayMat.cols(), Imgproc.BORDER_CONSTANT, Scalar.all(0));
        
        List<Mat> planes = new ArrayList<Mat>();
        padded.convertTo(padded, CvType.CV_32F);
        planes.add(padded);
        planes.add(Mat.zeros(padded.size(), CvType.CV_32F));
        Mat complexI = new Mat(m,n, CvType.CV_32F);
        Core.merge(planes, complexI);
        
        Core.dft(complexI, complexI);
        
        Core.split(complexI, planes);
        
        Core.magnitude(planes.get(0), planes.get(1), planes.get(0));
        
        Mat magFreq = planes.get(0);
        Core.add(magFreq, Scalar.all(20), magFreq);
        //Core.log(magFreq, magFreq);
        //Core.log(magFreq, magFreq);
        magFreq = magFreq.submat(new Rect(0, 0, magFreq.cols() & -2, magFreq.rows() & -2));
        
        return magFreq;
    }
    
    public float[] getColorContent(CvCameraViewFrame inputFrame) {
        float[] colors = new float[COLORS.length];
        Mat rgbMat = inputFrame.rgba();
        int size = rgbMat.rows() * rgbMat.cols();
        for (int i = 0; i < rgbMat.rows(); i++){
            for (int j = 0; j < rgbMat.cols(); j++) {
                colors[getClosestColor(rgbMat.get(i, j))]++;
            }
        }
        return colors;
    }
    
    public int getClosestColor(double[] rgb) {
        int minColor = 0;
        int min = Integer.MAX_VALUE;
        
        for (int i = 0; i < COLORS.length; i++) {
            int rgbDist = rgbDistance(rgb, COLORS[i]); 
            if (rgbDist < min){
                min = rgbDist;
                minColor = i;
            }
        }
        //Log.i("Shit", Integer.toString(minColor));
        return minColor;
    }
    
    public int rgbDistance(double[] rgb, int c) {
        return (int) (Math.abs(rgb[0] - Color.red(c)) + Math.abs(rgb[1] - Color.green(c))  + Math.abs(rgb[2] - Color.blue(c)));
        
    }

    public CameraData getData(CvCameraViewFrame inputFrame) {
        return new CameraData(getSpectrum(inputFrame), getColorContent(inputFrame));
    }
    
}
