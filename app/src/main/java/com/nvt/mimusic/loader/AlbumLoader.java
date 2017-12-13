package com.nvt.mimusic.loader;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.nvt.mimusic.model.Album;
import com.nvt.mimusic.utils.PreferencesUtility;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 10/19/17.
 */

public class AlbumLoader {
    public static Album getAlbum (Cursor cursor){
        Album album = new Album();
        if (cursor != null)
        {
            if ( cursor.moveToFirst())
            {
                album = new Album(cursor.getLong(0), cursor.getString(1) , cursor.getString(2),cursor.getLong(3), cursor.getInt(4));
            }
        }
        if (cursor !=null)
            cursor.close();
        return album;
    }
    public static List <Album> getAllAlbumForCursor(Cursor cursor){
        ArrayList arrayList = new ArrayList();
        if (cursor != null && cursor.moveToFirst())

        do{
            arrayList.add(new Album(cursor.getLong(0), cursor.getString(1) , cursor.getString(2),cursor.getLong(3), cursor.getInt(4)));
        }
        while (cursor.moveToNext());
        if (cursor != null)
            cursor.close();

        return arrayList;
    }
    public static List<Album> getAllAlbum(Context context ){
        return getAllAlbumForCursor(makeAlbumCursor(context,null,null));
    }
    public static Album getAlbum(Context context , long albumId)
    {
        return getAlbum(makeAlbumCursor(context,"_id =?",new String[]{String.valueOf(albumId)}));
    }


    public static Cursor makeAlbumCursor(Context context , String selection , String[] paramArrayOfString)
    {
        final String albumSortOrder = PreferencesUtility.getInstance(context).getAlbumSortOrder();
        Cursor albumCursor = context.getContentResolver().query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI, new String[]{"_id", "album", "artist", "artist_id", "numsongs"}, selection, paramArrayOfString, albumSortOrder);

        return albumCursor;
    }
}
