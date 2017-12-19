package com.nvt.mimusic.view.fragment.home;


import android.content.res.Resources;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import com.nvt.mimusic.R;
import com.nvt.mimusic.adapter.AlbumAdapter;
import com.nvt.mimusic.base.fragment.MiBaseFragment;
import com.nvt.mimusic.loader.AlbumLoader;
import com.nvt.mimusic.helper.GridSpacingItemDecoration;
import com.nvt.mimusic.model.Album;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


/**
 * Created by Admin on 10/17/17.
 */

public class AlbumFragment extends MiBaseFragment {
    private List<Album> albumList;
    private AlbumAdapter mAlbumAdapter;
    @BindView(R.id.albumRecycleView)
    RecyclerView albumRecycleView;


    @Override
    protected void initData() {
    }

    @Override
    protected void initUI() {

    }

    @Override
    protected int getViewContent() {
        return R.layout.album_content;
    }

    @Override
    protected void initControls() {
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(mAppContext,2);
        albumList = new ArrayList<>();
        mAlbumAdapter = new AlbumAdapter(AlbumLoader.getAllAlbum(mAppContext),mAppContext);
        albumRecycleView.setLayoutManager(layoutManager);
        albumRecycleView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10 ), true));
        albumRecycleView.setItemAnimator(new DefaultItemAnimator());
        albumRecycleView.setAdapter(mAlbumAdapter);


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
