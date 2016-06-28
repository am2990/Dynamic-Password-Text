package com.example.wordpassword.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.wordpassword.CSVeditor;
import com.example.wordpassword.R;

import java.util.Calendar;

public class InstructionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructions);
    }

    long startTime;
    @Override
    protected void onResume() {
        super.onResume();
        startTime = Calendar.getInstance().getTimeInMillis();
    }

    @Override
    protected void onPause() {
        super.onPause();
        long endTime = Calendar.getInstance().getTimeInMillis() - startTime;
        CSVeditor.shared().recordTimeStamp(endTime, 15);
    }
}
