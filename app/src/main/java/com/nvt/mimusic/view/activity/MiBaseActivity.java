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
import android.view.View;

import com.nvt.mimusic.MiCoreService;
import com.nvt.mimusic.R;
import com.nvt.mimusic.core.MusicCorePlayer;
import com.nvt.mimusic.core.MusicService;
import com.nvt.mimusic.listener.MediaStateListener;
import com.nvt.mimusic.utils.PlayBackStatus;
import com.nvt.mimusic.view.fragment.control.QuickControlFragment;
import com.nvt.mimusic.view.panel.SlidingUpPanelLayout;

import java.util.ArrayList;

import static com.nvt.mimusic.core.MusicCorePlayer.mMiCoreService;

public class MiBaseActivity extends AppCompatActivity implements ServiceConnection,MediaStateListener{
    private String TAG = getClass().getSimpleName();
    private PlayBackStatus mPlayBackStatus;
    private MusicCorePlayer.ServiceToken mToken;
    private ArrayList<MediaStateListener> mediaStateListenerArrayList = new ArrayList<>();


    /**
     * On Create Life Cycle try to bind Core Service
     * */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPlayBackStatus = new PlayBackStatus(this);
        //Bound Service
        mToken = MusicCorePlayer.bindToService(this,this);
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
    public void setupSlidePanel(SlidingUpPanelLayout panelLayout)
    {
        panelLayout.setPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {

            }

            @Override
            public void onPanelCollapsed(View panel) {

            }

            @Override
            public void onPanelExpanded(View panel) {

            }

            @Override
            public void onPanelAnchored(View panel) {

            }

            @Override
            public void onPanelHidden(View panel) {

            }
        });
    }
    public void setMediaStateListenerListener(final MediaStateListener status) {
        if (status == this) {
            throw new UnsupportedOperationException("Override the method, don't add a listener");
        }

        if (status != null) {
            mediaStateListenerArrayList.add(status);
        }
    }
    public class initQuickControls extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            QuickControlFragment fragment1 = new QuickControlFragment();
            FragmentManager fragmentManager1 = getSupportFragmentManager();
            fragmentManager1.beginTransaction()
                    .replace(R.id.quickcontrols_container, fragment1).commitAllowingStateLoss();
            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {
//            QuickControlsFragment.topContainer.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    NavigationUtils.navigateToNowplaying(BaseActivity.this, false);
//                }
//            });
        }

        @Override
        protected void onPreExecute() {
        }
    }

}
