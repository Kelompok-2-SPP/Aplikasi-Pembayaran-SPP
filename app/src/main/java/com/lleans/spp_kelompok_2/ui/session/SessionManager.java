package com.lleans.spp_kelompok_2.ui.session;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.HashMap;

public class SessionManager {

    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;

    public static final String IS_LOGGED_IN = "isLoggedIn";
    public static final String ID = "id";
    public static final String USERNAME = "username";
    public static final String TYPE = "type";
    public static final String TOKEN = "token";

    public SessionManager(Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = sharedPreferences.edit();
    }

    public void createLogininSessFor(String username, String id, String type, String token) {
        editor.putBoolean(IS_LOGGED_IN, true)
                .putString(ID, id)
                .putString(USERNAME, username)
                .putString(TYPE, type)
                .putString(TOKEN, token)
                .commit();
    }

    public HashMap<String, String> getUserDetail() {
        HashMap<String, String> user = new HashMap<>();
        user.put(ID, sharedPreferences.getString(ID, null));
        user.put(USERNAME, sharedPreferences.getString(USERNAME, null));
        user.put(TYPE, sharedPreferences.getString(TYPE, null));
        user.put(TOKEN, sharedPreferences.getString(TOKEN, null));
        return user;
    }

    public void logOutSession() {
        editor.clear()
                .commit();
    }

    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(IS_LOGGED_IN, false);
    }
}
