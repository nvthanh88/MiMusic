package com.nvt.mimusic.view.fragment.now_playing;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.nvt.mimusic.R;
import com.nvt.mimusic.adapter.AlbumDetailsAdapter;
import com.nvt.mimusic.adapter.NowPlayingSongsAdapter;
import com.nvt.mimusic.base.fragment.MiBaseFragment;
import com.nvt.mimusic.constant.Constant;
import com.nvt.mimusic.loader.AlbumSongLoader;
import com.nvt.mimusic.model.Song;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


/**
 * Created by Admin on 12/11/17.
 */

public class NowPlayingFragment extends MiBaseFragment {

    List<Song> songList;
    NowPlayingSongsAdapter mNowPlayingQueueAdapter;
    @BindView(R.id.recycleViewNowPlayingPlaylist)
    RecyclerView recycleViewNowPlayingPlaylist;
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
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mAppContext);
        songList = new ArrayList<>();
        songList = AlbumSongLoader.getAlbumSongList(mAppContext,5);
        mNowPlayingQueueAdapter = new NowPlayingSongsAdapter(mAppContext,songList);
        recycleViewNowPlayingPlaylist.setLayoutManager(layoutManager);
        recycleViewNowPlayingPlaylist.setItemAnimator(new DefaultItemAnimator());
        recycleViewNowPlayingPlaylist.setAdapter(mNowPlayingQueueAdapter);

    }



}
