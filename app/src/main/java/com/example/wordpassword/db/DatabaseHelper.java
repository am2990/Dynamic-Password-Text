package com.example.wordpassword.db;

/**
 * Created by apurv on 12/24/2015.
 */

import java.util.ArrayList;
import java.util.List;



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.wordpassword.util.User;


public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "wordpin";

    // Table Names
    private static final String TABLE_WORDPIN = "wordpin_table";

    private static final String KEY_ID = "_id"; //primary key
    private static final String KEY_CREATED_AT = "created_at";

    private static final String KEY_SENTENCE = "sentence";
    private static final String KEY_TRIMMED_SENTENCE = "trimmed_sentence";
    private static final String KEY_PASSWORD_TYPE = "password_type";
    private static final String KEY_SYNONYM_LIST = "syn_list";
    private static final String KEY_ANTONYM_LIST = "ant_list";
    private static final String KEY_SIM_LIST = "sim_list";

    private static final String KEY_USERNAME = "username";
    private static final String KEY_SELECTED = "selected";
    private static final String KEY_NSELECTED = "nselected";

    // Table Create Statements
    // t_login table create statement
    private static final String CREATE_TABLE_t_login = "CREATE TABLE "
            + TABLE_WORDPIN + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_USERNAME + " VARCHAR," + KEY_SELECTED + " VARCHAR," + KEY_NSELECTED + " VARCHAR " + " )";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // creating required tables
        db.execSQL(CREATE_TABLE_t_login);

        Log.d(TAG, "creating tables");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WORDPIN);
        // create new tables
        onCreate(db);
    }

    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        System.out.println("username1: " + user.getUsername());
        boolean exists = getUserByName(user.getUsername());
        if(exists == false) {
            values.put(KEY_USERNAME, user.getUsername());
            values.put(KEY_SELECTED, user.getSelected());
            values.put(KEY_NSELECTED, user.getNselected());
            // Inserting Row
            db.insert(TABLE_WORDPIN, null, values);
            System.out.println("inserted successfully");
        }
        db.close(); // Closing database connection
    }

    public boolean getUserByName(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        System.out.println("username: "+ username);
        String selectString = "SELECT * FROM " + TABLE_WORDPIN + " WHERE " + KEY_USERNAME + " =?";
       /* Cursor cursor = db.query(TABLE_WORDPIN,
                new String[]{KEY_USERNAME},
                KEY_USERNAME + " = ? ",
                new String[]{username},
                null, null, null, null);
*/
        Cursor cursor = db.rawQuery(selectString, new String[]{username});
        if(cursor.getCount()>0) {
            System.out.println("cursor: "+ cursor);
            return true; //row exists
        }
        else
            return false;
    }

    public String getSelectedByName(String username){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_WORDPIN, new String[] { KEY_ID,
                        KEY_USERNAME, KEY_SELECTED }, KEY_USERNAME + "=?",
                new String[] { username }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        String selected = new String((cursor.getString(2)));
        //System.out.println("usern db: "+ usern);
        // return contact
        return selected;
    }

    public String getNselectedByName(String username){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_WORDPIN, new String[] { KEY_ID,
                        KEY_USERNAME, KEY_NSELECTED }, KEY_USERNAME + "=?",
                new String[] { username }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        String notSelected = new String((cursor.getString(2)));
        //System.out.println("usern db: "+ usern);
        // return contact
        return notSelected;
    }
//
//    public String getPassByName(String username){
//        SQLiteDatabase db = this.getReadableDatabase();
//
//        Cursor cursor = db.query(TABLE_NUMPIN, new String[] { KEY_ID,
//                        KEY_USERNAME, KEY_PASSWORD }, KEY_USERNAME + "=?",
//                new String[] { username }, null, null, null, null);
//        if (cursor != null)
//            cursor.moveToFirst();
//
//        String pass = new String((cursor.getString(2)));
//        //System.out.println("usern db: "+ usern);
//        // return contact
//        return pass;
//
//    }
//    public int updatewrongtry(String username,int wrongtry) {
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        ContentValues values = new ContentValues();
//        values.put(KEY_WRONGTRY, wrongtry);
//        // values.put(KEY_STATUS,
//        System.out.println("wrontrydb: "+ wrongtry);
//        // updating row
//        return db.update(TABLE_NUMPIN, values, KEY_USERNAME + " = ?",
//                new String[] { username });
//    }
}
