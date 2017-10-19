package com.nvt.mimusic.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Admin on 10/19/17.
 */

public final class PreferencesUtility {
    public static final String ALBUM_SORT_ORDER = "album_sort_order";
    public static final String SONG_SORT_ORDER = "album_sort_order";

    private static PreferencesUtility sInstance;
    private Context mContext;
    private static SharedPreferences mPreferences;

    public  PreferencesUtility(Context mContext) {
        this.mContext = mContext;
        mPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
    }
    public static final PreferencesUtility getInstance(Context context){
        if (sInstance == null )
        {
            sInstance = new PreferencesUtility(context.getApplicationContext());
        }
        return sInstance;
    }


    public static String getAlbumSortOrder() {
        return mPreferences.getString(ALBUM_SORT_ORDER,DataSort.albumSortOrder.ALBUM_A_Z);
    }
    public static String getSongSortOrder() {
        return mPreferences.getString(SONG_SORT_ORDER,DataSort.albumSortOrder.SONG_A_Z);
    }
}
