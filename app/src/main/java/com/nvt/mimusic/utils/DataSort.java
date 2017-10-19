package com.nvt.mimusic.utils;

import android.provider.MediaStore;

/**
 * Created by Admin on 10/19/17.
 */

public final class DataSort {
    public interface albumSortOrder{

        String ALBUM_A_Z = MediaStore.Audio.Albums.DEFAULT_SORT_ORDER;
        String SONG_A_Z = MediaStore.Audio.Media.DEFAULT_SORT_ORDER;


    }

}
