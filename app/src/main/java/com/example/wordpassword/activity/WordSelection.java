package com.example.wordpassword.activity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.wordpassword.R;
import com.example.wordpassword.util.Constants;
import com.example.wordpassword.util.WordModel;

import org.json.JSONArray;
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
    WordModel wm;
    ListView listView;
    ArrayAdapter<String> adapter;
    Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_selection);

        mContext = this;

        TextView tv = (TextView)findViewById(R.id.textView);
        listView = (ListView) findViewById(R.id.list);

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

        wm = new WordModel();
        //get the list of words
        Bundle extra = getIntent().getBundleExtra("extra");
        ArrayList<String> wordList = (ArrayList<String>) extra.getSerializable("wordArrayList");


        //async task which gets list of synonyms, antonyms, similar words and updates the object
        new WordAPITask().execute("kill");
        tv.setText("kill");


        String[] words = wm.getWordList("synonyms", "kill");
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_multiple_choice, words);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listView.setAdapter(adapter);

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
                Log.d(TAG, (data!=null?"Success":"Failure"));
                createWordModel(word[0], data);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        private void createWordModel(String key, String data) {

            try {
                JSONObject obj = new JSONObject(data);

                if(obj.has("adjective")){
                    JSONObject json_o = (JSONObject) obj.get("adjective");

                    if(json_o.has("syn")){
                        JSONArray arr = new JSONArray(json_o.get("syn").toString());
                        for(int i = 0 ; i < arr.length(); i++ ){
                            wm.addSynonyms(key, arr.get(i).toString());
                        }
                    }
                    if(json_o.has("ant")){
                        JSONArray arr = new JSONArray(json_o.get("ant").toString());
                        for(int i = 0 ; i < arr.length(); i++ ){
                            wm.addAntonyms(key , arr.get(i).toString());
                        }
                    }
                    if(json_o.has("sim")){
                        JSONArray arr = new JSONArray(json_o.get("sim").toString());
                        for(int i = 0 ; i < arr.length(); i++ ){
                            wm.addSimilar(key, arr.get(i).toString());
                        }
                    }

                    if(json_o.has("rel")){
                        JSONArray arr = new JSONArray(json_o.get("rel").toString());
                        for(int i = 0 ; i < arr.length(); i++ ){
                            wm.addSimilar(key ,arr.get(i).toString());
                        }
                    }
                }

                if(obj.has("verb")){
                    JSONObject json_o = (JSONObject) obj.get("verb");

                    if(json_o.has("syn")){
                        JSONArray arr = new JSONArray(json_o.get("syn").toString());
                        for(int i = 0 ; i < arr.length(); i++ ){
                            wm.addSynonyms(key, arr.get(i).toString());
                        }
                    }
                    if(json_o.has("ant")){
                        JSONArray arr = new JSONArray(json_o.get("ant").toString());
                        for(int i = 0 ; i < arr.length(); i++ ){
                            wm.addAntonyms(key, arr.get(i).toString());
                        }
                    }
                    if(json_o.has("sim")){
                        JSONArray arr = new JSONArray(json_o.get("sim").toString());
                        for(int i = 0 ; i < arr.length(); i++ ){
                            wm.addSimilar(key, arr.get(i).toString());
                        }
                    }

                    if(json_o.has("rel")){
                        JSONArray arr = new JSONArray(json_o.get("rel").toString());
                        for(int i = 0 ; i < arr.length(); i++ ){
                            wm.addSimilar(key, arr.get(i).toString());
                        }
                    }
                }
                if(obj.has("noun")){
                    JSONObject json_o = (JSONObject) obj.get("verb");

                    if(json_o.has("syn")){
                        JSONArray arr = new JSONArray(json_o.get("syn").toString());
                        for(int i = 0 ; i < arr.length(); i++ ){
                            wm.addSynonyms(key, arr.get(i).toString());
                        }
                    }
                    if(json_o.has("ant")){
                        JSONArray arr = new JSONArray(json_o.get("ant").toString());
                        for(int i = 0 ; i < arr.length(); i++ ){
                            wm.addAntonyms(key, arr.get(i).toString());
                        }
                    }
                    if(json_o.has("sim")){
                        JSONArray arr = new JSONArray(json_o.get("sim").toString());
                        for(int i = 0 ; i < arr.length(); i++ ){
                            wm.addSimilar(key, arr.get(i).toString());
                        }
                    }

                    if(json_o.has("rel")){
                        JSONArray arr = new JSONArray(json_o.get("rel").toString());
                        for(int i = 0 ; i < arr.length(); i++ ){
                            wm.addSimilar(key, arr.get(i).toString());
                        }
                    }
                }

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

        protected void onPostExecute(Boolean result) {
            String[] words = wm.getWordList("synonyms", "kill");
            adapter = new ArrayAdapter<String>(mContext,
                    android.R.layout.simple_list_item_multiple_choice, words);
            listView.setAdapter(adapter);

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
