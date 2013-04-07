package com.nullpandaexception.markov;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;

import android.util.Log;

public class MarkovChain {
    private Mat probs;
    private float[] nodes; // Vector, needs to be the same length 
    private float scale;
    private Mat scaledProbs;
    private int curr;
    
    public MarkovChain(float[] n, Mat p, float s) {
        // we should probably make sure that the matrix is square, but we can worry about that later
        nodes = n;
        probs = p;
        scale = s;
        scaledProbs = new Mat(p.rows(), p.cols(), p.depth());
        scaleMatrix();
        curr = (int) Math.random() * nodes.length;
        
    }
    
    public void setScale(float s) {
        scale = s;
        scaleMatrix();
    }
    
    public void scaleMatrix() {
        Core.multiply(probs, Scalar.all(scale), scaledProbs);
    }
    
    public void setProbs(Mat newProb) {
        if(probs.rows() != newProb.rows() || probs.rows() != newProb.cols()){
            Log.i("Shit", "ASDFASDFASDFASDF");
        }
        probs = newProb;
        scaleMatrix();
    }
    
    // Advances to the next node and returns the nodes values
    public float advance() {
        //cheating
        /*
        return nodes[(int) (nodes.length*Math.random())];
        */

        // sum frequencies to get probabilities
        Mat probs = this.probs.clone(); // So the instance variable doesn't get swept out under our feet
        
        
        float total = 0;
        for (int k = 0; k < nodes.length && k < probs.cols(); k++) {
            if(probs.get(curr,k) == null) {
                // TODO fix this 
                continue;
            }
            total += magnitude(probs.get(curr,k));
        }
        
        // go through probabilities to get random 
        float runSum = 0;
        float prob = (float) Math.random();
        
        for (int m = 0; m < nodes.length && m < probs.cols(); m++) {
            if(probs.get(curr,m) == null) {
                // TODO fix this shit
                if(m == nodes.length - 1) {
                    return nodes[curr = (int)Math.random()*m];
                }
                continue;
            }
            runSum += magnitude(probs.get(curr,m))/total;
            if (runSum >= prob) {
                curr = m;
                return nodes[m];
            }
        }
        return 0; // Shouldn't be reached

    }
    
    public float magnitude(double[] v) {
        float mag = 0;
        for (int i = 0; i < v.length; i++) {
            mag += Math.pow(v[i],2);
        }
        return (float)Math.sqrt(mag);
    }
    
}
