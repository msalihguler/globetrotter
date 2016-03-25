package com.teamspaghetti.globetrotter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by msalihguler on 24.03.2016.
 */
public class CreateTravelPlan extends Activity {

    EditText title,explanation;
    Button saveRoute;
    String url = "192.168.1.251:3000/saveroute";
    ProgressDialog pDialog;
    String title_send,explanation_send,city,latitude,longitude,location_details;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.createtravelplan);
        title = (EditText)findViewById(R.id.input_tourtitle);
        explanation = (EditText)findViewById(R.id.input_tourdetail);
        saveRoute=(Button)findViewById(R.id.saveroute);
        title_send=title.getText().toString();
        explanation_send=explanation.getText().toString();
        latitude=getIntent().getExtras().getString("latitude");
        longitude=getIntent().getExtras().getString("longitude");
        location_details=getIntent().getExtras().getString("details");

        saveRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SaveRoute().execute();
            }
        });
    }
    class SaveRoute extends AsyncTask<String,String,String>{
        @Override
        protected void onPreExecute() {
            pDialog= new ProgressDialog(CreateTravelPlan.this);
            pDialog.setMessage("Kaydediliyor");
            pDialog.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {


            return null;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pDialog.dismiss();
        }

    }
}
