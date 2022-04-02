package com.lleans.spp_kelompok_2.ui.auth;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.lleans.spp_kelompok_2.ui.MainActivity;
import com.lleans.spp_kelompok_2.ui.session.SessionManager;

public class Logout {

    public Logout(Context context, Activity activity){
        // Clear session
        SessionManager sessionManager = new SessionManager(context);
        sessionManager.logOutSession();

        context.startActivity(new Intent(activity, MainActivity.class));

        // Kill current activity
        activity.finish();
    }
}
