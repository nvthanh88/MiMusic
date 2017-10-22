package com.nvt.mimusic.core;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.nfc.Tag;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

/**
 * Created by THANH.NV on 10/22/2017.
 */

public class MusicService extends Service {
    private static final String TAG = "Music Playback Service";
    private static final boolean F = false;
    private NotificationManagerCompat mNotificationManager;

    @Override
    public void onCreate() {
        super.onCreate();
        mNotificationManager =  NotificationManagerCompat.from(this);
        // Get pointer to media service state
        mPlaybackStateStore = MusicPlaybackState.getInstance(this);
        mSongPlayCount = SongPlayCount.getInstance(this);
        mRecentStore = RecentStore.getInstance(this);


    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG ," onStartCommand" + intent);

        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind: " + intent);
        return null;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: " );
        super.onDestroy();
    }
}
