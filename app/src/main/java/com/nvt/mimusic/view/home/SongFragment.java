package com.nvt.mimusic.view.home;

import android.content.res.Resources;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;

import com.nvt.mimusic.R;
import com.nvt.mimusic.adapter.AlbumAdapter;
import com.nvt.mimusic.adapter.SongAdapter;
import com.nvt.mimusic.base.fragment.MiBaseFragment;
import com.nvt.mimusic.core.AlbumDataLoader;
import com.nvt.mimusic.core.SongDataLoader;
import com.nvt.mimusic.helper.GridSpacingItemDecoration;
import com.nvt.mimusic.model.SongModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Admin on 10/19/17.
 */

public class SongFragment  extends MiBaseFragment{
    private List<SongModel> songModelList;
    private SongAdapter mSongAdapter;
    @BindView(R.id.songRecycleView)
    RecyclerView songRecycleView;
    @Override
    protected void initData() {

    }

    @Override
    protected void initUI() {

    }

    @Override
    protected int getViewContent() {
        return R.layout.song_content;
    }

    @Override
    protected void initControls() {
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(mAppContext,1);
        songModelList = new ArrayList<>();
        mSongAdapter = new SongAdapter(SongDataLoader.getAllSongs(mAppContext),mAppContext);
        songRecycleView.setLayoutManager(layoutManager);
        songRecycleView.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(10 ), true));
        songRecycleView.setItemAnimator(new DefaultItemAnimator());
        songRecycleView.setAdapter(mSongAdapter);

    }
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}