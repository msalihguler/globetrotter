package com.teamspaghetti.globetrotter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by msalihguler on 24.03.2016.
 */
public class CreateTravelPlan extends Activity {

    EditText title,explanation;
    Button saveRoute;
    String url = "http://192.168.1.244:3000/createroute";
    ProgressDialog pDialog;
    String title_send,explanation_send,city,latitude,longitude,location_details,creator;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.createtravelplan);
        sharedPreferences=getSharedPreferences("appPrefs",0);
        title = (EditText)findViewById(R.id.input_tourtitle);
        explanation = (EditText)findViewById(R.id.input_tourdetail);
        saveRoute=(Button)findViewById(R.id.saveroute);
        city = "Ankara";
        latitude=getIntent().getExtras().getString("latitude");
        longitude=getIntent().getExtras().getString("longitude");
        location_details=getIntent().getExtras().getString("details");

        saveRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title_send=title.getText().toString();
                explanation_send=explanation.getText().toString();
                new SaveRoute().execute(url);
            }
        });
    }
    class SaveRoute extends AsyncTask<String, String, Integer> {
        int result=0;
        @Override
        protected void onPreExecute() {
            pDialog= new ProgressDialog(CreateTravelPlan.this);
            pDialog.setMessage("Kaydediliyor");
            pDialog.show();
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(String... params) {

            HttpURLConnection urlConnection;
            try {
                URL url = new URL(params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestMethod("POST");
                String charset = "UTF-8";

                String s = "{\"title\":\"" + URLEncoder.encode(title_send, charset)+"\",";
                    s += "\"latitude\":\"" + URLEncoder.encode(latitude, charset)+"\",";
                s += "\"longitude\":\"" + URLEncoder.encode(longitude, charset)+"\",";
                s += "\"location_details\":\"" + URLEncoder.encode(location_details, charset)+"\",";
                s += "\"city\":\"" + URLEncoder.encode(city, charset)+"\",";
                s += "\"creator\":\"" + URLEncoder.encode(sharedPreferences.getString("username","default_name"), charset)+"\",";
                s += "\"detail\":\"" + URLEncoder.encode(explanation_send, charset)+"\"}";

                urlConnection.setFixedLengthStreamingMode(s.getBytes().length);
                PrintWriter out = new PrintWriter(urlConnection.getOutputStream());
                out.print(s);
                out.close();
                int statusCode = urlConnection.getResponseCode();

                Log.e("status", String.valueOf(statusCode));

                if (statusCode == 253) {
                    BufferedReader r = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = r.readLine()) != null) {
                        response.append(line);
                    }
                    JSONObject jsonObject = new JSONObject(response.toString());

                    result = 1; // Successful
                } else {
                    result = statusCode; //"Failed to fetch data!";
                }
            } catch (Exception e) {
                Log.d("sadwd", e.getLocalizedMessage());
            }
            return result; //"Failed to fetch data!";
        }
        @Override
        protected void onPostExecute(Integer s) {
            super.onPostExecute(s);
            pDialog.dismiss();
            if(result==1){
                Toast.makeText(CreateTravelPlan.this,getString(R.string.savesuccesfull),Toast.LENGTH_SHORT).show();
                finish();
            }else{
                Toast.makeText(CreateTravelPlan.this,String.valueOf(result)+" error occured",Toast.LENGTH_SHORT).show();

            }
        }

    }
}
