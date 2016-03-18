package com.teamspaghetti.globetrotter.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.teamspaghetti.globetrotter.R;

/**
 * Created by msalihguler on 12.03.2016.
 */
public class MessageFragment extends Fragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.feed_fragment, container, false);
        return rootView;
    }

}