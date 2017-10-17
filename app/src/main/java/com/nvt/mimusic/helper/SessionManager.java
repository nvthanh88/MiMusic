package com.nvt.mimusic.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by Admin on 9/11/17.
 */

public class SessionManager {
    //LogCat
    private static  final  String TAG = SessionManager.class.getSimpleName();

    SharedPreferences mSharedPrefer;
    Context mAppContext;
    SharedPreferences.Editor mEditor;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "GoMix";

    private static final String KEY_IS_LOGGEDIN = "isLoggedIn";
    private static final String ACCOUNT_NAME = "accountName";
    private static final String EMAIL_ADDRESS = "emailAddress";



    public SessionManager(Context context)
    {
        this.mAppContext = context;
        mSharedPrefer = mAppContext.getSharedPreferences(PREF_NAME,PRIVATE_MODE);
        mEditor = mSharedPrefer.edit();
    }

    public void setLogin(Boolean isLoggedin)
    {
        mEditor.putBoolean(KEY_IS_LOGGEDIN,isLoggedin);
        mEditor.commit();
        Log.d(TAG, "User login session modified!");
    }
    public void setAccount(String accountName)
    {
        mEditor.putString(ACCOUNT_NAME,accountName);
        mEditor.commit();
    }
    public void setEmail(String emailAddress)
    {
        mEditor.putString(EMAIL_ADDRESS,emailAddress);
        mEditor.commit();
    }

    public Boolean isLoggedin()
    {
        return mSharedPrefer.getBoolean(KEY_IS_LOGGEDIN,false);
    }
    public String getAccountName(){
        return  mSharedPrefer.getString(ACCOUNT_NAME,"");
    }
    public String getEmailAdress(){
        return  mSharedPrefer.getString(EMAIL_ADDRESS,"");
    }
}
