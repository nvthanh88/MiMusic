package com.nvt.mimusic.core;

import android.app.Application;
import android.content.ContentUris;
import android.net.Uri;

/**
 * Created by Admin on 10/18/17.
 */

public class MiCoreApplication extends Application {
    public static MiCoreApplication mInstance;
    public static final MiCoreApplication getInstance(){
        if (mInstance == null)
        {
            mInstance = new MiCoreApplication();
        }
        return mInstance;
    }
    public static Uri getAlbumUri(long albumId)
    {
        return ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), albumId);
    }

}
