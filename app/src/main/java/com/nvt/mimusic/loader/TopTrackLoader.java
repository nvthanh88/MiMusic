package com.nvt.mimusic.loader;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.provider.BaseColumns;

import com.nvt.mimusic.loader.cursor.SortedCursor;

/**
 * Created by Admin on 12/13/17.
 */

public class TopTrackLoader extends SongLoader {
    private Context context;
    private QuerryType querryType;

    public TopTrackLoader(Context context, QuerryType querryType) {
        this.context = context;
        this.querryType = querryType;
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

    private enum QuerryType{
        TOP_TRACK,
        RECENT_SONGS;
    }


}
