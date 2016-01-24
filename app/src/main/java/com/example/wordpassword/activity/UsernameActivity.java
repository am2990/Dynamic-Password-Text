package com.example.wordpassword.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.wordpassword.MainActivity;
import com.example.wordpassword.R;
import com.example.wordpassword.SampleTagCloud;
import com.example.wordpassword.db.DatabaseHelper;
import com.example.wordpassword.util.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UsernameActivity extends ActionBarActivity {

    EditText username;
    Button bcontinue;
    User user = new User();
    private DatabaseHelper db;
   // boolean checkuser;
    String checkuser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.username_layout);


        username = (EditText) findViewById(R.id.username);
        bcontinue = (Button) findViewById(R.id.bcontinue);


        db = new DatabaseHelper(this);

        bcontinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String u = username.getText().toString();
                System.out.println("u: " + u);
                if (!(u.equals(""))){
                    user.setUsername(u);
                //TODO the function name should be addUser
                // db.addUsername(user);

                // DatabaseHelper db = new DatabaseHelper(Context);
                if (!(db.getUserByName(u))) {
                    checkuser = "false";
                    Intent intent = new Intent();
                    intent.setClass(UsernameActivity.this, MainActivity.class);
                    intent.putExtra("usern", u);
                    intent.putExtra("checkuser", checkuser);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Login!", Toast.LENGTH_SHORT).show();
                    checkuser = "true";
                    String str_selected = db.getSelectedByName(u);
                    String str_notSelected = db.getNselectedByName(u);

                    int indexOfOpenBracket1 = str_selected.indexOf("[");
                    int indexOfLastBracket1 = str_selected.lastIndexOf("]");
                    int indexOfOpenBracket2 = str_notSelected.indexOf("[");
                    int indexOfLastBracket2 = str_notSelected.lastIndexOf("]");

                    System.out.println("hhh: " + str_selected.substring(indexOfOpenBracket1 + 1, indexOfLastBracket1));

                    ArrayList<String> selected = new ArrayList<String>(Arrays.asList(str_selected.substring(indexOfOpenBracket1 + 1, indexOfLastBracket1).split(",")));
                    System.out.println("selected al:" + selected);

                    ArrayList<String> notSelected = new ArrayList<String>(Arrays.asList(str_notSelected.substring(indexOfOpenBracket2 + 1, indexOfLastBracket2).split(",")));
                    System.out.println("selected username: " + selected);
                    Intent intent = new Intent();
                    intent.setClass(UsernameActivity.this, SampleTagCloud.class);
                    intent.putExtra("usern", u);
                    intent.putExtra("checkuser", checkuser);
                    intent.putStringArrayListExtra("selected", selected);
                    intent.putStringArrayListExtra("notSelected", notSelected);
                    startActivity(intent);
                }
            }
                else{
                    Toast.makeText(getApplicationContext(), "Enter a username", Toast.LENGTH_SHORT).show();
                }
        }
        });
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
