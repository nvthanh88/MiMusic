package com.nvt.mimusic.core;

import android.media.MediaPlayer;
import android.os.Handler;
import android.os.PowerManager;
import android.util.Log;

import java.lang.ref.WeakReference;

/**
 * Created by Admin on 11/22/17.
 * Custom MediaPlayer
 */



public  final class MultiPlayer implements MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener {
    private static final String TAG = "MultiPlayer";
    public final WeakReference<MusicService> musicServiceWeakReference ;
    private MediaPlayer mMediaPlayer = new MediaPlayer();

    public MultiPlayer(MusicService mMusicService) {
        this.musicServiceWeakReference = new WeakReference<MusicService>(mMusicService);
        mMediaPlayer.setWakeMode(mMusicService.getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
    }
    private MediaPlayer mCurrentPlayer = new MediaPlayer();


    private MediaPlayer mNextMediaPlayer;

    private Handler mHandler;

    private boolean mIsInitialized = false;
    public boolean isInitialized() {
        return mIsInitialized;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        Log.i(TAG, "onCompletion: ");

    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Log.e(TAG, "onError: "+ what );
        return false;
    }
    public long position(){
        return mCurrentPlayer.getCurrentPosition();
    }
    public long duration() {
        return mCurrentPlayer.getDuration();
    }
    public long seek(final long whereTo) {
        mCurrentPlayer.seekTo((int) whereTo);
        return whereTo;
    }
}
