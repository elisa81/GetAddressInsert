package com.example.edu.getaddressinsert;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.BitSet;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button searchBtn;
    EditText locationInput;
    TextView textViewResult;
    String APIkey = "5159694f6364656136336157466d56";
    String findLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);

    searchBtn = findViewById (R.id.searchBtn);
    searchBtn.setOnClickListener (this);

    locationInput = findViewById (R.id.locationInput);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.searchBtn:
                textViewResult =findViewById (R.id.textViewResult);
                String location = ((EditText)findViewById (R.id.locationInput)).getText().toString();
                SeoulDataTask task = new SeoulDataTask();
                try {
                    String findLocation = task.execute("잠실").get();
                    textViewResult.setText(findLocation);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
        }

    }

    private class SeoulDataTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String id = params[0];
            String findLocation = null;

            String urlString="http://openAPI.seoul.go.kr:8088/" + APIkey + "/xml/SearchInfoBySubwayNameService/1/8/" + id +"/";
            HttpURLConnection urlConnection = null;
            InputStream jsonResult = null;
            byte[] buffer = null;
            try {
                URL url = new URL(urlString);
                urlConnection=(HttpURLConnection)url.openConnection();
                jsonResult = urlConnection.getInputStream();
                buffer = new byte[1000];
                jsonResult.read(buffer);
                findLocation = new String(buffer);
            } catch (MalformedURLException e) {
                e.printStackTrace ();
            } catch (IOException e) {
                e.printStackTrace ();
            } finally {
                try {
                    if (buffer!=null) buffer=null;
                    if (jsonResult!=null) jsonResult.close();
                    if (urlConnection!=null) urlConnection.disconnect();
                } catch (IOException e) {
                    e.printStackTrace ();
                }

            }

            return findLocation;
        }
    }
}