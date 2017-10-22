package com.nvt.mimusic.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



/**
 * Created by THANH.NV on 10/22/2017.
 */

public class MusicDatabase extends SQLiteOpenHelper {

    private static  MusicDatabase musicDatabaseInstance;
    private static final String DATABASE_NAME = "music.db";
    private static final int DATABASE_VERSION = 4;
    private Context mContext;
    public static MusicDatabase getMusicDatabaseInstance(final Context mContext){
        if (musicDatabaseInstance == null)
        {
            musicDatabaseInstance = new MusicDatabase(mContext.getApplicationContext());
        }
        return musicDatabaseInstance;
    }
    public MusicDatabase(final Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
        mContext = context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}