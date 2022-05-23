package vianh.nva.moneymanager.data;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

public class SharePrefHelper {
    private SharedPreferences sharedPreferences;
    private static SharePrefHelper sInstance;
    private static final String FILE_NAME = "app_pref";
    private static final String PASSWORD_KEY = "user_pass";

    private SharePrefHelper (Application application) {
        this.sharedPreferences = application.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
    }

    public static SharePrefHelper getInstance(Application application) {
        if (sInstance == null) {
            synchronized (SharedPreferences.class) {
                if (sInstance == null) {
                    sInstance = new SharePrefHelper(application);
                }
            }
        }

        return sInstance;
    }

    public void savePassword(String password) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PASSWORD_KEY, password);
        editor.apply();
    }

    public String getPassword() {
        if (sharedPreferences != null) {
            return sharedPreferences.getString(PASSWORD_KEY, null);
        }

        return null;
    }

    public void clearPassword() {
        sharedPreferences.edit().clear().apply();
    }
}
