package com.nullpandaexception.locality;

import android.app.Service;
import android.content.Context;
import android.location.*;
import android.os.Bundle;
import android.os.IBinder;


public class LocationHandler {
    
    public static LocationHandler instance = null;
    public Location location;
    public Context context;
    
    
    
    protected LocationHandler(Context c) {
        // Acquire a reference to the system Location Manager
        context = c;
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

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
    public static void init(Context c) {
        if(instance == null) {
            instance = new LocationHandler(c);
         }
    }
    public static LocationHandler getInstance() {
        return instance;
     }

    
    


}
