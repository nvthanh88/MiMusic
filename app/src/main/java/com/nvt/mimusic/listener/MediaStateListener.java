package com.nvt.mimusic.listener;

/**
 * Created by Admin on 10/20/17.
 */

public interface MediaStateListener {

    void restartLoader();


    void onPlaylistChanged();


    void onMetaChanged();
}
