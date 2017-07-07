package testing.gps_service;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class GET_RestCall extends AppCompatActivity {


    // Method where we initialize the activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d("Devender","Starting of GET Rest Call");
        Log.d("Devender","Entering onCreate function");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_rest_call);
        Log.d("Devender","Exiting onCreate function");
    }

    //  Method : restCall
    //  Description : This gets executed, when we click the "Check your IP button"  button
    public void restCall(View view){
        try {
            Log.d("Devender","Entering rest call");

            // .execute() method is used to start the background task.
            new SendRequest(view).execute(new URL("http://ip.jsontest.com/"));
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

                // Printing the elements to the screen
                Log.d("Devender", "Background thread started");

                // Getting the first URL
                URL url = urls[0];

                // Creating the HTTP connection
                HttpURLConnection con = (HttpURLConnection) url.openConnection();

                // optional default is GET
                // Setting the request method to be GET
                con.setRequestMethod("GET");

                Log.d("Devender", "'GET' request was sent to URL : " + url);

                // Getting the response code for the HTTP request
                int responseCode = con.getResponseCode();

                // Response code of 200: Means that the request was successfully fulfilled.
                Log.d("Devender", "Response Code from the request: " + responseCode);

                // Creating a map for the key-value attributes of the header element
                Map<String, List<String>> headers = con.getHeaderFields();

                // Converting the input from the connection to the buffered reader
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));

                String inputLine;

                StringBuffer response = new StringBuffer();


                // Appending the connection input the string
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }

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

            Gson g = new Gson();

            IP ip=g.fromJson(result,IP.class);

            TextView txttitle = (TextView) vRef.get();

            txttitle.setText("Your public Ip is "+ip.getIp());
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