package com.example.wordpassword.activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wordpassword.CSVeditor;
import com.example.wordpassword.R;
import com.example.wordpassword.SampleTagCloud;
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
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;

public class WordSelection extends AppCompatActivity {

    private final String TAG = "WordSelection";
    private static WordModel wm;

    ListView listView;
    ArrayAdapter<String> adapter;
    Context mContext;
    Intent iuser,icheckuser;
    View headerView;
    TextView titleView;
    Button nextButton;

    int type = 0;
    String str_usern,checkuser;
    ArrayList<String> words;
    String titlePrefix = "Select Synonyms/Antonyms/Similar Words";
    private static int counter;
    ArrayList<String> selectedWords, notSelectedWords, wordList;
    HashMap<String, WordModel> word_hm = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_selection);

        mContext = this;

        words = new ArrayList<>();
//        TextView tv = (TextView) findViewById(R.id.textView);
        listView = (ListView) findViewById(R.id.list);
        counter = 0;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        iuser=getIntent();
        icheckuser = getIntent();

        checkuser = icheckuser.getStringExtra("checkuser");
        str_usern = iuser.getStringExtra("usern");
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Add your own words !!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                LayoutInflater layoutInflater
                        = (LayoutInflater)getBaseContext()
                        .getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupView = layoutInflater.inflate(R.layout.popup, null);
                final PopupWindow popupWindow = new PopupWindow(
                        popupView,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    popupWindow.setElevation(50.0f);
                }

                popupWindow.setFocusable(true);
                Button addBtn = (Button)popupView.findViewById(R.id.add);
                Button btnDismiss = (Button)popupView.findViewById(R.id.dismiss);
                final EditText addWord = (EditText)popupView.findViewById(R.id.et_word);
                addBtn.setOnClickListener(new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String newWord = addWord.getText().toString().trim();
                        Log.d(TAG, newWord);
                        if(newWord.equals("")){
                            Toast.makeText(mContext, "blank is not a word :(", Toast.LENGTH_LONG).show();
                        }
                        else if(words.contains(newWord)){
                            Toast.makeText(mContext, "Already exists !!", Toast.LENGTH_LONG).show();
                        }
                        else {
                            words.add(newWord);
                            adapter = new ArrayAdapter<String>(mContext,
                                    android.R.layout.simple_list_item_multiple_choice, words);
                            listView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            addWord.setText("");
                            Toast.makeText(mContext, "Added to list !!", Toast.LENGTH_LONG).show();
                        }
                    }
                });

                btnDismiss.setOnClickListener(new Button.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        popupWindow.dismiss();
                    }
                });

//                popupWindow.showAsDropDown(listView, 50, -30);
                popupWindow.showAtLocation(listView, Gravity.CENTER, 0 ,0);

            }});

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        nextButton = (Button)findViewById(R.id.nextbutton);
        nextButton.setEnabled(false);

        //get the list of words
        Bundle extra = getIntent().getBundleExtra("extra");
        wordList = (ArrayList<String>) extra.getSerializable("wordArrayList");
        type = extra.getInt(Constants.PASSWORD_TYPE);

        String word = wordList.get(counter);
        wm = new WordModel(word);
        word_hm.put(word, wm);
        selectedWords = new ArrayList<>();
        notSelectedWords = new ArrayList<>();

        //async task which gets list of synonyms, antonyms, similar words and updates the object
        new WordAPITask().execute(wordList.get(counter));
        headerView = getLayoutInflater().inflate(R.layout.listview_title, null);
        titleView = (TextView) headerView.findViewById(R.id.repaymentScreenTitle);
        titlePrefix = "Select Synonyms/Antonyms/Similar Words"; //TODO update title prefix
        titleView.setText(titlePrefix + " - " + wordList.get(counter).toUpperCase());
        listView.addHeaderView(headerView);
        counter++;

//        String[] words = wm.getWordList(type, wordList.get(0));
        String[] words = {};
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_multiple_choice, words);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View view, final int position,
                                    long id) {
                final View selectedView = view; // Save selected view in final variable**
                Object listItem = listView.getItemAtPosition(position);

                if(!selectedWords.contains(listItem.toString())) {
                    Log.d(TAG, listItem.toString() + " Adding to selected");
                    selectedWords.add(listItem.toString());
                    notSelectedWords.remove(listItem.toString());
                }
                else{
                    Log.d(TAG, listItem.toString() + " Removing from selected");
                    notSelectedWords.add(listItem.toString());
                    selectedWords.remove(listItem.toString());
                }
            }
        });
    }

    class WordAPITask extends AsyncTask<String, Void, Boolean> {

        String curr_word;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Boolean doInBackground(String... word) {
            try {
                curr_word = word[0];
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
                            wm.addWord(key, arr.get(i).toString(), Constants.SYNONYMS);
                            notSelectedWords.add(arr.get(i).toString());
                        }
                    }
                    if(json_o.has("ant")){
                        JSONArray arr = new JSONArray(json_o.get("ant").toString());
                        for(int i = 0 ; i < arr.length(); i++ ){
                            wm.addWord(key, arr.get(i).toString(), Constants.ANTONYMS);
                            notSelectedWords.add(arr.get(i).toString());
                        }
                    }
                    if(json_o.has("sim")){
                        JSONArray arr = new JSONArray(json_o.get("sim").toString());
                        for(int i = 0 ; i < arr.length(); i++ ){
                            wm.addSimilar(key, arr.get(i).toString());
                            notSelectedWords.add(arr.get(i).toString());
                        }
                    }

                    if(json_o.has("rel")) {
                        JSONArray arr = new JSONArray(json_o.get("rel").toString());
                        for(int i = 0 ; i < arr.length(); i++ ){
                            wm.addSimilar(key ,arr.get(i).toString());
                            notSelectedWords.add(arr.get(i).toString());
                        }
                    }
                }

                if(obj.has("verb")){
                    JSONObject json_o = (JSONObject) obj.get("verb");

                    if(json_o.has("syn")){
                        JSONArray arr = new JSONArray(json_o.get("syn").toString());
                        for(int i = 0 ; i < arr.length(); i++ ){
                            wm.addSynonyms(key, arr.get(i).toString());
                            notSelectedWords.add(arr.get(i).toString());
                        }
                    }
                    if(json_o.has("ant")){
                        JSONArray arr = new JSONArray(json_o.get("ant").toString());
                        for(int i = 0 ; i < arr.length(); i++ ){
                            wm.addWord(key, arr.get(i).toString(), Constants.ANTONYMS);
                            notSelectedWords.add(arr.get(i).toString());
                        }
                    }
                    if(json_o.has("sim")){
                        JSONArray arr = new JSONArray(json_o.get("sim").toString());
                        for(int i = 0 ; i < arr.length(); i++ ){
                            wm.addSimilar(key, arr.get(i).toString());
                            notSelectedWords.add(arr.get(i).toString());
                        }
                    }

                    if(json_o.has("rel")){
                        JSONArray arr = new JSONArray(json_o.get("rel").toString());
                        for(int i = 0 ; i < arr.length(); i++ ){
                            wm.addWord(key, arr.get(i).toString(), Constants.SIMILAR);
                            notSelectedWords.add(arr.get(i).toString());
                        }
                    }
                }
                if(obj.has("noun")){
                    JSONObject json_o = (JSONObject) obj.get("noun");

                    if(json_o.has("syn")){
                        JSONArray arr = new JSONArray(json_o.get("syn").toString());
                        for(int i = 0 ; i < arr.length(); i++ ){
                            wm.addWord(key, arr.get(i).toString(), Constants.SYNONYMS);
                            notSelectedWords.add(arr.get(i).toString());
                        }
                    }
                    if(json_o.has("ant")){
                        JSONArray arr = new JSONArray(json_o.get("ant").toString());
                        for(int i = 0 ; i < arr.length(); i++ ){
                            wm.addWord(key, arr.get(i).toString(), Constants.ANTONYMS);
                            notSelectedWords.add(arr.get(i).toString());
                        }
                    }
                    if(json_o.has("sim")){
                        JSONArray arr = new JSONArray(json_o.get("sim").toString());
                        for(int i = 0 ; i < arr.length(); i++ ){
                            wm.addWord(key, arr.get(i).toString(), Constants.SIMILAR);
                            notSelectedWords.add(arr.get(i).toString());
                        }
                    }

                    if(json_o.has("rel")){
                        JSONArray arr = new JSONArray(json_o.get("rel").toString());
                        for(int i = 0 ; i < arr.length(); i++ ){
                            wm.addWord(key, arr.get(i).toString(), Constants.SIMILAR);
                            notSelectedWords.add(arr.get(i).toString());
                        }
                    }
                }

                //print the entire hashmap of the word
                Log.d(TAG, "Word:" + curr_word);
                Log.d(TAG, "HM:AN" + wm.getWordList(1, curr_word) +" size" + wm.getWordList(1, curr_word).length);
                Log.d(TAG, "HM:SY" + wm.getWordList(2, curr_word)+" size" + wm.getWordList(2, curr_word).length);
                Log.d(TAG, "HM:SI" + wm.getWordList(3, curr_word) + " size" + wm.getWordList(3, curr_word).length);


            } catch (JSONException e) {
                Log.d(TAG, "Word:" + curr_word);
                Log.d(TAG, "HM:AN" + wm.getWordList(1, curr_word) +" size" + wm.getWordList(1, curr_word).length);
                Log.d(TAG, "HM:SY" + wm.getWordList(2, curr_word)+" size" + wm.getWordList(2, curr_word).length);
                Log.d(TAG, "HM:SI" + wm.getWordList(3, curr_word) + " size" + wm.getWordList(3, curr_word).length);
                e.printStackTrace();
            }
        }

        protected void onPostExecute(Boolean result) {
            String[] wordarr = wm.getWordList(type, curr_word);
            for(int i = 0; i< wordarr.length; i++){
                words.add(wordarr[i]);
            }

            adapter = new ArrayAdapter<String>(mContext,
                    android.R.layout.simple_list_item_multiple_choice, words);

            listView.setAdapter(adapter);
            //TODO by default set the word itself as selected
//            listView.setItemChecked(1,true);
            word_hm.put(curr_word, wm);
            nextButton.setEnabled(true);
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

//            if(urls.contains("kill"))
//                result = "{\"noun\":{\"syn\":[\"killing\",\"putting to death\",\"conclusion\",\"destruction\",\"devastation\",\"ending\",\"termination\"]},\"verb\":{\"syn\":[\"shoot down\",\"defeat\",\"vote down\",\"vote out\",\"stamp out\",\"toss off\",\"pop\",\"bolt down\",\"belt down\",\"pour down\",\"down\",\"drink down\",\"obliterate\",\"wipe out\",\"ache\",\"be\",\"beat\",\"blackball\",\"cut\",\"destroy\",\"destruct\",\"drink\",\"end\",\"exhaust\",\"hit\",\"hurt\",\"imbibe\",\"negative\",\"overcome\",\"overpower\",\"overtake\",\"overwhelm\",\"suffer\",\"sweep over\",\"switch off\",\"take away\",\"take out\",\"terminate\",\"tucker\",\"tucker out\",\"turn off\",\"turn out\",\"veto\",\"wash up\",\"whelm\"],\"rel\":[\"kill off\"]}}\n";
//            else if(urls.contains("dry"))
//                result = "{\"adjective\":{\"syn\":[\"ironic\",\"ironical\",\"wry\",\"juiceless\",\"teetotal\"],\"ant\":[\"phlegmy\",\"sweet\",\"wet\"],\"rel\":[\"nonsweet\",\"sour\",\"sugarless\"],\"sim\":[\"adust\",\"air-dried\",\"air-dry\",\"alcoholic\",\"arid\",\"baked\",\"bone dry\",\"bone-dry\",\"brut\",\"desiccated\",\"dried\",\"dried-out\",\"dried-up\",\"dry-eyed\",\"dry-shod\",\"humorous\",\"humourous\",\"kiln-dried\",\"medium-dry\",\"milkless\",\"parched\",\"plain\",\"rainless\",\"scorched\",\"sear\",\"sec\",\"semi-dry\",\"semiarid\",\"sere\",\"shriveled\",\"shrivelled\",\"sober\",\"solid\",\"sunbaked\",\"tearless\",\"thirsty\",\"unemotional\",\"unexciting\",\"unproductive\",\"unstimulating\",\"unsweet\",\"waterless\",\"withered\"]},\"noun\":{\"syn\":[\"prohibitionist\",\"crusader\",\"meliorist\",\"reformer\",\"reformist\",\"social reformer\"]},\"verb\":{\"syn\":[\"dry out\",\"alter\",\"change\",\"modify\"],\"ant\":[\"wet\"]}}";
//            else if(urls.contains("heaven"))
//                result = "{\"noun\":{\"syn\":[\"Eden\",\"paradise\",\"Nirvana\",\"promised land\",\"Shangri-la\",\"Heaven\",\"fictitious place\",\"imaginary place\",\"mythical place\",\"part\",\"region\"],\"ant\":[\"Hell\"]}}\n";

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

    int selected = 0;
    public void nextAction(View view){

        // get the current word
        int len = words.size();

        SparseBooleanArray checked = listView.getCheckedItemPositions();

//        for (int i = 0; i < len; i++)
//            if (checked.get(i)) {
//                String item = words[i];
//                /* do whatever you want with the checked item */
//                Log.d(TAG, "selected" + item);
//                selectedWords.add(item);
//                notSelectedWords.remove(item);
//            }

        // pick next word from word list
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {

            if(listView.getCheckedItemCount() == selected){
                Toast.makeText(this, "Select atleast one word !!!", Toast.LENGTH_LONG).show();
            }
            else if(counter < wordList.size()) {
                String word = wordList.get(counter);
                words = new ArrayList<>(); // clean previous word list for adapter
                new WordAPITask().execute(word);
                wm = new WordModel(word);
                titleView.setText(titlePrefix + " - " + word.toUpperCase());
                counter++;
                selected = listView.getCheckedItemCount();
                nextButton.setEnabled(false);

            }
            else{
                Log.d(TAG,"take me to next activity");
                Log.d(TAG,"selected words-" + selectedWords.size());
                Log.d(TAG,"Not selected words-" + notSelectedWords.size());

                Bundle extra = new Bundle();
                extra.putSerializable("selectedWordArrayList", selectedWords);
                extra.putSerializable("notSelectedWordArrayList", notSelectedWords);
                Intent intent = new Intent(getBaseContext(), SampleTagCloud.class);
                intent.putExtra("SIGN_UP",true);
                intent.putExtra("extra", extra);
                intent.putExtra("usern",str_usern);
                intent.putExtra("checkuser", checkuser);

                long timeSpent = Calendar.getInstance().getTimeInMillis() - startTime;
                CSVeditor.shared().recordTimeStamp(timeSpent, 8);

                startActivity(intent);

            }
        }
        // call the async task with next word if word list empty then send to next activity
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getBaseContext(), UsernameActivity.class);
        startActivity(intent);
    }

    long startTime;
    @Override
    protected void onResume() {
        super.onResume();
        startTime = Calendar.getInstance().getTimeInMillis();
    }
}
