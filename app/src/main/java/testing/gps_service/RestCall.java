package testing.gps_service;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rest_call);

    }

    public void restCall(View view){
        try {

            new SendRequest(view).execute(new URL("http://ip.jsontest.com/"));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private class SendRequest extends AsyncTask<URL, String, String> {

        private WeakReference vRef;

        public SendRequest (View v) {
            vRef = new WeakReference(v);
        }

        protected String doInBackground(URL... urls) {
            try {

                URL url = urls[0];
                HttpURLConnection con = (HttpURLConnection) url.openConnection();

                // optional default is GET
                con.setRequestMethod("GET");
                //con.setRequestMethod("HEAD");

                //add request header
                //con.setRequestProperty("User-Agent", USER_AGENT);


                int responseCode = con.getResponseCode();
                Map<String, List<String>> headers = con.getHeaderFields();

                System.out.println("Sending 'GET' request to URL : " + url);
                System.out.println("Response Code : " + responseCode);

                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

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