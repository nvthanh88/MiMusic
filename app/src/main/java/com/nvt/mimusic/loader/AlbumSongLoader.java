package com.nvt.mimusic.loader;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.nvt.mimusic.model.Song;

import java.util.ArrayList;

/**
 * Created by Admin on 12/7/17.
 */

public class AlbumSongLoader {

    public static ArrayList<Song> getAlbumSongList(Context context,long albumId)
    {
        Cursor cursor = makeAlbumSongCursor(context,albumId);
        ArrayList arrayList =new ArrayList();
        if (cursor != null && cursor.moveToFirst())
            do {
                long id = cursor.getLong(0);
                String title = cursor.getString(1);
                String artist = cursor.getString(2);
                String album = cursor.getString(3);
                int duration = cursor.getInt(4);
                int trackNumber = cursor.getInt(5);
                /*This fixes bug where some track numbers displayed as 100 or 200*/
                while (trackNumber >= 1000) {
                    trackNumber -= 1000; //When error occurs the track numbers have an extra 1000 or 2000 added, so decrease till normal.
                }
                long artistId = cursor.getInt(6);
                long albumID = albumId;

                arrayList.add(new Song(title, id, artist,  artistId,  album, albumID, trackNumber, duration));
            }
            while (cursor.moveToNext());
        if (cursor != null)
            cursor.close();
        return arrayList;

    }

    public static Cursor makeAlbumSongCursor(Context context , long albumId)
    {
        ContentResolver contentResolver = context.getContentResolver();
        String string = "is_music=1 AND title != '' AND album_id=" + albumId;
        Cursor cursor = contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,new String[]{"_id", "title", "artist", "album", "duration", "track", "artist_id"},
                string,null,MediaStore.Audio.Media.TRACK + ", "+ MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        return  cursor;
    }
}
