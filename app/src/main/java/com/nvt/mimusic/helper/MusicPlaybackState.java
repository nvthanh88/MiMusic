package com.nvt.mimusic.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.nvt.mimusic.core.MiCoreApplication;
import com.nvt.mimusic.database.MusicDatabase;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;


/**
 * Created by THANH.NV on 10/22/2017.
 */

public class MusicPlaybackState {
    private static MusicPlaybackState musicPlaybackStateInstance = null;
    private MusicDatabase mMusicDatabase;

    public MusicPlaybackState(final Context context) {
        mMusicDatabase = MusicDatabase.getMusicDatabaseInstance(context);
    }

    public static final synchronized MusicPlaybackState getMusicPlaybackStateInstance(final Context context){
        if (musicPlaybackStateInstance == null)
            musicPlaybackStateInstance = new MusicPlaybackState(context);
        return musicPlaybackStateInstance;
    }
    public void onCreate(final SQLiteDatabase db) {
        StringBuilder builder = new StringBuilder();
        builder.append("CREATE TABLE IF NOT EXISTS ");
        builder.append(PlaybackQueueColumns.NAME);
        builder.append("(");

        builder.append(PlaybackQueueColumns.TRACK_ID);
        builder.append(" LONG NOT NULL,");

        builder.append(PlaybackQueueColumns.SOURCE_ID);
        builder.append(" LONG NOT NULL,");

        builder.append(PlaybackQueueColumns.SOURCE_TYPE);
        builder.append(" INT NOT NULL,");

        builder.append(PlaybackQueueColumns.SOURCE_POSITION);
        builder.append(" INT NOT NULL);");

        db.execSQL(builder.toString());

        builder = new StringBuilder();
        builder.append("CREATE TABLE IF NOT EXISTS ");
        builder.append(PlaybackHistoryColumns.NAME);
        builder.append("(");

        builder.append(PlaybackHistoryColumns.POSITION);
        builder.append(" INT NOT NULL);");

        db.execSQL(builder.toString());
    }

    public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
        // this table was created in version 2 so call the onCreate method if we hit that scenario
        if (oldVersion < 2 && newVersion >= 2) {
            onCreate(db);
        }
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + PlaybackQueueColumns.NAME);
        db.execSQL("DROP TABLE IF EXISTS " + PlaybackHistoryColumns.NAME);
        onCreate(db);
    }
    public ArrayList<MusicPlaybackTrack> getQueue() {
        ArrayList<MusicPlaybackTrack> results = new ArrayList<>();

        Cursor cursor = null;
        try {
            cursor = mMusicDatabase.getReadableDatabase().query(PlaybackQueueColumns.NAME, null,
                    null, null, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                results.ensureCapacity(cursor.getCount());

                do {
                    results.add(new MusicPlaybackTrack(cursor.getLong(0), cursor.getLong(1),
                            MiCoreApplication.IdType.getTypeById(cursor.getInt(2)), cursor.getInt(3)));
                } while (cursor.moveToNext());
            }

            return results;
        } finally {
            if (cursor != null) {
                cursor.close();
                cursor = null;
            }
        }
    }

    public LinkedList<Integer> getHistory(final int playlistSize) {
        LinkedList<Integer> results = new LinkedList<>();

        Cursor cursor = null;
        try {
            cursor = mMusicDatabase.getReadableDatabase().query(PlaybackHistoryColumns.NAME, null,
                    null, null, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int pos = cursor.getInt(0);
                    if (pos >= 0 && pos < playlistSize) {
                        results.add(pos);
                    }
                } while (cursor.moveToNext());
            }

            return results;
        } finally {
            if (cursor != null) {
                cursor.close();
                cursor = null;
            }
        }
    }


    public class PlaybackQueueColumns {

        public static final String NAME = "playbackqueue";
        public static final String TRACK_ID = "trackid";
        public static final String SOURCE_ID = "sourceid";
        public static final String SOURCE_TYPE = "sourcetype";
        public static final String SOURCE_POSITION = "sourceposition";
    }

    public class PlaybackHistoryColumns {

        public static final String NAME = "playbackhistory";

        public static final String POSITION = "position";
    }

}
