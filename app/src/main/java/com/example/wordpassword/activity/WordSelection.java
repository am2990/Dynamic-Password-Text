package com.example.wordpassword.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.example.wordpassword.R;
import com.example.wordpassword.com.example.wordpassword.util.Constants;
import com.example.wordpassword.com.example.wordpassword.util.WordModel;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class WordSelection extends AppCompatActivity {

    private final String TAG = "WordSelection";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_selection);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        WordModel wm = new WordModel();
        //get the list of words
        Bundle extra = getIntent().getBundleExtra("extra");
        ArrayList<String> wordList = (ArrayList<String>) extra.getSerializable("wordArrayList");

        //async task which gets list of synonyms, antonyms, similar words and updates the object
        new WordAPITask().execute("kill");

        //build the Word Model Object
    }

    class WordAPITask extends AsyncTask<String, Void, Boolean> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Boolean doInBackground(String... word) {
            try {

                String data = GET(Constants.WORD_API+word[0]+"/json");
                Log.d(TAG, data);
                JSONObject jsono = new JSONObject(data);
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return false;
        }

        protected void onPostExecute(Boolean result) {

        }

        public String GET(String urls) {

            HttpURLConnection urlConnection = null;
            String result = "";
            try {
                URL url = new URL(urls);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                if (in != null)
                    result = convertInputStreamToString(in);
                else
                    result = "Did not work!";

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                urlConnection.disconnect();
            }
            return result;
        }

        private String convertInputStreamToString(InputStream inputStream) throws IOException {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            String result = "";
            while ((line = bufferedReader.readLine()) != null)
                result += line;

            inputStream.close();
            return result;

        }

    }

}
