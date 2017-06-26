package testing.gps_service;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RestCall extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("Devender","Entering onCreate function");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rest_call);
        Log.d("Devender","Exiting onCreate function");
    }

    //  Method : restCall
    public void restCall(View view){
        try {
            Log.d("Devender","Entering rest call");
            new SendRequest(view).execute(new URL("http://ip.jsontest.com/"));
            Log.d("Devender","Exiting rest call");
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    // Class    : SendRequest
    // Modifier : Private
    private class SendRequest extends AsyncTask<URL, String, String> {

        private WeakReference vRef;

        public SendRequest (View v) {
            vRef = new WeakReference(v);
        }

        protected String doInBackground(URL... urls) {
            try {

                // Getting the first URL
                URL url = urls[0];

                // Creating the HTTP connection
                HttpURLConnection con = (HttpURLConnection) url.openConnection();

                // optional default is GET
                // Setting the request method to be GET
                con.setRequestMethod("GET");

                // Getting the response code for the HTTP request
                int responseCode = con.getResponseCode();

                // Creating a map for the key-value attributes of the header element
                Map<String, List<String>> headers = con.getHeaderFields();

                // Printing the elements to the screen
                Log.d("Devender", "'GET' request was sent to URL : " + url);
               // System.out.println();

                Log.d("Devender", "Response Code of the request was: " + responseCode);
               // System.out.println();

                // Converting the input from the connection to the buffered reader
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));

                String inputLine;

                StringBuffer response = new StringBuffer();

                // Appending the connection input the string
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }

                /*StringBuffer responseHeader = new StringBuffer();
                for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
                    responseHeader.append( entry.getKey() + "::" + entry.getValue());
                    responseHeader.append("\n");
                }
                return responseHeader.toString();*/

                return response.toString();

            } catch (Exception e) {
                e.printStackTrace();
            }

            return "";
        }


        protected void onProgressUpdate(Integer... progress) {
            //setProgressPercent(progress[0]);
        }

        protected void onPostExecute(String result) {
            //Toast.makeText(getApplicationContext() , result, Toast.LENGTH_LONG).show();

            //TextView txttitle = (TextView) vRef.get();
            //txttitle.setText(result);

            Gson g = new Gson();

            IP ip=g.fromJson(result,IP.class);

            TextView txttitle = (TextView) vRef.get();

            txttitle.setText("Your public Ip is "+ip.getIp());
        }
    }
}