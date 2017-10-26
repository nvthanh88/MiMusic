package com.nvt.mimusic.core;

import android.app.Service;
import android.content.ComponentName;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;

import android.content.SharedPreferences;
import android.database.ContentObserver;

import android.media.AudioManager;
import android.media.MediaPlayer;

import android.net.Uri;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;

import android.os.RemoteException;

import android.support.v4.media.session.MediaSessionCompat;


import com.nvt.mimusic.MiCoreService;

import com.nvt.mimusic.helper.MusicPlaybackTrack;


import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;


/**
 * Created by Admin on 10/25/17.
 */

public class MusicService extends Service {

    private int mNextPlayPos = -1;
    private MusicPlayerHandler mPlayerHandler;
    private int mRepeatMode = REPEAT_NONE;
    public static final int REPEAT_NONE = 0;
    public static final int REPEAT_CURRENT = 1;
    private static final int FADEDOWN = 6;
    private static final int FADEUP = 7;
    public static final int SHUFFLE_AUTO = 2;
    private static final int FOCUSCHANGE = 5;
    public static final String META_CHANGED = "com.naman14.timber.metachanged";
    public static final String QUEUE_CHANGED = "com.naman14.timber.queuechanged";
    public static final int MAX_HISTORY_SIZE = 1000;
    private ArrayList<MusicPlaybackTrack> mPlaylist = new ArrayList<MusicPlaybackTrack>(100);
    private final AudioManager.OnAudioFocusChangeListener mAudioFocusListener = new AudioManager.OnAudioFocusChangeListener() {

        @Override
        public void onAudioFocusChange(final int focusChange) {
            mPlayerHandler.obtainMessage(FOCUSCHANGE, focusChange, 0).sendToTarget();
        }
    };
    private MediaPlayer mPlayer;
    public static final int SHUFFLE_NORMAL = 1;
    private ContentObserver mMediaStoreObserver;
    private HandlerThread mHandlerThread;
    private ComponentName mMediaButtonReceiverComponent;
    private SharedPreferences mPreferences;
    private boolean mServiceInUse = false;
    private final IBinder mBinder = new MusicPlayerHandler.ServiceStub(this);
    private MediaSessionCompat mSession;
    private AudioManager mAudioManager;
    @Override
    public void onCreate() {
        super.onCreate();

       /* mHandlerThread = new HandlerThread("MusicPlayerHandler",
                android.os.Process.THREAD_PRIORITY_BACKGROUND);
        mHandlerThread.start();
        mPlayerHandler = new MusicPlayerHandler(this, mHandlerThread.getLooper());
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);*/
        mPlayer = new MediaPlayer();
    }


    @Override
    public IBinder onBind(Intent intent) {
        mServiceInUse = true;
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent,  int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }
    public void play(long id) {
        Uri uri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Uri myUri = ContentUris.withAppendedId(uri, id);
        mPlayer = new MediaPlayer();
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mPlayer.setDataSource(getApplicationContext(), myUri);
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static final class MusicPlayerHandler extends Handler {
        private final WeakReference<MusicService> mService;

        public MusicPlayerHandler(final MusicService service, final Looper looper) {
            super(looper);
            mService = new WeakReference<>(service);
        }


        @Override
        public void handleMessage(final Message msg) {
            final MusicService service = mService.get();
            if (service == null) {
                return;
            }

            synchronized (service) {
                switch (msg.what) {

                }
            }
        }

        private static final class ServiceStub extends MiCoreService.Stub {

            private final WeakReference<MusicService> mService;

            private ServiceStub(final MusicService service) {
                mService = new WeakReference<>(service);
            }


            @Override
            public void openFile(final String path) throws RemoteException {

            }

            @Override
            public void open(final long[] list, final int position, long sourceId, int sourceType)
                    throws RemoteException {
            }

            @Override
            public void stop() throws RemoteException {

            }

            @Override
            public void pause() throws RemoteException {

            }

            @Override
            public void play(long id) throws RemoteException {
                mService.get().play(id);
            }




            @Override
            public void prev(boolean forcePrevious) throws RemoteException {
            }

            @Override
            public void next() throws RemoteException {

            }

            @Override
            public void enqueue(final long[] list, final int action, long sourceId, int sourceType)
                    throws RemoteException {

            }

            @Override
            public void moveQueueItem(final int from, final int to) throws RemoteException {

            }

            @Override
            public void refresh() throws RemoteException {

            }

            @Override
            public void playlistChanged() throws RemoteException {

            }

            @Override
            public boolean isPlaying() throws RemoteException {
                return false;
            }

            @Override
            public long[] getQueue() throws RemoteException {
                return new long[0];
            }

            @Override
            public long getQueueItemAtPosition(int position) throws RemoteException {
                return 0;
            }

            @Override
            public int getQueueSize() throws RemoteException {
                return 0;
            }

            @Override
            public int getQueueHistoryPosition(int position) throws RemoteException {
                return 0;
            }

            @Override
            public int getQueueHistorySize() throws RemoteException {
                return 0;
            }

            @Override
            public int[] getQueueHistoryList() throws RemoteException {

                return new int[0];
            }

            @Override
            public long duration() throws RemoteException {
                return 0;
            }

            @Override
            public long position() throws RemoteException {
                return 0;
            }

            @Override
            public long seek(final long position) throws RemoteException {
                return 0;
            }

            @Override
            public void seekRelative(final long deltaInMs) throws RemoteException {
            }

            @Override
            public long getAudioId() throws RemoteException {
                return 0;
            }

            @Override
            public MusicPlaybackTrack getCurrentTrack() throws RemoteException {
                return null;
            }

            @Override
            public MusicPlaybackTrack getTrack(int index) throws RemoteException {
                return null;
            }


            @Override
            public long getNextAudioId() throws RemoteException {
                return 0;
            }

            @Override
            public long getPreviousAudioId() throws RemoteException {
                return 0;
            }

            @Override
            public long getArtistId() throws RemoteException {
                return 0;
            }

            @Override
            public long getAlbumId() throws RemoteException {
                return 0;
            }

            @Override
            public String getArtistName() throws RemoteException {
                return null;
            }

            @Override
            public String getTrackName() throws RemoteException {
                return null;
            }

            @Override
            public String getAlbumName() throws RemoteException {
                return null;
            }

            @Override
            public String getPath() throws RemoteException {
                return null;
            }

            @Override
            public int getQueuePosition() throws RemoteException {
                return 0;
            }

            @Override
            public void setQueuePosition(final int index) throws RemoteException {

            }

            @Override
            public int getShuffleMode() throws RemoteException {
                return 0;
            }

            @Override
            public void setShuffleMode(final int shufflemode) throws RemoteException {

            }

            @Override
            public int getRepeatMode() throws RemoteException {
                return 0;
            }

            @Override
            public void setRepeatMode(final int repeatmode) throws RemoteException {

            }

            @Override
            public int removeTracks(final int first, final int last) throws RemoteException {
                return 0;
            }


            @Override
            public int removeTrack(final long id) throws RemoteException {
                return 0;
            }


            @Override
            public boolean removeTrackAtPosition(final long id, final int position)
                    throws RemoteException {
                return false;

            }


            @Override
            public int getMediaMountedCount() throws RemoteException {
                return 0;
            }

            @Override
            public int getAudioSessionId() throws RemoteException {
                return 0;
            }


        }
    }

}
