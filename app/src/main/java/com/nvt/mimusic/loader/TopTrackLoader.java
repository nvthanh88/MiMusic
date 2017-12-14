package com.nvt.mimusic.loader;

import android.content.Context;
import android.database.Cursor;
import android.provider.BaseColumns;

import com.nvt.mimusic.constant.Constant;
import com.nvt.mimusic.database.RecentStore;
import com.nvt.mimusic.database.SongPlayedCounter;
import com.nvt.mimusic.loader.cursor.SortedCursor;

import java.util.ArrayList;

/**
 * Created by Admin on 12/13/17.
 */

public class TopTrackLoader extends SongLoader {
    
    private static Context context;
    private static  QuerryType querryType;

    public  TopTrackLoader(Context context, QuerryType querryType) {
        this.context = context;
        this.querryType = querryType;
    }
    public static Cursor getCursor() {
        SortedCursor retCursor = null;
        if (querryType == QuerryType.TOP_TRACK) {
            retCursor = makeTopTracksCursor(context);
        } else if (querryType == QuerryType.RECENT_SONGS) {
            retCursor = makeRecentTracksCursor(context);
        }

        if (retCursor != null) {
            ArrayList<Long> missingIds = retCursor.getmMissingIds();
            if (missingIds != null && missingIds.size() > 0) {
                for (long id : missingIds) {
                    if (querryType == QuerryType.TOP_TRACK) {
                        SongPlayedCounter.getInstance(context).removeItem(id);
                    } else if (querryType == QuerryType.RECENT_SONGS) {
                        RecentStore.getRecentStoreInstance(context).removeItem(id);
                    }
                }
            }
        }

        return retCursor;
    }



    public static final SortedCursor makeSortedCursor(final Context context, final Cursor cursor,
                                                      final int idColumn) {
        if (cursor != null && cursor.moveToFirst()) {

            StringBuilder selection = new StringBuilder();
            selection.append(BaseColumns._ID);
            selection.append(" IN (");

            long[] order = new long[cursor.getCount()];

            long id = cursor.getLong(idColumn);
            selection.append(id);
            order[cursor.getPosition()] = id;

            while (cursor.moveToNext()) {
                selection.append(",");

                id = cursor.getLong(idColumn);
                order[cursor.getPosition()] = id;
                selection.append(String.valueOf(id));
            }

            selection.append(")");

            Cursor songCursor = makeSongCursor(context, selection.toString(), null);
            if (songCursor != null) {
                return new SortedCursor(songCursor, order, BaseColumns._ID, null);
            }
        }

        return null;
    }
    public static final SortedCursor makeTopTracksCursor(final Context context) {

        Cursor songs = SongPlayedCounter.getInstance(context).getTopPlayedResults(Constant.NUMBER_OF_TOP_SONG);

        try {
            return makeSortedCursor(context, songs,
                    songs.getColumnIndex(SongPlayedCounter.SongPlayCountColumns.ID));
        } finally {
            if (songs != null) {
                songs.close();
                songs = null;
            }
        }
    }
    public static final SortedCursor makeRecentTracksCursor(final Context context) {

        Cursor songs = RecentStore.getRecentStoreInstance(context).queryRecentIds(null);

        try {
            return makeSortedCursor(context, songs,
                    songs.getColumnIndex(SongPlayedCounter.SongPlayCountColumns.ID));
        } finally {
            if (songs != null) {
                songs.close();
                songs = null;
            }
        }
    }

    public static enum QuerryType{
        TOP_TRACK,
        RECENT_SONGS;
    }


}
