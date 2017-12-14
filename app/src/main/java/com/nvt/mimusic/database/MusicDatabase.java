package com.nvt.mimusic.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by THANH.NV on 10/22/2017.
 */

public class MusicDatabase extends SQLiteOpenHelper {

    private static  MusicDatabase musicDatabaseInstance;
    private static final String DATABASE_NAME = "mimusic.db";
    private static final int DATABASE_VERSION = 4;
    private Context mContext;
    public static synchronized MusicDatabase getMusicDatabaseInstance(final Context mContext){
        if (musicDatabaseInstance == null)
        {
            musicDatabaseInstance = new MusicDatabase(mContext.getApplicationContext());
        }
        return musicDatabaseInstance;
    }

    /**
     *
     * */
    public MusicDatabase(final Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
        mContext = context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        MusicPlaybackState.getMusicPlaybackStateInstance(mContext).onCreate(db);
        RecentStore.getRecentStoreInstance(mContext).onCreate(db);
        SongPlayedCounter.getInstance(mContext).onCreate(db);



    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        MusicPlaybackState.getMusicPlaybackStateInstance(mContext).onUpgrade(db, oldVersion, newVersion);
        RecentStore.getRecentStoreInstance(mContext).onUpgrade(db, oldVersion, newVersion);}

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        MusicPlaybackState.getMusicPlaybackStateInstance(mContext).onDowngrade(db,oldVersion,newVersion);
        RecentStore.getRecentStoreInstance(mContext).onDownGrade(db, oldVersion, newVersion);
    }
}
