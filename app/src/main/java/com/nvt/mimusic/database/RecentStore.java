package com.nvt.mimusic.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by Admin on 11/2/17.
 */

/**
 * Recent Store Table
 * */

public class RecentStore {

    private static final int MAX_ITEMS_IN_DB = 100;
    private static RecentStore recentStoreInstance ;
    private MusicDatabase mMusicDatabase = null;

    public RecentStore(Context context) {
         mMusicDatabase =MusicDatabase.getMusicDatabaseInstance(context);
    }
    public static final synchronized RecentStore getRecentStoreInstance(final Context context)
    {
        if (recentStoreInstance == null)
        {
            recentStoreInstance = new RecentStore(context.getApplicationContext());
        }
        return recentStoreInstance;
    }

    /**
     * On create SQL Querry : CREATE TABLE IF NOT EXISTS recent_store (id LONG NOT NULL,time_played LONG NOT NULL);
     *
     * */
    public  void onCreate(final SQLiteDatabase sqLiteDatabase)
    {
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + RecentStoreColumn.NAME + " (" + RecentStoreColumn.ID +
                " LONG NOT NULL, "+RecentStoreColumn.TIME_PLAYED + "LONG NOT NULL);");

    }
    public  void onUpgrade(final SQLiteDatabase sqLiteDatabase ,int oldversion,int newversion)
    {


    }
    public  void onDownGrade(final SQLiteDatabase sqLiteDatabase,int oldversion,int newversion)
    {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+RecentStoreColumn.NAME);
        onCreate(sqLiteDatabase);
    }
    /**
     * Remove when history over max history storage
     * */
    public void removeItem(final long songId){
        final SQLiteDatabase sqLiteDatabase = mMusicDatabase.getWritableDatabase();
        sqLiteDatabase.delete(RecentStoreColumn.NAME,RecentStoreColumn.ID+ "= ?",new String[]{String.valueOf(songId)});

    }
    /**
     * Delete when user clear hostory
     * */
    public void deleteAll(){
        final SQLiteDatabase sqLiteDatabase = mMusicDatabase.getWritableDatabase();
        sqLiteDatabase.delete(RecentStoreColumn.NAME,null,null);
    }
    /**
     * Query to get id with selection argument = id
     * */
    private Cursor queryRecentId(final String limit){
        final SQLiteDatabase sqLiteDatabase = mMusicDatabase.getReadableDatabase();
        return sqLiteDatabase.query(RecentStoreColumn.NAME,new String[]{RecentStoreColumn.ID}
        ,null ,null,null,null,null,limit);

    }
    /**
     * Query to get last id with selection argument = time search and sorted
     * */


    private Cursor queryLastId()
    {
        final SQLiteDatabase sqLiteDatabase = mMusicDatabase.getReadableDatabase();
        return sqLiteDatabase.query(RecentStoreColumn.NAME,new String[]{RecentStoreColumn.TIME_PLAYED},
                null,null,RecentStoreColumn.TIME_PLAYED,null,"ACS");
    }


    //Add Remove new Recent Items
    public void addSongIdToRecentList(long songId){
        final SQLiteDatabase sqLiteDatabase = mMusicDatabase.getWritableDatabase();
        sqLiteDatabase.beginTransaction();
        try{
            Cursor recentCursor = null;
            try
            {
                recentCursor = queryRecentId("1");
                if (recentCursor != null && recentCursor.moveToFirst())
                {
                    if (songId == recentCursor.getLong(0));
                    return;
                }

            }finally
            {
                if (recentCursor != null)
                {
                    recentCursor.close();
                    recentCursor = null;
                }

            }
            final ContentValues contentValues = new ContentValues(2);
            contentValues.put(RecentStoreColumn.ID , songId);
            contentValues.put(RecentStoreColumn.TIME_PLAYED,System.currentTimeMillis());
            sqLiteDatabase.insert(RecentStoreColumn.NAME,null ,contentValues);

            Cursor lastCursor = null;
            try {
                lastCursor = queryLastId();
                if (lastCursor != null && lastCursor.getCount() > MAX_ITEMS_IN_DB)
                {
                    lastCursor.moveToPosition(lastCursor.getCount() - MAX_ITEMS_IN_DB);
                    Long timeToSkeep = lastCursor.getLong(0);
                    sqLiteDatabase.delete(RecentStoreColumn.NAME,RecentStoreColumn.TIME_PLAYED + "< ?",
                            new String[]{String.valueOf(timeToSkeep)});

                }

            }finally {
                if (lastCursor !=null)
                {
                    lastCursor.close();
                    lastCursor = null;
                }

            }

        }
       finally {
            sqLiteDatabase.setTransactionSuccessful();
            sqLiteDatabase.endTransaction();

        }
    }
    public interface RecentStoreColumn{
        String NAME = "recent_store";
        String ID = "id";
        String TIME_PLAYED = "time_played" ;
    }


}
