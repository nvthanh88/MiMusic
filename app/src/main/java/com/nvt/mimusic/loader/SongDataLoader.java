package com.nvt.mimusic.loader;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.nvt.mimusic.model.SongModel;
import com.nvt.mimusic.utils.PreferencesUtility;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 10/19/17.
 */

public class SongDataLoader {
    private static final long[] sEmptyList = new long[0];
    public static ArrayList<SongModel> getSongListForCursor(Cursor cursor){
        ArrayList arrayList = new ArrayList();
        if (cursor != null && cursor.moveToFirst())
        do
        {
            arrayList.add(new SongModel(cursor.getString(0),cursor.getLong(1),cursor.getString(2),cursor.getLong(3),cursor.getString(4),cursor.getLong(5),cursor.getInt(6)));
        }
        while (cursor.moveToNext());
        if (cursor != null)
        {
            cursor.close();
        }
        return arrayList;

    }
    public static SongModel getSongForCursor(Cursor cursor)
    {
        SongModel songModel = new SongModel();
        if (cursor != null && cursor.moveToFirst())
        {
            songModel = new SongModel(cursor.getString(0),cursor.getLong(1),cursor.getString(2),cursor.getLong(3),cursor.getString(4),cursor.getLong(5),cursor.getInt(6));
        }
        if (cursor != null)
            cursor.close();
        return songModel;
    }
    public static ArrayList<SongModel> getAllSongs(Context context) {
        return getSongListForCursor(makeSongCursor(context, null, null));
    }
    public static Cursor makeSongCursor(Context context, String selection, String[] paramArrayOfString) {
        final String songSortOrder = PreferencesUtility.getInstance(context).getSongSortOrder();
        return makeSongCursor(context, selection, paramArrayOfString, songSortOrder);
    }
    private static Cursor makeSongCursor(Context context, String selection, String[] paramArrayOfString, String sortOrder) {
        String selectionStatement = "is_music=1 AND title != ''";

        if (!TextUtils.isEmpty(selection)) {
            selectionStatement = selectionStatement + " AND " + selection;
        }
        return context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, new String[]{ "title","_id", "artist", "artist_id", "album", "album_id","track"}, selectionStatement, paramArrayOfString, sortOrder);

    }

}
