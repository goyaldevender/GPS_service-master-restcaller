package testing.gps_service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;

import java.text.DecimalFormat;

/**
 * Created by Devender Goyal on 26/6/17.
 *
 * @Description: * Service:   A Service is a component that performs the operations in the background without a user interface
 *
 *               * Broadcast: A Broadcast is an event that any app can receive. The system delivers various broadcasts for
 *                            system events, such as when the system boots up or the device starts charging. We can deliver
 *                            a broadcast to other apps by passing an Intent to sendBroadcast().
 */
public class GPS_Service extends Service {

    // LocationManager class provides access to system location services
    private LocationManager locationManager;

    // Description: Used for receiving notifications from the location manager, when the location has changed.
    private LocationListener listener;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {

                listener = new LocationListener() {

                    // Called when the location has changed
                    @Override
                    public void onLocationChanged(Location location) {
                        Intent i = new Intent("location_update");
                        i.putExtra("coordinates","Lat: " + new DecimalFormat("#.##").format(location.getLatitude())
                                        + "     Long: " + new DecimalFormat("#.##").format(location.getLongitude()));

                        // Sends the broadcast to all the receivers in an undefined order. The call is asynchronous; it returns immediately,
                        // and we will continue executing while the receivers are run
                        sendBroadcast(i);
                }

                // Called when the provider status changes
                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {

                }

                // Called when the provider is enabled by the user
                @Override
                public void onProviderEnabled(String s) {

                }

                // Called when the provider is disabled by the user
                @Override
                public void onProviderDisabled(String s) {
                        Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                }
            };


        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);



        /*
            Description: Registering the locationListener with the location manager service
            Provider:    The name of provider with which to register
            MinTime:     Minimum time interval between location updates, in milliseconds( here 3 sec)
            MinDistance: Minimum distance between location updates
            Listener:    A LocationListener whose onLocationChanged(Location) method will be called
                         for each location update.
         */

        //no inspection MissingPermission
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,3000,0,listener);

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if(locationManager != null){
            //noinspection MissingPermission
            locationManager.removeUpdates(listener);
        }
    }
}
