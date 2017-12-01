package com.nvt.mimusic.core;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

import com.nvt.mimusic.MiCoreService;
import com.nvt.mimusic.core.MiApplication.IdType;
import java.util.Arrays;
import java.util.WeakHashMap;

/**
 * Created by Admin on 10/25/17.
 */

public class MusicPlayer {
    private static final long[] sEmptyList;
    private static final WeakHashMap<Context, ServiceBinder> mConnectionMap;
    public static MiCoreService mMiCoreService = null;
    public static MusicService mMusicService;


    /**
     * Get position when playing music
     * */
    public static long position (){
        if (mMiCoreService != null)
        {
            try {
                return mMiCoreService.position();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return  0;
    }
    /**
     * Check Music player is playing
     * */
    public static final boolean isPlaying(){
        if(mMiCoreService != null)
            try {
                return  mMiCoreService.isPlaying();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            return false;
    }

    static {
        mConnectionMap = new WeakHashMap<Context, ServiceBinder>();
        sEmptyList = new long[0];
    }

    public static void playAll(final Context context, final long[] list, int position,
                               final long sourceId, final IdType sourceType,
                               final boolean forceShuffle)  {
        if (list == null || list.length == 0 || mMiCoreService == null) {
            return;
        }
        try {
            if (forceShuffle) {
                mMiCoreService.setShuffleMode(MusicService.SHUFFLE_NORMAL);
            }
            final long currentId = mMiCoreService.getAudioId();
            final int currentQueuePosition = getQueuePosition();//-1
            if (position != -1 && currentQueuePosition == position && currentId == list[position]) {
                final long[] playlist = getQueue();
                if (Arrays.equals(list, playlist)) {
                    mMiCoreService.play();
                    return;
                }
            }
            if (position < 0) {
                position = 0;
            }
            mMiCoreService.prepareData(list, forceShuffle ? -1 : position, sourceId, sourceType.mId);
            mMiCoreService.play();
        } catch (final RemoteException ignored) {
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }

    }
    /*Get Queue*/

    public static final long[] getQueue() {
        try {
            if (mMiCoreService != null) {
                return mMiCoreService.getQueue();
            } else {
            }
        } catch (final RemoteException ignored) {
        }
        return sEmptyList;
    }
    public static final int getQueuePosition() {
        try {
            if (mMiCoreService != null) {
                return mMiCoreService.getQueuePosition();
            }
        } catch (final RemoteException ignored) {
        }
        return 0;
    }

    public static void seek(int progress) {
        if (mMiCoreService != null)
        {
            try {
                mMiCoreService.seek(progress);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public static void next() {
        if (mMiCoreService != null)
        {
            try {
                mMiCoreService.next();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public static void prev() {
        if (mMiCoreService != null)
        {
            try {
                mMiCoreService.prev(true);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public static String getTrackName() {
        if (mMiCoreService != null) {
            try {
                return mMiCoreService.getTrackName();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
            return null;
    }

    public static String getArtistName() {
        if (mMiCoreService != null)
        {
            try {
                return mMiCoreService.getArtistName();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static long getCurrentAlbumId() {
        if (mMiCoreService != null)
        {
            try {
                return mMiCoreService.getAlbumId();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return  -1;
    }

    public static long duration() {
        if (mMiCoreService != null)
        {
            try {
                return mMiCoreService.duration();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return  0;
    }

    /*Bind Service*/

    public static final class ServiceBinder implements ServiceConnection {
        private final ServiceConnection mCallback;
        private final Context mContext;


        public ServiceBinder(final ServiceConnection callback, final Context context) {
            mCallback = callback;
            mContext = context;
        }

        @Override
        public void onServiceConnected(final ComponentName className, final IBinder service) {
            mMiCoreService = MiCoreService.Stub.asInterface(service);
            if (mCallback != null) {
                mCallback.onServiceConnected(className, service);
            }
            initPlaybackServiceWithSettings(mContext);
        }

        @Override
        public void onServiceDisconnected(final ComponentName className) {
            if (mCallback != null) {
                mCallback.onServiceDisconnected(className);
            }
            mMiCoreService = null;
        }
    }
    public static void initPlaybackServiceWithSettings(final Context context) {

    }
    public static final class ServiceToken {
        public ContextWrapper mWrappedContext;

        public ServiceToken(final ContextWrapper context) {
            mWrappedContext = context;
        }
    }
    public static final ServiceToken bindToService(final Context context,
                                                   final ServiceConnection callback) {
        Activity realActivity = ((Activity) context).getParent();
        if (realActivity == null) {
            realActivity = (Activity) context;
        }
        final ContextWrapper contextWrapper = new ContextWrapper(realActivity);
        contextWrapper.startService(new Intent(contextWrapper, MusicService.class));
        final ServiceBinder binder = new ServiceBinder(callback,
                contextWrapper.getApplicationContext());
        if (contextWrapper.bindService(
                new Intent().setClass(contextWrapper, MusicService.class), binder, 0)) {
            mConnectionMap.put(contextWrapper, binder);
            return new ServiceToken(contextWrapper);
        }
        return null;
    }
    public static void unbindFromService(final ServiceToken token) {
        if (token == null) {
            return;
        }
        final ContextWrapper mContextWrapper = token.mWrappedContext;
        final ServiceBinder mBinder = mConnectionMap.remove(mContextWrapper);
        if (mBinder == null) {
            return;
        }
        mContextWrapper.unbindService(mBinder);
        if (mConnectionMap.isEmpty()) {
            mMiCoreService = null;
        }
    }
}
