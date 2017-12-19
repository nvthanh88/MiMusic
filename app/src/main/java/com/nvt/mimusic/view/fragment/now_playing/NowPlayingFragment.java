package com.nvt.mimusic.view.fragment.now_playing;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nvt.mimusic.R;
import com.nvt.mimusic.adapter.AlbumDetailsAdapter;
import com.nvt.mimusic.adapter.NowPlayingSongsAdapter;
import com.nvt.mimusic.base.fragment.MiBaseFragment;
import com.nvt.mimusic.constant.Constant;
import com.nvt.mimusic.core.MiApplication;
import com.nvt.mimusic.core.MusicPlayer;
import com.nvt.mimusic.listener.MediaStateListener;
import com.nvt.mimusic.loader.AlbumSongLoader;
import com.nvt.mimusic.loader.NowPlayingSongLoader;
import com.nvt.mimusic.model.Song;
import com.nvt.mimusic.utils.ImageUtils;
import com.nvt.mimusic.view.activity.MiBaseActivity;
import com.nvt.mimusic.wiget.CircularSeekBar;
import com.nvt.mimusic.wiget.PlayPauseDrawable;

import net.steamcrafted.materialiconlib.MaterialIconView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindFloat;
import butterknife.BindView;
import butterknife.OnClick;


/**
 * Created by Admin on 12/11/17.
 */

public class NowPlayingFragment extends MiBaseFragment {

    List<Song> songList;
    NowPlayingSongsAdapter mNowPlayingQueueAdapter;
    @BindView(R.id.recycleViewNowPlayingPlaylist)
    RecyclerView recycleViewNowPlayingPlaylist;
    @BindView(R.id.nowPlayingAlbumCover)
    ImageView nowPlayingAlbumCover;
    @BindView(R.id.nowPlayingSongName)
    TextView nowPlayingSongName;
    @BindView(R.id.nowPlayingArtistName)
    TextView nowPlayingArtistName;
    @BindView(R.id.song_progress_circular)
    CircularSeekBar songProgressCircular;
    @BindView(R.id.playFloatingBtn)
    FloatingActionButton playFloatingBtn;
    @BindView(R.id.nowPlayingBackground)
    ImageView nowPlayingBackground;
    @BindView(R.id.btnPrev)
    MaterialIconView btnPrev;
    @BindView(R.id.btnNext)
    MaterialIconView btnNext;
    @BindView(R.id.btnRepeat)
    MaterialIconView btnRepeat;
    @BindView(R.id.btnShuffle)
    MaterialIconView btnShuffle;
    PlayPauseDrawable playPauseDrawable = new PlayPauseDrawable();
    int overflowcounter = 0;
    boolean fragmentPaused = false;

    @Override
    protected void initData() {
        fragmentPaused = false;
        updateNowPlayingScreen();
    }

    @Override
    protected void initUI() {
        playFloatingBtn.setImageDrawable(playPauseDrawable);
        setDefaultMaterialIconColor(R.color.colorPrimary);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected int getViewContent() {
        return R.layout.fragment_now_playing;
    }

    @Override
    protected void initControls() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mAppContext);
        songList = new ArrayList<>();
        songList = NowPlayingSongLoader.getQueueSongs(mAppContext);
        mNowPlayingQueueAdapter = new NowPlayingSongsAdapter(mAppContext,songList);
        recycleViewNowPlayingPlaylist.setLayoutManager(layoutManager);
        recycleViewNowPlayingPlaylist.setItemAnimator(new DefaultItemAnimator());
        recycleViewNowPlayingPlaylist.setAdapter(mNowPlayingQueueAdapter);

        seekOnCircularBar();
        setMusicStateListener();
    }
    public void updateNowPlayingScreen(){
        songProgressCircular.setMax((int)MusicPlayer.duration());
        nowPlayingSongName.setText(MusicPlayer.getTrackName());
        nowPlayingArtistName.setText(MusicPlayer.getArtistName());
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(mAppContext));
        ImageLoader.getInstance().displayImage(MiApplication.getAlbumUri(MusicPlayer.getCurrentAlbumId()).toString(), nowPlayingAlbumCover,
                new DisplayImageOptions.Builder().cacheInMemory(true)
                        .showImageOnFail(R.drawable.album1)
                        .resetViewBeforeLoading(true)
                        .build(), new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {

                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        Bitmap failedBitmap = ImageLoader.getInstance().loadImageSync("drawable://" + R.drawable.album1);

                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {


                    }

                    @Override
                    public void onLoadingCancelled(String imageUri, View view) {

                    }
                });

        if (mActivity != null)
        mUpdateCircularProgress.run();
        updateMediaState();

    }
    public Runnable mUpdateCircularProgress = new Runnable() {

        @Override
        public void run() {
            long position = MusicPlayer.position();
            if (songProgressCircular != null) {
                songProgressCircular.setProgress((int) position);
            }
            overflowcounter--;
            if (MusicPlayer.isPlaying()) {
                int delay = (int) (1500 - (position % 1000));
                if (overflowcounter < 0 && !fragmentPaused) {
                    overflowcounter++;
                    songProgressCircular.postDelayed(mUpdateCircularProgress, delay);
                }
            }

        }
    };

    @Override
    public void onPause() {

        super.onPause();
        fragmentPaused = true;
    }

    public void seekOnCircularBar()
    {
        if (songProgressCircular != null){
            songProgressCircular.setOnSeekBarChangeListener(new CircularSeekBar.OnCircularSeekBarChangeListener() {
                @Override
                public void onProgressChanged(CircularSeekBar circularSeekBar, int progress, boolean fromUser) {
                    if (fromUser) MusicPlayer.seek(progress);

                }

                @Override
                public void onStopTrackingTouch(CircularSeekBar seekBar) {

                }

                @Override
                public void onStartTrackingTouch(CircularSeekBar seekBar) {

                }
            });
        }

    }

    public void setMusicStateListener() {
        ((MiBaseActivity) getActivity()).setMediaStateListenerListener(this);
    }

    @Override
    public void restartLoader() {

    }

    @Override
    public void onPlaylistChanged() {

    }

    @Override
    public void onMetaChanged() {
        updateNowPlayingScreen();
        mNowPlayingQueueAdapter.notifyDataSetChanged();
    }

    public void updateMediaState(){
        if (playFloatingBtn != null) {
            if (MusicPlayer.isPlaying()) {
                playPauseDrawable.transformToPause(false);
            } else playPauseDrawable.transformToPlay(false);
        }
    }
    @OnClick(R.id.playFloatingBtn)
    public void changeMediaState(){
            MusicPlayer.playOrPause();
    }
    @OnClick(R.id.btnNext)
    public void gotoNext(){
        MusicPlayer.next();
    }
    @OnClick(R.id.btnPrev)
    public void backtoPrev(){
        MusicPlayer.prev();

    }


    public void setDefaultMaterialIconColor(int color){
        int defaultColor = getResources().getColor(color);
        btnPrev.setColor(defaultColor);
        btnNext.setColor(defaultColor);
        btnRepeat.setColor(defaultColor);
        btnShuffle.setColor(defaultColor);
    }
}
