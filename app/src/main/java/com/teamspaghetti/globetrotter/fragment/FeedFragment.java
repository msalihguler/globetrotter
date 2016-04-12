package com.teamspaghetti.globetrotter.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.teamspaghetti.globetrotter.R;
import com.teamspaghetti.globetrotter.mapoperations.MapActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by msalihguler on 12.03.2016.
 */
public class FeedFragment extends Fragment {
    FloatingActionButton fab;
    ProgressBar progressBar;
    JSONArray answer;
    String url = "http://192.168.1.159:3000/getroutes";
    int pagenumber = 0;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.feed_mainholder, container, false);
        progressBar = (ProgressBar)rootView.findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);

        fab = (FloatingActionButton) rootView.findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (getActivity(), MapActivity.class);
                startActivity(intent);

            }
        });
        new LatestFeed().execute(url);
        return rootView;

    }
    class LatestFeed extends AsyncTask<String, String, Integer> {
        @Override
        protected void onPreExecute() {
            getActivity().setProgressBarIndeterminateVisibility(true);
        }

        @Override
        protected Integer doInBackground(String... params) {
            Integer result = 0;
            HttpURLConnection urlConnection;
            try {
                URL url = new URL(params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestMethod("POST");
                String charset = "UTF-8";
                pagenumber++;
                String s = "{\"pagenumber\":\"" + URLEncoder.encode(String.valueOf(pagenumber), charset)+"\"}";


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
                    Log.e("status", response.toString());

                    JSONObject jsonObject = new JSONObject(response.toString());
                    answer = new JSONArray();
                    answer = jsonObject.getJSONArray("routes");
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
            progressBar.setVisibility(View.GONE);

        }

    }
}
