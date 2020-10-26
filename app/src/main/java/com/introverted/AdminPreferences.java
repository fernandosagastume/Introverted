package com.introverted;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import java.util.HashMap;

public class AdminPreferences {
    // Shared Preferences reference
    SharedPreferences sharedPreferences;
    // Editor reference for Shared preferences
    Editor editor;
    private Context context;
    private static final String PREF_NAME = "USER_INFO";
    private static final String USER_ID_KEY = "USER_ID";


    // Constructor
    public AdminPreferences(Context context){
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    //Create login session
    public void createUserLoginSession(int id, String emailuser){
        // Storing login value as TRUE
        editor.putBoolean("USER_LOGIN", true);
        // Storing email in pref
        editor.putInt(USER_ID_KEY, id);
        // commit changes
        editor.commit();
    }

    /**
     * Check login method will check user login status
     * If false it will redirect user to login page
     * Else do anything
     * */
    public boolean checkLogin(){
        // Check login status
        if(!this.isUserLoggedIn()){

            // user is not logged in redirect him to Login Activity
            Intent in = new Intent(context, Login.class);
            // Closing all the Activities from stack
            in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            // Add new Flag to start new Activity
            in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            // Staring Login Activity
            context.startActivity(in);

            return true;
        }
        return false;
    }

    /**
     * Get stored session data
     * */
    public HashMap<String, String> getUserDetails(){
        //Use hashmap to store user credentials
        HashMap<String, String> user = new HashMap<String, String>();
        // user email id
        user.put(USER_ID_KEY, sharedPreferences.getString(USER_ID_KEY, null));
        // return user
        return user;
    }

    /**
     * Clear session details
     * */
    public void logoutUser(){

        // Clearing all user data from Shared Preferences
        editor.clear();
        editor.commit();
        // After logout redirect user to Login Activity
        Intent i = new Intent(context, Login.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // Staring Login Activity
        context.startActivity(i);
    }


    // Check for login
    public boolean isUserLoggedIn(){
        return sharedPreferences.getBoolean("USER_LOGIN", false);
    }

}
