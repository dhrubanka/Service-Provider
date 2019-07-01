package com.bankaspace.serviceproviderbeta;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

/**
 * Created by DC on 27-02-2018.
 */

public class SharedPrefManager {
    //the constants
    private static final String SHARED_PREF_NAME = "Profilesharedpref";
    private static final String KEY_USERNAME = "keyusername";
    private static final String KEY_EMAIL = "keyemail";
    private static final String KEY_GENDER = "keygender";
    private static final String KEY_ID = "keyid";
    private static final String KEY_CATEGORY="keycategory";
    private static final String KEY_FULLNAME="keyfullname";
    private static final String KEY_PERSONALNO="keypersonalno";
    private static final String KEY_PRICERANGE="keypricerange";
    private static final String KEY_BUSINESSDES="keybusinessdes";
    private static final String KEY_WHATSAPP="keywhatsapp";
    private static final String KEY_PICPATH="keypicpath";
    private static final String KEY_LOCALITY="keylocality";
    private static final String KEY_ADDRESS="keyaddress";


    private static SharedPrefManager mInstance;
    private static Context mCtx;

    private SharedPrefManager(Context context) {
        mCtx = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    //method to let the user login
    //this method will store the user data in shared preferences
    public void userLogin(User user) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_ID, user.getId());
        editor.putString(KEY_USERNAME, user.getUsername());
        editor.putString(KEY_EMAIL, user.getEmail());
        editor.putString(KEY_GENDER, user.getGender());
        editor.putString(KEY_CATEGORY, user.getCategory());
        editor.putString(KEY_FULLNAME, user.getName());
        editor.putString(KEY_PERSONALNO,user.getPersonalno());
        editor.putString(KEY_BUSINESSDES,user.getBusinessdes());
        editor.putString(KEY_PRICERANGE,user.getPricerange());
        editor.putString(KEY_WHATSAPP,user.getWhatsapp());
        editor.putString(KEY_PICPATH,user.getPicpath());
        editor.putString(KEY_LOCALITY,user.getLocality());
        editor.putString(KEY_ADDRESS,user.getAddress());

        editor.apply();
    }

    //this method will checker whether user is already logged in or not
    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USERNAME, null) != null;
    }

    //this method will give the logged in user
    public User getUser() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new User(
                sharedPreferences.getString(KEY_ID, null),
                sharedPreferences.getString(KEY_USERNAME, null),
                sharedPreferences.getString(KEY_EMAIL, null),
                sharedPreferences.getString(KEY_GENDER, null),
                sharedPreferences.getString(KEY_CATEGORY,null),
                sharedPreferences.getString(KEY_FULLNAME,null),
                sharedPreferences.getString(KEY_PERSONALNO,null),
                sharedPreferences.getString(KEY_PRICERANGE,null),
                sharedPreferences.getString(KEY_BUSINESSDES,null),
                sharedPreferences.getString(KEY_WHATSAPP,null),
                sharedPreferences.getString(KEY_PICPATH,null),
                sharedPreferences.getString(KEY_LOCALITY,null),
                sharedPreferences.getString(KEY_ADDRESS,null)

        );
    }

    //this method will logout the user
    public void logout() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        mCtx.startActivity(new Intent(mCtx, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK)
);
    }
}
