package com.nvt.mimusic.view.activity;

import android.Manifest;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

import android.content.ComponentName;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.nvt.mimusic.MiCoreService;
import com.nvt.mimusic.R;
import com.nvt.mimusic.adapter.AlbumAdapter;

import com.nvt.mimusic.base.fragment.MiBaseFragment;
import com.nvt.mimusic.constant.ScreenIDs;

import com.nvt.mimusic.core.MusicCorePlayer;
import com.nvt.mimusic.core.MusicService;
import com.nvt.mimusic.listener.MediaStateListener;
import com.nvt.mimusic.model.AlbumModel;

import com.nvt.mimusic.utils.PlayBackStatus;
import com.nvt.mimusic.view.fragment.home.AlbumFragment;
import com.nvt.mimusic.view.fragment.home.SongFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import static com.nvt.mimusic.core.MusicCorePlayer.mMiCoreService;

public class MiBaseActivity extends AppCompatActivity implements ServiceConnection,MediaStateListener{
    private String TAG = getClass().getSimpleName();
    private PlayBackStatus mPlayBackStatus;
    private MusicCorePlayer.ServiceToken mToken;
    private ArrayList<MediaStateListener> mediaStateListenerArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPlayBackStatus = new PlayBackStatus(this);
        //Bound Service
        mToken = MusicCorePlayer.bindToService(getApplicationContext(),this);
                //make volume keys change multimedia volume even if music is not playing now
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

    }

    @Override
    protected void onStart() {
        super.onStart();
        final IntentFilter filter = new IntentFilter();
        // Play and pause changes
        filter.addAction(MusicService.PLAYSTATE_CHANGED);
        // Track changes
        filter.addAction(MusicService.META_CHANGED);
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
            MusicCorePlayer.unbindFromService(mToken);
            mToken = null;
        }
        try{
            unregisterReceiver(mPlayBackStatus);
        }catch (Throwable e)
        {
            mediaStateListenerArrayList.clear();
        }


    }

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
}
