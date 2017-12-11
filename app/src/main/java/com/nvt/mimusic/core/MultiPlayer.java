package com.nvt.mimusic.core;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.PowerManager;
import android.util.Log;

import com.nvt.mimusic.constant.Constant;

import java.io.IOException;
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
    private String mNextMediaPath;
    public boolean isInitialized() {
        return mIsInitialized;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (mp == mCurrentPlayer && mNextMediaPlayer != null) {
            mCurrentPlayer.release();
            mCurrentPlayer = mNextMediaPlayer;
            mNextMediaPath = null;
            mNextMediaPlayer = null;
            mHandler.sendEmptyMessage(Constant.TRACK_WENT_TO_NEXT);
        }

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
    public void stop() {
        mCurrentPlayer.reset();
        mIsInitialized = false;
    }
    public int getAudioSessionId() {
        return mCurrentPlayer.getAudioSessionId();
    }
    public void start() {
        mCurrentPlayer.start();
    }
    public void setDataSource(final String path) {
        try {
            mIsInitialized = setDataSourceImpl(mCurrentPlayer, path);
            if (mIsInitialized) {
                setNextDataSource(null);
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }
    private boolean setDataSourceImpl(final MediaPlayer player, final String path) {
        try {
            player.reset();
            player.setOnPreparedListener(null);
            if (path.startsWith("content://")) {
                player.setDataSource(musicServiceWeakReference.get(), Uri.parse(path));
            } else {
                player.setDataSource(path);
            }
            player.setAudioStreamType(AudioManager.STREAM_MUSIC);

            player.prepare();
        } catch (final IOException todo) {

            return false;
        } catch (final IllegalArgumentException todo) {

            return false;
        }
        player.setOnCompletionListener(this);
        player.setOnErrorListener(this);
        return true;
    }
    public void setNextDataSource(final String path) {
        mNextMediaPath = null;
        try {
            mCurrentPlayer.setNextMediaPlayer(null);
        } catch (IllegalArgumentException e) {
            Log.i(TAG, "Next media player is current one, continuing");
        } catch (IllegalStateException e) {
            Log.e(TAG, "Media player not initialized!");
            return;
        }
        if (mNextMediaPlayer != null) {
            mNextMediaPlayer.release();
            mNextMediaPlayer = null;
        }
        if (path == null) {
            return;
        }
        mNextMediaPlayer = new MediaPlayer();
        mNextMediaPlayer.setWakeMode(musicServiceWeakReference.get(), PowerManager.PARTIAL_WAKE_LOCK);
        mNextMediaPlayer.setAudioSessionId(getAudioSessionId());
        try {
            if (setDataSourceImpl(mNextMediaPlayer, path)) {
                mNextMediaPath = path;
                mCurrentPlayer.setNextMediaPlayer(mNextMediaPlayer);
            } else {
                if (mNextMediaPlayer != null) {
                    mNextMediaPlayer.release();
                    mNextMediaPlayer = null;
                }
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }
    public void setHandler(final Handler handler) {
        mHandler = handler;
    }

}
