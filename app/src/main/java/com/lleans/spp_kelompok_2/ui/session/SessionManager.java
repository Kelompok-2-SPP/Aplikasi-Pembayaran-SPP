package com.lleans.spp_kelompok_2.ui.session;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.HashMap;

public class SessionManager {

    private Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public static final String IS_LOGGED_IN = "isloggedIn";
    public static final String ID = "id";
    public static final String USERNAME = "username";
    public static final String TYPE = "type";
    public static final String TOKEN = "token";

    // Public constructor
    public SessionManager(Context context) {
        this.context = context;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = sharedPreferences.edit();
    }

    // Public function to create session
    public void createLogininSessFor(String username, String id, String type, String token) {
        editor.putBoolean(IS_LOGGED_IN, true);
        editor.putString(ID, id);
        editor.putString(USERNAME, username);
        editor.putString(TYPE, type);
        editor.putString(TOKEN, token);
        editor.commit();
    }

    // Function to create HashMap(Dict) for session
    public HashMap<String, String> getUserDetail() {
        HashMap<String, String> user = new HashMap<>();
        user.put(ID, sharedPreferences.getString(ID, null));
        user.put(USERNAME, sharedPreferences.getString(USERNAME, null));
        user.put(TYPE, sharedPreferences.getString(TYPE, null));
        user.put(TOKEN, sharedPreferences.getString(TOKEN, null));
        return user;
    }

    // Public function to clear session
    public void logOutSession() {
        editor.clear();
        editor.commit();
    }

    // Public to check if there is a session
    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(IS_LOGGED_IN, false);
    }
}
