package com.nvt.mimusic.view.fragment.album;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.nvt.mimusic.R;
import com.nvt.mimusic.adapter.AlbumAdapter;
import com.nvt.mimusic.adapter.AlbumDetailsAdapter;
import com.nvt.mimusic.base.fragment.MiBaseFragment;
import com.nvt.mimusic.constant.Constant;
import com.nvt.mimusic.core.MiApplication;
import com.nvt.mimusic.helper.GridSpacingItemDecoration;
import com.nvt.mimusic.loader.AlbumDataLoader;
import com.nvt.mimusic.loader.AlbumSongLoader;
import com.nvt.mimusic.model.Album;
import com.nvt.mimusic.model.Song;
import com.nvt.mimusic.utils.PreferencesUtility;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Admin on 12/7/17.
 */

public class AlbumDetailsFragment extends MiBaseFragment {
    long albumId = -1;
    Album album ;
    PreferencesUtility mPreferences;
    AlbumDetailsAdapter mAlbumDetailsAdapter;
    List<Song> songList ;
    @BindView(R.id.albumDetailsAlbumArt)
    ImageView albumDetailsAlbumArt;
    @BindView(R.id.albumDetailsRecyclerView)
    RecyclerView albumDetailsRecyclerView;
    @BindView(R.id.albumDetailsTitle)
    TextView albumDetailsTitle;
    @BindView(R.id.albumDetailsInfo)
    TextView albumDetailsInfo;


    @Override
    protected void initData() {

    }

    @Override
    protected void initUI() {

    }

    @Override
    protected int getViewContent() {
        return R.layout.fragment_album_detail;
    }

    @Override
    protected void initControls() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mAppContext);
        songList = new ArrayList<>();
        songList = AlbumSongLoader.getAlbumSongList(mAppContext,albumId);
        mAlbumDetailsAdapter = new AlbumDetailsAdapter(mAppContext,songList,albumId);
        albumDetailsRecyclerView.setLayoutManager(layoutManager);
        albumDetailsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        albumDetailsRecyclerView.setAdapter(mAlbumDetailsAdapter);
        setUpAlbumCover();


    }
    public static AlbumDetailsFragment newInstance(long id, boolean useTransition, String transitionName) {
        AlbumDetailsFragment albumDetailsFragment = new AlbumDetailsFragment();
        Bundle args = new Bundle();
        args.putLong(Constant.ALBUM_ID, id);
        args.putBoolean("transition", useTransition);
        if (useTransition)
            args.putString("transition_name", transitionName);
        albumDetailsFragment.setArguments(args);
        return albumDetailsFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        if (getArguments() != null)
        {
            albumId = getArguments().getLong(Constant.ALBUM_ID);

        }

        mPreferences = PreferencesUtility.getInstance(mAppContext);
        super.onCreate(savedInstanceState);
    }
    public void setUpAlbumCover()
    {
        album = AlbumDataLoader.getAlbum(mAppContext,albumId);
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(mAppContext));
        ImageLoader.getInstance().displayImage(MiApplication.getAlbumUri(albumId).toString(),albumDetailsAlbumArt
                ,new DisplayImageOptions.Builder().cacheInMemory(true)
                        .showImageOnFail(R.drawable.album1)
                        .resetViewBeforeLoading(true)
                        .displayer(new FadeInBitmapDisplayer(400))
                        .build(),new SimpleImageLoadingListener()
                {
                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        super.onLoadingComplete(imageUri, view, loadedImage);

                    }

                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                        super.onLoadingStarted(imageUri, view);
                    }

                    @Override
                    public void onLoadingCancelled(String imageUri, View view) {
                        super.onLoadingCancelled(imageUri, view);
                    }
                });
        albumDetailsTitle.setText(album.getName());
        albumDetailsInfo.setText(album.getArtistName() );

    }
}
