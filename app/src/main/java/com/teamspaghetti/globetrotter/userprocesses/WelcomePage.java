package com.teamspaghetti.globetrotter.userprocesses;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ViewFlipper;

import com.teamspaghetti.globetrotter.R;

/**
 * Created by msalihguler on 10.03.2016.
 */
public class WelcomePage extends Activity{
    Animation fade_in, fade_out;
    ViewFlipper viewFlipper;
    Button register,login;
    public static Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcomepage);
        this.mActivity = this;
        Typeface font = Typeface.createFromAsset(getAssets(), "deneme.ttf");
        login = (Button)findViewById(R.id.tologin);
        register = (Button)findViewById(R.id.toregister);
        login.setTypeface(font);
        register.setTypeface(font);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomePage.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomePage.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
        viewFlipper = (ViewFlipper) this.findViewById(R.id.bckgrndViewFlipper1);
        fade_in = AnimationUtils.loadAnimation(this,
                android.R.anim.fade_in);
        fade_out = AnimationUtils.loadAnimation(this,
                android.R.anim.fade_out);
        viewFlipper.setInAnimation(fade_in);
        viewFlipper.setOutAnimation(fade_out);
        viewFlipper.setAutoStart(true);
        viewFlipper.setFlipInterval(1250);
        viewFlipper.startFlipping();
    }

}
