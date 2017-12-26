package com.nvt.mimusic.view.fragment.home;

import android.content.res.Resources;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;

import com.nvt.mimusic.R;
import com.nvt.mimusic.adapter.TopSongAdapter;
import com.nvt.mimusic.base.fragment.MiBaseFragment;
import com.nvt.mimusic.loader.SongLoader;
import com.nvt.mimusic.helper.GridSpacingItemDecoration;
import com.nvt.mimusic.loader.TopTrackLoader;
import com.nvt.mimusic.model.Song;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Admin on 10/19/17.
 */

public class TopSongFragment extends MiBaseFragment{
    long albumID = -1;
    private List<Song> songList;
    private TopSongAdapter mSongAdapter;
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
        songList = new ArrayList<>();
        TopTrackLoader topTrackLoader = new TopTrackLoader(mAppContext.getApplicationContext(),TopTrackLoader.QuerryType.TOP_TRACK);
        mSongAdapter = new TopSongAdapter(SongLoader.getSongListForCursor(topTrackLoader.getCursor()),mAppContext);
        songRecycleView.setLayoutManager(layoutManager);
        songRecycleView.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(10 ), true));
        songRecycleView.setItemAnimator(new DefaultItemAnimator());
        songRecycleView.setAdapter(mSongAdapter);
        songRecycleView.setNestedScrollingEnabled(false);

    }
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }


    @Override
    public void restartLoader() {

    }

    @Override
    public void onPlaylistChanged() {

    }

    @Override
    public void onMetaChanged() {

    }
}
