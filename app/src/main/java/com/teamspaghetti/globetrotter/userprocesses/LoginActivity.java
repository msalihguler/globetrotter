package com.teamspaghetti.globetrotter.userprocesses;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
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
 * Created by msalihguler on 11.03.2016.
 */
public class LoginActivity extends Activity{
    Button login;
    TextView title;
    EditText email,password;
    ProgressDialog pDialog;
    String url = "http://localhost:3000/login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);
        login=(Button)findViewById(R.id.loginrequest);
        title = (TextView)findViewById(R.id.title_logo);
        email = (EditText)findViewById(R.id.input_email);
        password=(EditText)findViewById(R.id.input_password);
        Typeface font = Typeface.createFromAsset(getAssets(), "deneme.ttf");
        login.setTypeface(font);
        title.setTypeface(font);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(login.getText().toString().trim()==""){
                    Toast.makeText(LoginActivity.this, getString(R.string.fillusername), Toast.LENGTH_SHORT).show();
                }else if(password.getText().toString().trim()==""){
                    Toast.makeText(LoginActivity.this,getString(R.string.fillpassword),Toast.LENGTH_SHORT).show();
                }else{
                        new LoginTask().execute(url);
                }

            }
        });
    }
    public class LoginTask extends AsyncTask<String, Void, Integer> {

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(LoginActivity.this);
            pDialog.setMessage(getString(R.string.logginin));
            pDialog.setCancelable(false);
            pDialog.show();
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

                String s = "{\"email\":\"" + URLEncoder.encode(email.getText().toString(), charset)+"\",";
                s += "\"password\":\"" + URLEncoder.encode(password.getText().toString(), charset)+"\"}";

                urlConnection.setFixedLengthStreamingMode(s.getBytes().length);
                PrintWriter out = new PrintWriter(urlConnection.getOutputStream());
                out.print(s);
                out.close();
                int statusCode = urlConnection.getResponseCode();

                Log.e("status", String.valueOf(statusCode));

                if (statusCode == 255) {
                    BufferedReader r = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = r.readLine()) != null) {
                        response.append(line);
                    }
                    JSONObject jsonObject = new JSONObject(response.toString());
                    Log.e("id",jsonObject.get("userid").toString());


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
        protected void onPostExecute(Integer result) {
            pDialog.dismiss();

            if (result == 1) {
                Toast.makeText(LoginActivity.this, getString(R.string.loginsuccess), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                WelcomePage.mActivity.finish();
                finish();
            } else {
                if(result==258){
                    Toast.makeText(LoginActivity.this, getString(R.string.wrongpassword), Toast.LENGTH_SHORT).show();

                }else if(result==212){
                    Toast.makeText(LoginActivity.this, getString(R.string.nosuchuser), Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(LoginActivity.this, String.valueOf(result)+" "+getString(R.string.errorcodeoccurred), Toast.LENGTH_SHORT).show();
                }

            }
        }
    }
}
