package com.teamspaghetti.globetrotter;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

/**
 * Created by Salih on 4/18/2016.
 */
public class FeedDetail extends Activity {
    TextView detail_title;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feed_detail);
        detail_title = (TextView)findViewById(R.id.title_detail);

        detail_title.setText(getIntent().getExtras().getString("title"));

    }
}
