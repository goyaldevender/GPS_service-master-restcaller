package testing.gps_service;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    // Start and stop service button
    private Button btn_start, btn_stop;

    private TextView textView;

    // Object for receiving and handling broadcast intents sent by sendBroadcast(Intent)
    private BroadcastReceiver broadcastReceiver;

    /*
     *
     */
    @Override
    protected void onResume() {
        super.onResume();
        if(broadcastReceiver == null){
            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {

                    // Appending the co-ordinates to the textView
                    textView.append("\n" +intent.getExtras().get("coordinates"));

                }
            };
        }

        // Register a BroadcastReceiver to be run in the main activity thread. The receiver will be
        // called with any broadcast Intent that matches filter, in the main application thread.
        // new IntentFilter( "Name of the intent that was broadcasted" );
        registerReceiver(broadcastReceiver,new IntentFilter("location_update"));
    }

    /**
     * When the activity is destroyed
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(broadcastReceiver != null){

            /*
              Unregister a previously registered Broadcast Receiver
             ( All the filters that have been registered for this BroadcastReceiver will be removed )
             */
            unregisterReceiver(broadcastReceiver);
        }
    }

    /**
     * Called when the activity is first created. This is where we do all of normal static set up:
     * such as create views, bind data to lists. This method also provide with a Bundle, which contains
     * the activity's previously frozen state
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Setting the default layout
        setContentView(R.layout.activity_main);

        // Mapping the UI elements to their backend entities
        btn_start = (Button) findViewById(R.id.button);
        btn_stop = (Button) findViewById(R.id.button2);
        textView = (TextView) findViewById(R.id.textView);

        // Asking for the user permission
        if(!runtime_permissions())
            enable_buttons();

    }

    /**
     * @description: Bind buttons to their on click listeners
     */
    private void enable_buttons() {

        // Specifying the code, which needs to run when the start button is clicked
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Specifying the intent
                Intent i =new Intent(getApplicationContext(),GPS_Service.class);

                // Signature:    startService(Intent service)
                // @Description: Request that the given application service be started.
                //               The intent should contain the complete class name of a specific
                //               service implementation.
                startService(i);
            }
        });

        // Specifying the code, which needs to run when the stop button is clicked
        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Specifying the intent
                Intent i = new Intent(getApplicationContext(),GPS_Service.class);

                // Stopping the service
                stopService(i);

            }
        });

    }

    /**
     * @description: Beginning android 6.0, the users grant the permission to the app when the app is running
     *               not when the app is installed ( This allows the users to have more control over the permissions )
     * @return
     */
    private boolean runtime_permissions() {

        // Checking if the API level is greater than 23( Android 6.0 )
        // if the permissions are not granted, then request for the permission
        if(Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            // ACCESS_FINE_LOCATION:   Allows the app to access fine location
            // ACCESS_COARSE_LOCATION: Allows the app to access approximate location
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},100);

            return true;
        }
        return false;
    }

    /**
     * @description: Callback for the results from requesting permissions
     * @param:       requestCode
     * @param:       permissions
     * @param:       grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 100){

            // If the permission is granted, then call the enable_button()
            if( grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                enable_buttons();
            }else {
             // If the permission is not granted, then ask for the permission again
                runtime_permissions();
            }
        }
    }
}
