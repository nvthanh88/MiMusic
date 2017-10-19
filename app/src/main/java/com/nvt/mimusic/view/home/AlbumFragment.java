package com.nvt.mimusic.view.home;


import android.content.res.Resources;
import android.os.AsyncTask;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import com.nvt.mimusic.R;
import com.nvt.mimusic.adapter.AlbumAdapter;
import com.nvt.mimusic.base.fragment.MiBaseFragment;
import com.nvt.mimusic.core.AlbumDataLoader;
import com.nvt.mimusic.helper.GridSpacingItemDecoration;
import com.nvt.mimusic.model.AlbumModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


/**
 * Created by Admin on 10/17/17.
 */

public class AlbumFragment extends MiBaseFragment {
    private List<AlbumModel> albumModelList ;
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
        albumModelList = new ArrayList<>();
        mAlbumAdapter = new AlbumAdapter(AlbumDataLoader.getAllAlbum(mAppContext),mAppContext);
        albumRecycleView.setLayoutManager(layoutManager);
        albumRecycleView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10 ), true));
        albumRecycleView.setItemAnimator(new DefaultItemAnimator());
        albumRecycleView.setAdapter(mAlbumAdapter);


    }
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }


}
