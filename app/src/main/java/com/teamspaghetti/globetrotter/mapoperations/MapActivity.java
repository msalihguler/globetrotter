package com.teamspaghetti.globetrotter.mapoperations;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.teamspaghetti.globetrotter.CreateTravelPlan;
import com.teamspaghetti.globetrotter.R;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by msalihguler on 15.03.2016.
 */
public class MapActivity extends FragmentActivity implements OnMapReadyCallback {
    int markerCounter;
    GoogleMap mMap;
    ImageButton drawroute,createtour;
    ArrayList<Marker> markerList;
    ArrayList<Marker> markerListClone;
    GPSTracker gps;
    double latitude,longitude;
    ProgressDialog pDialog;
    String cityname="";
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maps);
        markerList = new ArrayList<Marker>();
        markerListClone = new ArrayList<Marker>();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        drawroute=(ImageButton)findViewById(R.id.drawroute);
        createtour=(ImageButton)findViewById(R.id.goforsaving);

        pDialog = new ProgressDialog(this);
        createtour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(markerList.size()>0) {
                    String latitude ="";
                    String longitude = "";
                    String marker_details="";
                    for(int i=0;i<markerList.size();i++){
                        latitude += markerList.get(i).getPosition().latitude+"_";
                        longitude += markerList.get(i).getPosition().longitude+"_";
                        marker_details += markerList.get(i).getTitle()+"_";
                    }
                    Intent intent = new Intent(MapActivity.this, CreateTravelPlan.class);
                    intent.putExtra("latitude",latitude);
                    intent.putExtra("longitude",longitude);
                    intent.putExtra("details",marker_details);
                    startActivity(intent);
                }else{
                    Toast.makeText(MapActivity.this,getResources().getString(R.string.nomarker),Toast.LENGTH_SHORT).show();
                }

            }
        });
        drawroute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!markerList.isEmpty()){
                    pDialog.setMessage(getResources().getString(R.string.pleasewait));
                    mMap.clear();
                    markerListClone.clear();
                    markerListClone= (ArrayList<Marker>) markerList.clone();
                    markerList.clear();
                    pDialog.show();
                    for(int i=0;i<markerListClone.size();i++) {
                       Marker marker =  mMap.addMarker(new MarkerOptions().position(markerListClone.get(i).getPosition()).title(markerListClone.get(i).getTitle()));
                        markerList.add(marker);
                    }
                for(markerCounter=0;markerCounter<markerList.size()-1;markerCounter++){

                    String url = getDirectionsUrl(markerList.get(markerCounter).getPosition(), markerList.get(markerCounter+1).getPosition());

                    DownloadTask downloadTask = new DownloadTask();

                    downloadTask.execute(url);
                }

                }else{
                    mMap.clear();
                }


            }
        });
        gps = new GPSTracker(this);

        // check if GPS enabled
        if(gps.canGetLocation()){

           latitude = gps.getLatitude();
           longitude = gps.getLongitude();

        }else{
            gps.showSettingsAlert();
        }
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                Marker m_id = mMap.addMarker(new MarkerOptions().position(latLng).title(getResources().getString(R.string.titlemarker)));
                m_id.showInfoWindow();

                markerList.add(m_id);
            }
        });


        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                    markerList.remove(marker);
                    marker.remove();
                    Log.e("markers", markerList.toString());
                return false;
            }
        });

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(final Marker marker) {
                AlertDialog.Builder alert = new AlertDialog.Builder(MapActivity.this, R.style.AppCompatAlertDialogStyle);
                final EditText edittext = new EditText(MapActivity.this);

                alert.setMessage(getResources().getString(R.string.changetitle));

                alert.setView(edittext);

                alert.setPositiveButton(getResources().getString(R.string.change), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        marker.setTitle(edittext.getText().toString());
                        marker.showInfoWindow();
                    }
                });

                alert.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // what ever you want to do with No option.
                    }
                });

                alert.show();
            }
        });
                LatLng currentlocation = new LatLng(latitude, longitude);
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> addresses = null;
                try {
                    addresses = geocoder.getFromLocation(latitude, longitude, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                mMap.addMarker(new MarkerOptions().position(currentlocation).title(cityname)).showInfoWindow();
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(currentlocation)      // Sets the center of the map to Mountain View
                .zoom(18)                   // Sets the zoom
                .bearing(90)                // Sets the orientation of the camera to east
                .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    private String getDirectionsUrl(LatLng origin,LatLng dest){

        // Origin of route
        String str_origin = "origin="+origin.latitude+","+origin.longitude;

        // Destination of route
        String str_dest = "destination="+dest.latitude+","+dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin+"&"+str_dest+"&"+sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters+"&mode=walking";
        Log.e("url",url);
        return url;
    }
    /** A method to download json data from url */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while( ( line = br.readLine()) != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            Log.d("ex", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    // Fetches data from url passed
    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try{
                // Fetching the data from web service
                data = downloadUrl(url[0]);

            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }

            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            Log.e("number",String.valueOf(markerCounter));
            parserTask.execute(result);
        }
    }

    /** A class to parse the Google Places in JSON format */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> >{

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try{
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            }catch(Exception e){
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();

            // Traversing through all the routes
            for(int i=0;i<result.size();i++){
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for(int j=0;j<path.size();j++){
                    HashMap<String,String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(5);
                lineOptions.color(MapActivity.this.getResources().getColor(R.color.colorPrimary));
            }

            // Drawing polyline in the Google Map for the i-th route
            mMap.addPolyline(lineOptions);
            pDialog.dismiss();

        }
    }
}