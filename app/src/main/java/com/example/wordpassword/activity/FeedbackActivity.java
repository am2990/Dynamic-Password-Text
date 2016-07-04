package com.example.wordpassword.activity;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.wordpassword.helper.CSVeditor;
import com.example.wordpassword.helper.NotificationPublisher;
import com.example.wordpassword.R;

public class FeedbackActivity extends AppCompatActivity {

    private static final String TAG = FeedbackActivity.class.getSimpleName();

    private Button btnSubmit;
    private RatingBar ratingBar;
    private Spinner spnMemoryBurden;
    private Spinner spnUnderstand;
    private Spinner spnRemember;

    boolean submitPressed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        btnSubmit = (Button) findViewById(R.id.btn_submit_feedback);
        ratingBar = (RatingBar) findViewById(R.id.rating_bar);
        spnMemoryBurden = (Spinner) findViewById(R.id.spn_memory_burden);
        spnUnderstand = (Spinner) findViewById(R.id.spn_understand);
        spnRemember = (Spinner) findViewById(R.id.spn_remember);

        final String userName = getIntent().getStringExtra("USERNAME");

        UsernameActivity.stopScreenSharing();

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int rating = (int) ratingBar.getRating();
                String memoryBurden = spnMemoryBurden.getSelectedItem().toString();
                String understand = spnUnderstand.getSelectedItem().toString();
                String remember = spnRemember.getSelectedItem().toString();

                Log.v(TAG,"rating: "+rating+" memoryBurden: "+memoryBurden+
                        " understand: "+understand+" remember: "+remember);

                CSVeditor.shared().insertFeedback(rating, memoryBurden, understand, remember);
                CSVeditor.shared().recordTimeStamp(InstructionsActivity.endTime, 15);

                scheduleNotification(getNotification("Its time to login using "+userName), AlarmManager.INTERVAL_DAY);

                submitPressed = true;

                onBackPressed();
            }
        });

    }
    @Override
    public void onBackPressed() {

        if(submitPressed) {
            Intent intent = new Intent(FeedbackActivity.this, UsernameActivity.class);
            startActivity(intent);
            finish();
        }
        else {
            Toast.makeText(FeedbackActivity.this, "Please press submit", Toast.LENGTH_SHORT).show();
        }
    }

    private void scheduleNotification(Notification notification, long delay) {

        Intent notificationIntent = new Intent(this, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, 1);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        long futureInMillis = SystemClock.elapsedRealtime() + delay;
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private Notification getNotification(String content) {

        Intent myIntent = new Intent(getApplicationContext(), UsernameActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                getApplicationContext(),
                0,
                myIntent,
                PendingIntent.FLAG_ONE_SHOT);

        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentTitle("Word password login");
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);
        builder.setDefaults(Notification.DEFAULT_ALL);
        builder.setContentText(content);
        builder.setSmallIcon(R.mipmap.ic_launcher);

        return builder.build();
    }
}
