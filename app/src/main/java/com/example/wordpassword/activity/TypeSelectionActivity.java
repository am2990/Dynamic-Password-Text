package com.example.wordpassword.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.wordpassword.CSVeditor;
import com.example.wordpassword.R;
import com.example.wordpassword.util.Constants;

import java.util.ArrayList;
import java.util.Calendar;

public class TypeSelectionActivity extends AppCompatActivity {

    private int passwordType = 0;
    Bundle extra;
    Intent iuser,icheckuser;
    String str_usern,checkuser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_selection);
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
                Snackbar.make(view, "Select a type for words in your password phrase", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //by default set antonyms options checked
        RadioGroup r = (RadioGroup)findViewById(R.id.radio_select);
        r.check(R.id.radio_ant);
        passwordType = Constants.ANTONYMS;
        extra = getIntent().getBundleExtra("extra");
        try{
            ArrayList<String> objects = (ArrayList<String>) extra.getSerializable("wordArrayList");
            extra.putSerializable("wordArrayList", objects);

        }catch (Exception e){
            // means arraylist not sent with intent and probably user came back from Word Selection Activity
            Intent intent = new Intent(getBaseContext(), UsernameActivity.class);
            startActivity(intent);
            // send him to first screen again
        }
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_syn:
                if (checked) {
                    passwordType = Constants.SYNONYMS;
                    break;
                }
            case R.id.radio_ant:
                if (checked) {
                    passwordType = Constants.ANTONYMS;
                    break;
                }
            case R.id.radio_sim:
                if (checked) {
                    passwordType = Constants.SIMILAR;
                    break;
                }
        }
    }

    public void nextActivity(View view) {
        extra.putInt(Constants.PASSWORD_TYPE, passwordType);
        Intent intent = new Intent(getBaseContext(), WordSelection.class);
        intent.putExtra("extra", extra);
        intent.putExtra("usern",str_usern);
        intent.putExtra("checkuser", checkuser);

        long timeSpent = Calendar.getInstance().getTimeInMillis() - startTime;
        CSVeditor.shared().recordTimeStamp(timeSpent, 7);

        startActivity(intent);
    }

    long startTime;
    @Override
    protected void onResume() {
        super.onResume();
        startTime = Calendar.getInstance().getTimeInMillis();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
