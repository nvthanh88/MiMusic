package com.nvt.mimusic.core;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import com.nvt.mimusic.MiCoreService;
import com.nvt.mimusic.core.MiCoreApplication.IdType;
import java.util.Arrays;
import java.util.WeakHashMap;

/**
 * Created by Admin on 10/25/17.
 */

public class MusicCorePlayer {
    private static final long[] sEmptyList;
    private static final WeakHashMap<Context, ServiceBinder> mConnectionMap;
    public static MiCoreService mMiCoreService = null;

    static {
        mConnectionMap = new WeakHashMap<Context, ServiceBinder>();
        sEmptyList = new long[0];
    }

    public static void playAll(long id , Context context)  {
        if (mMiCoreService != null) {
            try {
                mMiCoreService.play(id);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        else {
            Toast.makeText(context, " Core Service Null", Toast.LENGTH_LONG).show();
        }

    }

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
}
