package com.nullpandaexception.surroundsound;

import as.adamsmith.etherealdialpad.dsp.*;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;

import com.nullpandaexception.surroundsound.R;
import com.nullpandaexception.music.*;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.Toast;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class MusicGenerator extends Activity implements CvCameraViewListener2{
    private static final String TAG = "OCVSample::Activity";

    private CameraBridgeViewBase mOpenCvCameraView;
    private boolean              mIsJavaCamera = true;
    private MenuItem             mItemSwitchCamera = null;
    private SurroundMusic smusic;
    private Dac ugDac;
    private WtOsc synthLead;
    private WtOsc synthPad;
    private Delay delay;
    private ExpEnv env;
    private WtOsc bass;
    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    //Log.i(TAG, "OpenCV loaded successfully");
                    mOpenCvCameraView.enableView();
                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };

    public MusicGenerator() {
        smusic = null;
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.surround_sound);

        if (mIsJavaCamera)
            mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.activity_main_java_surface_view);
        else
            mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.activity_main_native_surface_view);

        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
        mOpenCvCameraView.setCvCameraViewListener(this);
        

    }

    @Override
    public void onPause()
    {
        super.onPause();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_3, this, mLoaderCallback);
    }

    public void onDestroy() {
        super.onDestroy();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Log.i(TAG, "called onCreateOptionsMenu");
        mItemSwitchCamera = menu.add("Toggle Native/Java camera");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //String toastMesage = new String();
        //Log.i(TAG, "called onOptionsItemSelected; selected item: " + item);

        if (item == mItemSwitchCamera) {
            mOpenCvCameraView.setVisibility(SurfaceView.GONE);
            mIsJavaCamera = !mIsJavaCamera;

            if (mIsJavaCamera) {
                mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.activity_main_java_surface_view);
                //toastMesage = "Java Camera";
            } else {
                mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.activity_main_native_surface_view);
                //toastMesage = "Native Camera";
            }

            mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
            mOpenCvCameraView.setCvCameraViewListener(this);
            mOpenCvCameraView.enableView();
            //Toast toast = Toast.makeText(this, toastMesage, Toast.LENGTH_LONG);
            //toast.show();
        }

        return true;
    }

    public void onCameraViewStarted(int width, int height) {
        //WtOsc ugOscA1 = new WtOsc(); 
        //ugOscA1.fillWithHardSin(7.0f); 
        //ugOscA1.setFreq(220); 
        synthLead = new WtOsc();
        synthLead.fillWithHardSin(7.0f);
        //synthLead.fillWith();
        bass = new WtOsc();
        bass.fillWithSin();
        bass.setFreq(100);
        synthPad = new WtOsc();
        synthPad.fillWithSqr();
        synthPad.setFreq(100f);
        
        delay = new Delay(UGen.SAMPLE_RATE/8);
        //env = new ExpEnv();
        //synthLead.chuck(delay);
        //bass.chuck(delay);
        
        synthLead.setFreq(220);
        ugDac = new Dac(); 
        synthLead.chuck(delay).chuck(ugDac);
        //env.setFactor(ExpEnv.hardFactor);
        //bass.chuck(ugDac);
        //synthLead.chuck(ugDac);
        
        synthPad.chuck(delay).chuck(ugDac);
        
        //ugOscA1.chuck(ugDac);
        
        if (smusic == null) {
            smusic = new SurroundMusic(this, ugDac, synthLead);
            smusic.start();
        }
        


        Thread thisThread = new Thread( new Runnable( ) 
        { 
                public void run( ) 
                { 
                        ugDac.open(); 
                        //double curr = 200.0;
                        int del = 0;
                        int maxDel = 15;
                        while(true) {
                            if (smusic.leadNotes != null) {

                                if(del == 0){
                                    float freq = (float) smusic.leadNotes.advance();
                                    synthLead.unchuck(delay);
                                    synthLead = new WtOsc();
                                    synthLead.setFreq(freq);
                                    synthLead.fillWithHardSin(7.0f);
                                    del++;
                                    float max = -1;
                                    int max_i = -1;
                                    for (int i = 0; i < smusic.colors.length; i++){
                                        if (smusic.colors[i] > max){
                                            
                                            max = smusic.colors[i];
                                            max_i = i;
                                        }
                                    }
                                    delay.unchuck(ugDac);
                                    synthPad.unchuck(delay);
                                    delay = new Delay((int)(Delay.SAMPLE_RATE * (max_i + 1)));
                                    synthPad.unchuck(ugDac);
                                    synthPad = new WtOsc();
                                    synthPad.setFreq((float) (max_i + 1) * 400);
                                    synthPad.fillWithSqr();
                                    synthPad.chuck(delay).chuck(ugDac);
                                    synthLead.chuck(delay);

                                } else if (del == maxDel) {
                                    del = 0;
                                    if (smusic.locality.canGetWeather()){
                                        //maxDel = smusic.locality.getWeather().windspeed*2;
                                    }
                                } else {
                                    del++;
                                }
                            }
                            if (smusic.locality.canGetWeather()){
                                //bass.setFreq((float)smusic.locality.getWeather().hightemp);
                            }
                            //synthLead.setFreq((float)curr);
                            //curr += 5;
                            
                            ugDac.tick(); 
                        } 
                } 
        }); 
        thisThread.setPriority(Thread.MAX_PRIORITY); 
        thisThread.start(); 
        //ugDac.close(); 
    }

    public void onCameraViewStopped() {
    }

    public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
        synthLead.setFreq(440);
        smusic.frame = inputFrame;
        return inputFrame.rgba();
    }

}
