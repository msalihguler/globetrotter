package com.teamspaghetti.globetrotter.userprocesses;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.teamspaghetti.globetrotter.MainActivity;
import com.teamspaghetti.globetrotter.R;

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
public class RegisterActivity extends Activity {
    Button register;
    EditText namesurname,email,password,password2,username;
    ProgressDialog pDialog;
    String url = "http://192.168.1.159:3000/signup";
    TextView title;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_page);
        sharedPreferences=getSharedPreferences("appPrefs",0);
        Typeface font = Typeface.createFromAsset(getAssets(), "deneme.ttf");
        title=(TextView)findViewById(R.id.title_logo);
        title.setTypeface(font);
        register = (Button)findViewById(R.id.registerrequest);
        register.setTypeface(font);
        namesurname = (EditText)findViewById(R.id.input_nameandsurname);
        email = (EditText)findViewById(R.id.input_email);
        password = (EditText)findViewById(R.id.input_password);
        password2 = (EditText)findViewById(R.id.input_password2);
        username = (EditText)findViewById(R.id.input_username);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(email.getText().toString().trim().equals("")){
                    Toast.makeText(RegisterActivity.this,getString(R.string.fillemail),Toast.LENGTH_SHORT).show();
                }else if(username.getText().toString().trim().equals("")){
                    Toast.makeText(RegisterActivity.this,getString(R.string.fillusername),Toast.LENGTH_SHORT).show();
                }else if(password.getText().toString().trim().equals("")){
                    Toast.makeText(RegisterActivity.this,getString(R.string.fillpassword),Toast.LENGTH_SHORT).show();
                }else if(password2.getText().toString().trim().equals("")){
                    Toast.makeText(RegisterActivity.this,getString(R.string.fillpassword),Toast.LENGTH_SHORT).show();
                }else if(namesurname.getText().toString().trim().equals("")){
                    Toast.makeText(RegisterActivity.this,getString(R.string.fillname),Toast.LENGTH_SHORT).show();
                }
                else{
                    if(!isValidEmail(email.getText().toString())){
                        Toast.makeText(RegisterActivity.this,getString(R.string.validmail),Toast.LENGTH_SHORT).show();
                    }
                    else if(!(password.getText().toString().length()>5)||!(password2.getText().toString().length()>5)){
                        Toast.makeText(RegisterActivity.this,getString(R.string.shortpassword),Toast.LENGTH_SHORT).show();
                    }
                    else if(!password.getText().toString().trim().equals(password2.getText().toString().trim())){
                        Toast.makeText(RegisterActivity.this,getString(R.string.nomatchpassword),Toast.LENGTH_SHORT).show();
                    }else{
                        new AsyncHttpTask().execute(url);
                    }
                }
            }
        });

    }
    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }


    public class AsyncHttpTask extends AsyncTask<String, Void, Integer> {
        String userid,user;
        int result = 0;
        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(RegisterActivity.this);
            pDialog.setMessage(getString(R.string.registeringuser));
            pDialog.setCancelable(false);
            pDialog.show();
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

                String s = "{\"email\":\"" + URLEncoder.encode(email.getText().toString(), charset)+"\",";
                s += "\"username\":\"" + URLEncoder.encode(username.getText().toString(), charset)+"\",";
                s += "\"password\":\"" + URLEncoder.encode(password.getText().toString(), charset)+"\",";
                s += "\"nameandsurname\":\"" + URLEncoder.encode(namesurname.getText().toString(), charset)+"\"}";

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
                    Log.e("id",jsonObject.get("user_id").toString());
                    user = jsonObject.get("username").toString();
                    userid = jsonObject.get("user_id").toString();
                    sharedPreferences.edit().putString("username",user).commit();
                    sharedPreferences.edit().putString("userid",userid).commit();
                    result = 1; // Successful
                } else {
                    result = 0; //"Failed to fetch data!";
                }
            } catch (Exception e) {
                Log.d("sadwd", e.getLocalizedMessage());
            }
            return result; //"Failed to fetch data!";
        }

        @Override
        protected void onPostExecute(Integer result) {
           pDialog.dismiss();

            if (result == 1) {
                Toast.makeText(RegisterActivity.this, getString(R.string.registrationsuccess), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
                WelcomePage.mActivity.finish();
                finish();
            } else {

                Toast.makeText(RegisterActivity.this, getString(R.string.registrationfail), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
