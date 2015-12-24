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

import com.example.wordpassword.R;
import com.example.wordpassword.com.example.wordpassword.util.Constants;

public class TypeSelectionActivity extends AppCompatActivity {

    private int passwordType = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_selection);
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

        //by default set antonyms options checked
        RadioGroup r = (RadioGroup)findViewById(R.id.radio_select);
        r.check(R.id.radio_ant);

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

        Bundle extra = getIntent().getBundleExtra("extra");
        extra.putInt(Constants.PASSWORD_TYPE, passwordType);
        Intent intent = new Intent(getBaseContext(), WordSelection.class);
        intent.putExtra("extra", extra);
        startActivity(intent);
    }

}
