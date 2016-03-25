package com.teamspaghetti.globetrotter;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.teamspaghetti.globetrotter.userprocesses.WelcomePage;

/**
 * Created by msalihguler on 25.03.2016.
 */
public class SplashScreen extends Activity {
    SharedPreferences sharedPreferences;
    String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        sharedPreferences=getSharedPreferences("appPrefs",0);
        username = sharedPreferences.getString("username", "default_name");
        Thread timerThread = new Thread(){
            public void run(){
                try{
                    sleep(3000);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }finally{
                    if(username.equals("default_name")) {
                        Intent intent = new Intent(SplashScreen.this, WelcomePage.class);
                        startActivity(intent);
                        finish();
                    }else{
                        Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        };
        timerThread.start();
    }
}
