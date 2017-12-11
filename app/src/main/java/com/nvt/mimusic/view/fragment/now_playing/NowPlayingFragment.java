package com.nvt.mimusic.view.fragment.now_playing;

import android.app.Fragment;
import android.os.Bundle;

import com.nvt.mimusic.R;
import com.nvt.mimusic.base.fragment.MiBaseFragment;
import com.nvt.mimusic.constant.Constant;
import com.nvt.mimusic.view.fragment.album.AlbumDetailsFragment;

/**
 * Created by Admin on 12/11/17.
 */

public class NowPlayingFragment extends MiBaseFragment {
    static boolean useTransition;
    static String transitionName;
    @Override
    protected void initData() {

    }

    @Override
    protected void initUI() {

    }

    @Override
    protected int getViewContent() {
        return R.layout.fragment_now_playing;
    }

    @Override
    protected void initControls() {

    }

    public static Fragment newInstance(long songId, boolean b, String second) {
        NowPlayingFragment nowPlayingFragment = new NowPlayingFragment();
        Bundle args = new Bundle();
        args.putLong(Constant.ALBUM_ID, songId);
        args.putBoolean("transition", useTransition);

        if (useTransition)
            args.putString("transition_name", transitionName);
        nowPlayingFragment.setArguments(args);
        return nowPlayingFragment;

    }
}
