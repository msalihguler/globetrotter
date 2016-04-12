package com.teamspaghetti.globetrotter.adapters;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.teamspaghetti.globetrotter.Model.Routes;
import com.teamspaghetti.globetrotter.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by msalihguler on 04.04.2016.
 */
public class FeedAdapter extends ArrayAdapter<Routes> {
    String link = "http://192.168.1.159:3000/getdata";
    // View lookup cache
    private static class ViewHolder {
        TextView title;
        TextView detail;
        TextView likenumber;
        TextView commentnumber;
        LinearLayout like;
        LinearLayout comment;
        CardView routeholder;
    }

    public FeedAdapter(Context context, ArrayList<Routes> routes) {
        super(context, R.layout.feed_item, routes);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the cdata item for this position
        final Routes route = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.feed_item, parent, false);
            viewHolder.title = (TextView)convertView.findViewById(R.id.title_item);
            viewHolder.detail = (TextView)convertView.findViewById(R.id.detail_item);
            viewHolder.likenumber = (TextView)convertView.findViewById(R.id.likenumber_item);
            viewHolder.commentnumber = (TextView)convertView.findViewById(R.id.comment_number);
            viewHolder.like = (LinearLayout)convertView.findViewById(R.id.like_item);
            viewHolder.comment = (LinearLayout)convertView.findViewById(R.id.comment_button);
            viewHolder.routeholder = (CardView)convertView.findViewById(R.id.cardroutes);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Populate the data into the template view using the data object
        viewHolder.title.setText(route.getTitle());
        viewHolder.detail.setText(route.getDetail());
        viewHolder.likenumber.setText(route.getLikes());
        viewHolder.commentnumber.setText(route.getComments());
        viewHolder.routeholder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),route.getID(),Toast.LENGTH_SHORT).show();
            }
        });
        viewHolder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               new Thread(new Runnable() {
                   @Override
                   public void run() {
                       Integer result = 0;
                       HttpURLConnection urlConnection;
                       try {
                           URL url = new URL(link);
                           urlConnection = (HttpURLConnection) url.openConnection();
                           urlConnection.setRequestProperty("Content-Type", "application/json");
                           urlConnection.setRequestMethod("POST");

                           String s = "{\"type\":\"like\",";
                           s+="\"id\":\"" +route.getID()+"\"}";

                           urlConnection.setFixedLengthStreamingMode(s.getBytes().length);
                           PrintWriter out = new PrintWriter(urlConnection.getOutputStream());
                           out.print(s);
                           out.close();
                           int statusCode = urlConnection.getResponseCode();
                           Log.d("result", String.valueOf(statusCode));


                           if (statusCode == 253) {
                               BufferedReader r = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                               StringBuilder response = new StringBuilder();
                               String line;
                               while ((line = r.readLine()) != null) {
                                   response.append(line);
                               }

                            //   JSONObject jsonObject = new JSONObject(response.toString());

                               result = 1; // Successful
                           }
                       } catch (Exception e) {
                           Log.d("sadwd", e.getLocalizedMessage());
                       }
                   }
               }).start();
            }
        });
        viewHolder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"comment clicked",Toast.LENGTH_SHORT).show();

            }
        });
        // Return the completed view to render on screen
        return convertView;
    }

}