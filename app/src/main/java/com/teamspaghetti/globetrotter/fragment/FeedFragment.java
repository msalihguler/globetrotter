package com.teamspaghetti.globetrotter.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.teamspaghetti.globetrotter.R;
import com.teamspaghetti.globetrotter.mapoperations.MapActivity;

/**
 * Created by msalihguler on 12.03.2016.
 */
public class FeedFragment extends Fragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.feed_mainholder, container, false);
        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (getActivity(), MapActivity.class);
                startActivity(intent);
            }
        });
        return rootView;

    }

}
