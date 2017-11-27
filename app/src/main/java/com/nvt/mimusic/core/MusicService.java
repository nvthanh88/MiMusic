package com.nvt.mimusic.core;

import android.app.Service;
import android.content.ComponentName;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;

import android.content.SharedPreferences;
import android.database.ContentObserver;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;

import android.net.Uri;

import android.os.Build;
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
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.widget.Toast;


import com.nostra13.universalimageloader.core.ImageLoader;
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
    public static final String POSITION_CHANGED = "mimusic.positionchanged";
    public static final String REFRESH ="mimusic.refresh" ;
    public static final String PLAYLIST_CHANGED ="mimusic.playlistchange" ;
    public static final String TRACK_ERROR ="mimusic.trackerror" ;
    public static final String META_CHANGED = "mimusic.metachanged";
    public static final String QUEUE_CHANGED = "mimusic.queuechanged";
    private static final String TAG = "MusicService";

    private HandlerThread mHandlerThread;
    private AudioManager mAudioManager;
    private MultiPlayer mPlayer;

    private ArrayList<MusicPlaybackTrack> mPlaylist = new ArrayList<MusicPlaybackTrack>(100);
    private Cursor mCursor;
    private Cursor mAlbumCursor;
    private int mPlayPos = -1;
    private int mShuffleMode = SHUFFLE_NONE;
    public static final int SHUFFLE_NONE = 0;
    public static final int SHUFFLE_NORMAL = 1;
    public static final int SHUFFLE_AUTO = 2;
    private boolean mIsSupposedToBePlaying = false;
    private boolean mShowAlbumArtOnLockscreen;


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
    /**
     * GetAudio ID
     * */
    public long getAudioId() {
        MusicPlaybackTrack track = getCurrentTrack();
        if (track != null) {
            return track.mId;
        }

        return -1;
    }
    public MusicPlaybackTrack getCurrentTrack() {
        return getTrack(mPlayPos);
    }
    public synchronized MusicPlaybackTrack getTrack(int index) {
        if (index >= 0 && index < mPlaylist.size() && mPlayer.isInitialized()) {
            return mPlaylist.get(index);
        }

        return null;
    }

    /**
     * Prepare Data to play
     *
     * */
    public void prepareData(final long[] list, final int position, long sourceId, MiCoreApplication.IdType sourceType) {
        synchronized (this) {
            if (mShuffleMode == SHUFFLE_AUTO) {
                mShuffleMode = SHUFFLE_NORMAL;
            }
            final long oldId = getAudioId();
            final int listlength = list.length;
            boolean newlist = true;
            if (mPlaylist.size() == listlength) {
                newlist = false;
                for (int i = 0; i < listlength; i++) {
                    if (list[i] != mPlaylist.get(i).mId) {
                        newlist = true;
                        break;
                    }
                }
            }
            if (newlist) {
                addToPlayList(list, -1, sourceId, sourceType);
                notifyChange(QUEUE_CHANGED);
            }
            if (position >= 0) {
                mPlayPos = position;
            } else {
                mPlayPos = mShuffler.nextInt(mPlaylist.size());
            }
            mHistory.clear();
            openCurrentAndNext();
            if (oldId != getAudioId()) {
                notifyChange(META_CHANGED);
            }
        }

    }
    private void addToPlayList(final long[] list, int position, long sourceId, MiCoreApplication.IdType sourceType) {
        final int addlen = list.length;
        if (position < 0) {
            mPlaylist.clear();
            position = 0;
        }

        mPlaylist.ensureCapacity(mPlaylist.size() + addlen);
        if (position > mPlaylist.size()) {
            position = mPlaylist.size();
        }

        final ArrayList<MusicPlaybackTrack> arrayList = new ArrayList<MusicPlaybackTrack>(addlen);
        for (int i = 0; i < list.length; i++) {
            arrayList.add(new MusicPlaybackTrack(list[i], sourceId, sourceType, i));
        }

        mPlaylist.addAll(position, arrayList);

        if (mPlaylist.size() == 0) {
            closeCursor();
            notifyChange(META_CHANGED);
        }
    }
    /**
     * Update Media Session
     * */

    private void updateMediaSession(final String whatSession)
    {
        int playState = mIsSupposedToBePlaying ? PlaybackStateCompat.STATE_PLAYING:PlaybackStateCompat.STATE_PAUSED;
        if (whatSession == PLAYSTATE_CHANGED || whatSession == POSITION_CHANGED)
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mSession.setPlaybackState(new PlaybackStateCompat.Builder()
                        .setState(playState, position(), 1.0f)
                        .setActions(PlaybackStateCompat.ACTION_PLAY | PlaybackStateCompat.ACTION_PAUSE | PlaybackStateCompat.ACTION_PLAY_PAUSE |
                                PlaybackStateCompat.ACTION_SKIP_TO_NEXT | PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS)
                        .build());
            }

        }else if (whatSession == META_CHANGED || whatSession == QUEUE_CHANGED)
        {
            Bitmap albumArt = null;
            if (mShowAlbumArtOnLockscreen) {
                albumArt = ImageLoader.getInstance().loadImageSync(MiCoreApplication.getAlbumArtUri(getAlbumId()).toString());
                if (albumArt != null) {

                    Bitmap.Config config = albumArt.getConfig();
                    if (config == null) {
                        config = Bitmap.Config.ARGB_8888;
                    }
                    albumArt = albumArt.copy(config, false);
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    mSession.setMetadata(new MediaMetadataCompat.Builder()
                            .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, getArtistName())
                            .putString(MediaMetadataCompat.METADATA_KEY_ALBUM_ARTIST, getAlbumArtistName())
                            .putString(MediaMetadataCompat.METADATA_KEY_ALBUM, getAlbumName())
                            .putString(MediaMetadataCompat.METADATA_KEY_TITLE, getTrackName())
                            .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, duration())
                            .putLong(MediaMetadataCompat.METADATA_KEY_TRACK_NUMBER, getQueuePosition() + 1)
                            .putLong(MediaMetadataCompat.METADATA_KEY_NUM_TRACKS, getQueue().length)
                            .putString(MediaMetadataCompat.METADATA_KEY_GENRE, getGenreName())
                            .putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, albumArt)
                            .build());

                    mSession.setPlaybackState(new PlaybackStateCompat.Builder()
                            .setState(playState, position(), 1.0f)
                            .setActions(PlaybackStateCompat.ACTION_PLAY | PlaybackStateCompat.ACTION_PAUSE | PlaybackStateCompat.ACTION_PLAY_PAUSE |
                                    PlaybackStateCompat.ACTION_SKIP_TO_NEXT | PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS)
                            .build());
                }
            }

        }


    }
    public long getAlbumId() {
        synchronized (this) {
            if (mCursor == null) {
                return -1;
            }
            return mCursor.getLong(mCursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ALBUM_ID));
        }
    }
    public String getAlbumArtistName() {
        synchronized (this) {
            if (mAlbumCursor == null) {
                return null;
            }
            return mAlbumCursor.getString(mAlbumCursor.getColumnIndexOrThrow(MediaStore.Audio.AlbumColumns.ARTIST));
        }
    }
    public String getAlbumName() {
        synchronized (this) {
            if (mCursor == null) {
                return null;
            }
            return mCursor.getString(mCursor.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ALBUM));
        }
    }
    public String getGenreName() {
        synchronized (this) {
            if (mCursor == null || mPlayPos < 0 || mPlayPos >= mPlaylist.size()) {
                return null;
            }
            String[] genreProjection = {MediaStore.Audio.Genres.NAME};
            Uri genreUri = MediaStore.Audio.Genres.getContentUriForAudioId("external",
                    (int) mPlaylist.get(mPlayPos).mId);
            Cursor genreCursor = getContentResolver().query(genreUri, genreProjection,
                    null, null, null);
            if (genreCursor != null) {
                try {
                    if (genreCursor.moveToFirst()) {
                        return genreCursor.getString(
                                genreCursor.getColumnIndexOrThrow(MediaStore.Audio.Genres.NAME));
                    }
                } finally {
                    genreCursor.close();
                }
            }
            return null;
        }
    }
    public int getQueuePosition() {
        synchronized (this) {
            return mPlayPos;
        }
    }
    public long[] getQueue() {
        synchronized (this) {
            final int len = mPlaylist.size();
            final long[] list = new long[len];
            for (int i = 0; i < len; i++) {
                list[i] = mPlaylist.get(i).mId;
            }
            return list;
        }
    }




}
