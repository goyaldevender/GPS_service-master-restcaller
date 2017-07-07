package testing.gps_service;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

public class POST_RestCall_2 extends AppCompatActivity {


    // Method where we initialize the activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d("Devender","Starting of POST Rest Call");
        Log.d("Devender","START: onCreate function");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_rest_call_2);
        Log.d("Devender","END: onCreate function");
    }

    //  Method: postRestCall
    //  Description : This gets executed, when we click the "Check your IP button"  button
    public void postRestCall(View view){
        try {
            Log.d("Devender","Entering rest call");

            new SendRequest(view).execute(new URL("http://posttestserver.com/post.php")); // .execute() method is used to start the background task.

            Log.d("Devender","Exiting rest call");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     *  Class       : SendRequest
     *  Modifier    : Private
     *  Description : AsyncTask( This allows us to perform the heavy task in the background on a separate thread)
     *                and keep the UI thread light.
     *  Note        : AsyncTask can be started only once. Starting it again will throw an error.
     */

    private class SendRequest extends AsyncTask<URL, String, String> {

        private WeakReference vRef;

        public SendRequest (View v) {
            vRef = new WeakReference(v);
        }

        /** @description : This method contains the code which needs to be executed in the background.
         *                 Here we can send results multiple times to the UI thread, by using the method
         *                 publishProgress().
         * @param urls
         * @return
         */
        protected String doInBackground(URL... urls) {
            try {

                Log.d("Devender", "Background thread started");

                URL url = urls[0]; // Getting the first URL


                HttpURLConnection con = (HttpURLConnection) url.openConnection(); // Creating the HTTP connection

                Log.d("Devender", "START: POST headers creation");
                con.setDoOutput(true);
                con.setRequestProperty("Content-Type", "application/json");
                con.setRequestProperty("Accept", "application/json");
                con.setRequestMethod("POST");
                Log.d("Devender", "END: POST headers creation");

          //    con.connect();
                Log.d("Devender", "START: JSON Object created");
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("heading","East" );
                jsonObj.put("id",1 );
                jsonObj.put("ignition","On" );
                jsonObj.put("lastMoved","2017-05-10T16:40:46.608+05:30" );
                jsonObj.put("lastUpdated","2017-05-10T16:40:46.608+05:30" );
                jsonObj.put("latitude", 51.50632);
                jsonObj.put("longitude", -0.12714);
                jsonObj.put("satellites", 4);
                jsonObj.put("vehicleNumber", "BD51SMR");
                jsonObj.put("vehicleSpeed", 54);
                Log.d("Devender", "END: JSON Object created");



                Log.d("Devender", "START: send request");
                DataOutputStream localDataOutputStream = new DataOutputStream(con.getOutputStream()); //Send request
                Log.d("Devender", "END: send request");

                localDataOutputStream.writeBytes(jsonObj.toString());

                Log.d("Devender", "JSON Object: " + jsonObj.toString());

                Log.d("Devender", "Object to string conversion");

                localDataOutputStream.flush();
                localDataOutputStream.close();



                //Get Response
                InputStream is = con.getInputStream();
                BufferedReader rd = new BufferedReader(new InputStreamReader(is));
                String line;
                StringBuffer response = new StringBuffer();

                while((line = rd.readLine()) != null) {
                    response.append(line);
                    response.append('\r');
                }

                rd.close();

//                StringEntity se = new StringEntity( json.toString());

                Log.d("Devender", "'POST' request was sent to URL : " + url);

                // Getting the response code for the HTTP request
                int responseCode = con.getResponseCode();

                // Response code of 200: Means that the request was successfully fulfilled.
                Log.d("Devender", "Response Code from the request: " + responseCode);

                Log.d("Devender", "Response: " + response);

                return response.toString();

            } catch (Exception e) {
                e.printStackTrace();
            }

            return "";
        }

        /**
         *  @description: This method contains the code which needs to be executed before the background
         *                processing starts.
         */
        protected  void onPreExecute(){

        }

        /**
         * @description: This method is called after doInBackground method completes processing.
         *               Result from doInBackground is passed to this method.
         * @param result
         */
        protected void onPostExecute(String result) {

            TextView txttitle = (TextView) vRef.get();

            txttitle.setText("Your public Ip is ");
        }

        /**
         * @description: This method receives progress updates from doInBackground method, which is published
         *               via publishProgress method, and this method can use this progress update to update the UI thread
         * @param progress
         */
        protected void onProgressUpdate(Integer... progress) {
            //setProgressPercent(progress[0]);
        }

    }
}