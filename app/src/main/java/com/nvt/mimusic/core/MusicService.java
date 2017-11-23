package com.nvt.mimusic.core;

import android.app.Service;
import android.content.ComponentName;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;

import android.content.SharedPreferences;
import android.database.ContentObserver;

import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;

import android.net.Uri;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;

import android.os.PowerManager;
import android.os.RemoteException;

import android.provider.MediaStore;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.Log;
import android.widget.Toast;


import com.nvt.mimusic.MiCoreService;

import com.nvt.mimusic.helper.MusicPlaybackTrack;


import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;


/**
 * Created by Admin on 10/25/17.
 */

public class MusicService extends Service {
    public static final String PLAYSTATE_CHANGED ="mimusic.playstatechange" ;
    public static final String REFRESH ="mimusic.refresh" ;
    public static final String PLAYLIST_CHANGED ="mimusic.playlistchange" ;
    public static final String TRACK_ERROR ="mimusic.trackerror" ;
    public static final String META_CHANGED = "mimusic.metachanged";
    public static final String QUEUE_CHANGED = "mimusic.queuechanged";
    private static final String TAG = "MusicService";
    public static final int SHUFFLE_NORMAL = 1;
    private HandlerThread mHandlerThread;
    private AudioManager mAudioManager;
    private MultiPlayer mPlayer;


    private Cursor mCursor;


    @Override
    public void onCreate() {
        super.onCreate();
        setUpMediaSession();
        mHandlerThread = new HandlerThread("MusicPlayerHandler",
                android.os.Process.THREAD_PRIORITY_BACKGROUND);
        mHandlerThread.start();
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        mPlayer = new MultiPlayer(this);

    }





    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    private MediaSessionCompat mSession;
    private void setUpMediaSession() {
        mSession = new MediaSessionCompat(this, "MiMusic");
        mSession.setCallback(new MediaSessionCompat.Callback() {
            @Override
            public void onPause() {

            }

            @Override
            public void onPlay() {

            }

            @Override
            public void onSeekTo(long pos) {

            }

            @Override
            public void onSkipToNext() {

            }

            @Override
            public void onSkipToPrevious() {

            }

            @Override
            public void onStop() {

            }
        });
        mSession.setFlags(MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
    }

    /**
     * Get position from Media player
     * */
    public long position(){
        if (mPlayer.isInitialized())
        {
            return mPlayer.position();
        }
        return  -1;
    }
    /**
     * Seek to position
     * */
    public long seek(long position) {
        if (mPlayer.isInitialized()) {
            if (position < 0) {
                position = 0;
            } else if (position > mPlayer.duration()) {
                position = mPlayer.duration();
            }
            long result = mPlayer.seek(position);
            //notifyChange(POSITION_CHANGED);
            return result;
        }
        return -1;
    }
    /**
     * Get track name
     * */
    public String getTrackName() {
        synchronized (this) {
            if (mCursor == null) {
                return null;
            }
            return mCursor.getString(mCursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.TITLE));
        }
    }
    /**
    * Get ArtistName
    * */
    public String getArtistName() {
        synchronized (this) {
            if (mCursor == null) {
                return null;
            }
            return mCursor.getString(mCursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ARTIST));
        }
    }
    /**
     * Get duration
     * */
    public long duration() {
        if (mPlayer.isInitialized()) {
            return mPlayer.duration();
        }
        return -1;
    }




}
