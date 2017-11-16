package com.nvt.mimusic.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.nvt.mimusic.core.MusicService;
import com.nvt.mimusic.view.activity.MiBaseActivity;

import java.lang.ref.WeakReference;

/**
 * Created by Admin on 11/16/17.
 *
 */

public class PlayBackStatus extends BroadcastReceiver {
    private final WeakReference<MiBaseActivity> baseActivityWeakReference;

    public PlayBackStatus(final MiBaseActivity miBaseActivity) {
        baseActivityWeakReference = new WeakReference<>(miBaseActivity);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();
        final MiBaseActivity baseActivity = baseActivityWeakReference.get();
        if (baseActivity != null)
        {
            if (action.equals(MusicService.META_CHANGED))
                baseActivity.onMetaChanged();
            if (action.equals(MusicService.PLAYLIST_CHANGED))
                baseActivity.onPlaylistChanged();
            if(action.equals(MusicService.REFRESH))
                baseActivity.restartLoader();
        }
    }
}
