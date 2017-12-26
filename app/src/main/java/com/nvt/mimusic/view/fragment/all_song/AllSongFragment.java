package com.nvt.mimusic.view.fragment.all_song;

import android.content.res.Resources;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.widget.LinearLayout;

import com.nvt.mimusic.R;
import com.nvt.mimusic.adapter.SongAdapter;
import com.nvt.mimusic.adapter.TopSongAdapter;
import com.nvt.mimusic.base.fragment.MiBaseFragment;
import com.nvt.mimusic.helper.GridSpacingItemDecoration;
import com.nvt.mimusic.loader.SongLoader;
import com.nvt.mimusic.loader.TopTrackLoader;
import com.nvt.mimusic.model.Song;
import com.nvt.mimusic.wiget.FastScroller;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Admin on 12/20/17.
 */

public class AllSongFragment extends MiBaseFragment {
    @BindView(R.id.allSongRecycleView)
    RecyclerView allSongRecycleView;
    List<Song> songList ;
    SongAdapter songAdapter;
    @Override
    public void restartLoader() {

    }

    @Override
    public void onPlaylistChanged() {

    }

    @Override
    public void onMetaChanged() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initUI() {

    }

    @Override
    protected int getViewContent() {
        return R.layout.fragment_song;
    }

    @Override
    protected void initControls() {
        allSongRecycleView.setLayoutManager(new LinearLayoutManager(mAppContext));
        new loadSongs().execute();
    }
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
    private class loadSongs extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            if (getActivity() != null)
                songList = new ArrayList<>();
                songAdapter = new SongAdapter(mAppContext,SongLoader.getAllSongs(mAppContext));
            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {
            if (mActivity != null) {
                allSongRecycleView.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(10), true));
                allSongRecycleView.setItemAnimator(new DefaultItemAnimator());
                allSongRecycleView.setAdapter(songAdapter);
                allSongRecycleView.setNestedScrollingEnabled(false);
            }

        }

        @Override
        protected void onPreExecute() {
        }
    }
}
