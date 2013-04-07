package com.nullpandaexception.markov;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;

public class MarkovChain {
    private Mat probs;
    private double[] nodes; // Vector, needs to be the same length 
    private double scale;
    private Mat scaledProbs;
    private int curr;
    
    public MarkovChain(double[] n, Mat p, double s) {
        // we should probably make sure that the matrix is square, but we can worry about that later
        nodes = n;
        probs = p;
        scale = s;
        
        scaleMatrix();
        
        curr = (int) Math.random() * nodes.length;
        
    }
    
    public void setScale(double s) {
        scale = s;
        scaleMatrix();
    }
    
    public void scaleMatrix() {
        Core.multiply(probs, Scalar.all(scale), scaledProbs);
    }
    
    public void setProbs(Mat newProb) {
        probs = newProb;
        scaleMatrix();
    }
    
    // Advances to the next node and returns the nodes values
    public double advance() {
        // sum frequencies to get probabilities
        double total = 0;
        for (int k = 0; k < nodes.length; k++) {
            total += magnitude(probs.get(curr,k));
        }
        
        // go through probabilities to get random 
        double runSum = 0;
        double prob = Math.random();
        for (int k = 0; k < nodes.length; k++) {
            runSum += magnitude(probs.get(curr,k));
            if (runSum <= prob) {
                curr = k;
                return nodes[k];
            }
        }
        return 0; // Shouldn't be reached
    }
    
    public double magnitude(double[] v) {
        double mag = 0;
        for (int i = 0; i < v.length; i++) {
            mag += Math.pow(v[i],2);
        }
        return Math.sqrt(mag);
    }
    
}
