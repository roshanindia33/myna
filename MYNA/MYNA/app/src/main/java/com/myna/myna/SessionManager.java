package com.myna.myna;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
public class SessionManager {
    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        prefs = context.getSharedPreferences("userSession", Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public void saveSession(String uid, String email) {
        editor.putBoolean("isLoggedIn", true);
        editor.putString("uid", uid);
        editor.putString("email", email);
        editor.apply();
    }

    public boolean isLoggedIn() {
        return prefs.getBoolean("isLoggedIn", false);
    }

    public String getUid() {
        return prefs.getString("uid", null);
    }

    public String getEmail() {
        return prefs.getString("email", null);
    }

    public void clearSession() {
        editor.clear().apply();
    }
}
