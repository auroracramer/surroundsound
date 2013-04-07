package com.nullpandaexception.locality;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.json.JSONObject;

import android.location.Location;
import android.util.Log;

public class WeatherAPI extends TimerTask implements APIHandler {
    private String apiKey;
    private String apiBaseURL;
    private long refreshRate;
    private Location location;
    private String response;
    private JSONObject responseJSON;
    Timer timer;
    
    public WeatherAPI(String key, long refresh) {
        apiKey = key;
        apiBaseURL = "http://api.wunderground.com/api/" + key + "/forecast/q/";
        refreshRate = refresh;
        response = "";
        responseJSON = null;
        timer = new Timer();
        run();
        timer.schedule(this, refreshRate);
    }
    
    public void run() {

        HttpClient httpclient = new DefaultHttpClient();
        HttpResponse response;
        Location loc = LocationHandler.getInstance().location;
        String call = apiBaseURL + loc.getLatitude() + "," + loc.getLongitude() + ".json";
        String responseString = "";
        try {
            response = httpclient.execute(new HttpGet(call));
            StatusLine statusLine = response.getStatusLine();
            if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                out.close();
                this.response = out.toString();
                this.responseJSON = new JSONObject(this.response);
                WeatherAnalysis.getInstance().newDataAvailable = true;
            } else{
                //Closes the connection.
                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            }
        } catch (ClientProtocolException e) {
            Log.i("Shit", e.toString());
        } catch (IOException e) {
            Log.i("Shit", e.toString());
        }
    }
    public boolean responseReady() {
        return !response.equals("");
    }
    public String getResponse() {
        return response;
    }
    
    public JSONObject getJSONResponse() {
        return responseJSON;
    }

    public void call(String call) {
        // At least right now, this API doesn't take any other calls
        run();
        
    }
    
    
}
