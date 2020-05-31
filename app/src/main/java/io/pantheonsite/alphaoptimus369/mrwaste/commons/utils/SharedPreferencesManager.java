package io.pantheonsite.alphaoptimus369.mrwaste.commons.utils;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.Nullable;

import io.pantheonsite.alphaoptimus369.mrwaste.app.App;
import io.pantheonsite.alphaoptimus369.mrwaste.commons.models.UserItem;


public class SharedPreferencesManager
{

    // Field
    private SharedPreferences preferences;


    // Constructor
    public SharedPreferencesManager()
    {
        String PREF_NAME = "waste_sharedpref";
        preferences = App.getInstance().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }


    // Methods to save and get values
    private void saveString(String key, String value) {
        preferences.edit().putString(key, value).apply();
    }

    /*public String getString(String key) {
        return preferences.getString(key, "");
    }*/

    public String getString(String key, String defaultValue) {
        return preferences.getString(key, defaultValue);
    }

    /*public void saveInt(String key, int value) {
        preferences.edit().putInt(key, value).apply();
    }

    public int getInt(String key) {
        return preferences.getInt(key, -1);
    }*/

    public void saveFloat(String key, float value) {
        preferences.edit().putFloat(key, value).apply();
    }

    public float getFloat(String key) {
        return preferences.getFloat(key, 0);
    }

    public void saveBoolean(String key, boolean value) {
        preferences.edit().putBoolean(key, value).apply();
    }

    public boolean getBoolean(String key) {
        return preferences.getBoolean(key, false);
    }


    // Util method(s)
    /*public boolean isPresent(String key) {
        return preferences.contains(key);
    }*/


    // Helper methods
    public void saveCurrentUser(@Nullable UserItem userItem)
    {
        if (userItem == null) {
            saveBoolean("user_logged_in", false);
            return;
        }

        saveBoolean("user_logged_in", true);
        saveString("email", userItem.email);
        saveString("contact", userItem.contactNo);
        saveString("type", userItem.userType);
        saveString("address", userItem.addressLine);
        saveFloat("latitude", (float) userItem.latitude);
        saveFloat("longitude", (float) userItem.longitude);
    }

    @Nullable
    public UserItem getCurrentUser()
    {
        if (!getBoolean("user_logged_in"))
            return null;

        return new UserItem(
                getString("email", ""),
                getString("contact", ""),
                getString("type", ""),
                getString("address", ""),
                getFloat("latitude"),
                getFloat("longitude")
        );
    }

}
