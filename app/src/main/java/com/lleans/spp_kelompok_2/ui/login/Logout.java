package com.lleans.spp_kelompok_2.ui.login;

import android.app.Activity;
import android.content.Context;

import com.lleans.spp_kelompok_2.ui.session.SessionManager;

public class Logout {

    private SessionManager sessionManager;

    public Logout(Context context, Activity activity){
        sessionManager = new SessionManager(context);
        sessionManager.logOutSession();
        activity.finish();
    }
}
