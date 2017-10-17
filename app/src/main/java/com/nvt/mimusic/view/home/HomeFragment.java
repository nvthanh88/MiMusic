package com.nvt.mimusic.view.home;

import android.content.res.Resources;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;

import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;

import com.nvt.mimusic.R;
import com.nvt.mimusic.adapter.AlbumAdapter;
import com.nvt.mimusic.base.fragment.MiBaseFragment;
import com.nvt.mimusic.helper.GridSpacingItemDecoration;
import com.nvt.mimusic.model.AlbumModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Admin on 10/17/17.
 */

public class HomeFragment extends MiBaseFragment {
    private List<AlbumModel> albumModelList ;
    private AlbumAdapter mAlbumAdapter;
    @BindView(R.id.albumRecycleView)
    RecyclerView albumRecycleView;


    @Override
    protected void initData() {
        prepareAlbum();
    }

    private void prepareAlbum() {
        int[] covers = new int[]{
                R.drawable.album1,
                R.drawable.album2,
                R.drawable.album3,
                R.drawable.album4,
                R.drawable.album5,
                R.drawable.album6,
                R.drawable.album7,
                R.drawable.album8,
                R.drawable.album9,
                R.drawable.album10,
                R.drawable.album11};

        AlbumModel a = new AlbumModel("True Romance", 13, covers[0]);
        albumModelList.add(a);

        a = new AlbumModel("Xscpae", 8, covers[1]);
        albumModelList.add(a);

        a = new AlbumModel("Maroon 5", 11, covers[2]);
        albumModelList.add(a);

        a = new AlbumModel("Born to Die", 12, covers[3]);
        albumModelList.add(a);

        a = new AlbumModel("Honeymoon", 14, covers[4]);
        albumModelList.add(a);

        a = new AlbumModel("I Need a Doctor", 1, covers[5]);
        albumModelList.add(a);

        a = new AlbumModel("Loud", 11, covers[6]);
        albumModelList.add(a);

        a = new AlbumModel("Legend", 14, covers[7]);
        albumModelList.add(a);

        a = new AlbumModel("Hello", 11, covers[8]);
        albumModelList.add(a);

        a = new AlbumModel("Greatest Hits", 17, covers[9]);
        albumModelList.add(a);

        mAlbumAdapter.notifyDataSetChanged();
    }

    @Override
    protected void initUI() {

    }

    @Override
    protected int getViewContent() {
        return R.layout.home_content;
    }

    @Override
    protected void initControls() {
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(mAppContext,2);
        albumModelList = new ArrayList<>();
        mAlbumAdapter = new AlbumAdapter(albumModelList,mAppContext);
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
