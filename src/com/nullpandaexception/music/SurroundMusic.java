package com.nullpandaexception.music;

import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.core.Mat;

import android.content.Context;
import android.util.Log;
import as.adamsmith.etherealdialpad.dsp.*;

import com.nullpandaexception.locality.Locality;
import com.nullpandaexception.markov.MarkovChain;

public class SurroundMusic implements Runnable {
    public Locality locality;
    public MarkovChain leadNotes;
    private int delay;
    private int delayLimit = 1;
    private Thread proc;
    private Dac ugDac;
    public CvCameraViewFrame frame;
    private WtOsc synthLead;
    private WtOsc synthPad;
    private WtOsc synthBass;
    private WtOsc noise;
    public float[] colors;
    
    public SurroundMusic(Context context, Dac dac, WtOsc synthLead) {
        locality = new Locality(context);
        leadNotes = null;
        delay = 0;
        frame = null;
        ugDac = dac;
        //initInstruments();
        //testInstruments();
    }
    
    public void initInstruments() {
        //ugDac = new Dac();
        
        //synthLead.fillWithHardSin(7.0f);
        synthLead.setFreq(220);
        //synthPad = new WtOsc();
        //ugDac.chuck(synthLead);
        //ugDac.chuck(synthPad);
    }
    
    public void testInstruments() {
        WtOsc ugOscA1 = new WtOsc(); 
        ugOscA1.fillWithHardSin(7.0f); 
        ugOscA1.setFreq(220); 
        ugDac = new Dac(); 
        //ugOscA1.chuck(ugDac);
        
    }
    public void start() {
        stop();
        proc = new Thread(this);
        proc.start();
        
    }
    
    public void stop() {
        if (proc != null) {
            proc.interrupt();
        }
    }
    
    
    public void updateVideo(CvCameraViewFrame inputFrame) {

        if (delay == 0) {
            Mat freqs = locality.cam.getSpectrum(inputFrame);
            colors = locality.cam.getColorContent(inputFrame);
            

            float[] pitches = new float[freqs.cols()];
            float low = (float) 130.813;
            float high = (float) 523.251;
            float step = (high - low)/pitches.length;
            for (int i = 0; i < pitches.length; i++) {
                pitches[i] = low + step * i;
            }
            leadNotes = new MarkovChain(pitches, freqs, 1);
            
            delay++;
        } else if (delay == delayLimit){
            delay = 0;
        } else {
            delay++;
        }

    }

    public void run() {
        ugDac.open();
        while (!proc.isInterrupted()){
            if (frame != null) {
                updateVideo(frame);
            }
            if (leadNotes != null && frame != null) {
                //float freq = (float) leadNotes.advance();
                //synthLead.setFreq(freq);
                //ugDac.tick();
            }
        }
        ugDac.close();
    }
    
    
}
