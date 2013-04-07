package com.nullpandaexception.locality;

import android.app.Activity;
import android.content.Context;
import android.location.*;
import android.os.Bundle;


public class LocationHandler extends Activity {
    
    public static LocationHandler instance = null;
    public Location location;
    
    protected LocationHandler() {
        // Acquire a reference to the system Location Manager
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
              // Called when a new location is found by the network location provider.
              LocationHandler.getInstance().location = location;
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}

          };

        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        // Register the listener with the Location Manager to receive location updates
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
    }
    
    public static LocationHandler getInstance() {
        if(instance == null) {
           instance = new LocationHandler();
        }
        return instance;
     }


    
    


}
