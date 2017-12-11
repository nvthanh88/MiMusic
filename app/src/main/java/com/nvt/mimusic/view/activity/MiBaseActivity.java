package com.nvt.mimusic.view.activity;


import android.content.ComponentName;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.media.AudioManager;

import android.os.AsyncTask;
import android.os.IBinder;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;


import com.nvt.mimusic.MiCoreService;
import com.nvt.mimusic.R;
import com.nvt.mimusic.constant.Constant;
import com.nvt.mimusic.core.MusicPlayer;
import com.nvt.mimusic.core.MusicService;
import com.nvt.mimusic.listener.MediaStateListener;
import com.nvt.mimusic.utils.PlayBackStatus;



import java.util.ArrayList;

import static com.nvt.mimusic.core.MusicPlayer.mMiCoreService;

public class MiBaseActivity extends AppCompatActivity implements ServiceConnection,MediaStateListener{
    private String TAG = getClass().getSimpleName();
    private PlayBackStatus mPlayBackStatus;
    private MusicPlayer.ServiceToken mToken;
    private ArrayList<MediaStateListener> mediaStateListenerArrayList = new ArrayList<>();


    /**
     * On Create Life Cycle try to bind Core Service
     * */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPlayBackStatus = new PlayBackStatus(this);
        //Bound Service
        mToken = MusicPlayer.bindToService(this,this);
                //make volume keys change multimedia volume even if music is not playing now
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

    }
    /**
     * On Start Life Cycle register Receiver
     * */
    @Override
    protected void onStart() {
        super.onStart();
        final IntentFilter filter = new IntentFilter();
        // Play and pause changes
        filter.addAction(MusicService.PLAYSTATE_CHANGED);
        // Track changes
        filter.addAction(Constant.META_CHANGED);
        // Update a list, probably the playlist fragment's
        filter.addAction(MusicService.REFRESH);
        // If a playlist has changed, notify us
        filter.addAction(MusicService.PLAYLIST_CHANGED);
        // If there is an error playing a track
        filter.addAction(MusicService.TRACK_ERROR);

        registerReceiver(mPlayBackStatus, filter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        onMetaChanged();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        super.onDestroy();
        if (mToken != null) {
            MusicPlayer.unbindFromService(mToken);
            mToken = null;
        }
        try{
            unregisterReceiver(mPlayBackStatus);
        }catch (Throwable e)
        {
            mediaStateListenerArrayList.clear();
        }


    }
    /**
     * Connect to Remote Service use aidl
     * */
    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        Log.d(TAG, "onServiceConnected: Connect to service "+ service.toString());
        mMiCoreService = MiCoreService.Stub.asInterface(service);
        onMetaChanged();
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        mMiCoreService = null;
    }

    @Override
    public void restartLoader() {
        for(MediaStateListener mediaStateListener:mediaStateListenerArrayList)
        {
            if (mediaStateListener != null)
                mediaStateListener.restartLoader();
        }
    }

    @Override
    public void onPlaylistChanged() {
        for(MediaStateListener mediaStateListener:mediaStateListenerArrayList)
        {
            if (mediaStateListener != null)
                mediaStateListener.onPlaylistChanged();
        }
    }

    @Override
    public void onMetaChanged() {
        for(MediaStateListener mediaStateListener:mediaStateListenerArrayList)
        {
            if (mediaStateListener != null)
                mediaStateListener.onMetaChanged();
        }
    }
    /**
     * Setup Slide Panel
     *
     * */

    public void setMediaStateListenerListener(final MediaStateListener status) {
        if (status == this) {
            throw new UnsupportedOperationException("Override the method, don't add a listener");
        }

        if (status != null) {
            mediaStateListenerArrayList.add(status);
        }
    }




}
