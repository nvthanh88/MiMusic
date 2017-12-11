package com.nvt.mimusic.view.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nvt.mimusic.R;
import com.nvt.mimusic.base.fragment.MiBaseFragment;
import com.nvt.mimusic.constant.ScreenIDs;
import com.nvt.mimusic.core.MiApplication;
import com.nvt.mimusic.core.MusicPlayer;
import com.nvt.mimusic.listener.MediaStateListener;
import com.nvt.mimusic.utils.NavigationUtils;
import com.nvt.mimusic.utils.PermissionCallback;
import com.nvt.mimusic.utils.PermissionRequest;

import com.nvt.mimusic.view.fragment.home.AlbumFragment;
import com.nvt.mimusic.view.fragment.home.SongFragment;
import com.nvt.mimusic.view.fragment.now_playing.NowPlayingFragment;
import com.nvt.mimusic.wiget.PlayPauseButton;

import java.util.logging.Handler;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Admin on 11/16/17.
 */

public class MiMainActivity extends MiBaseActivity implements MediaStateListener{
    private final String TAG = getClass().getSimpleName();
    private Activity context;
    private ScreenIDs.ID mCurrentTab;
    private MiBaseFragment mCurrentFragment;
    boolean duetoplaypause;
    int overflowCounter = 0;
    @BindView(R.id.toolbarTop)
    Toolbar toolbarTop;
    @BindView(R.id.btnPlayPause)
    PlayPauseButton mPlayPauseButton;
    @BindView(R.id.qcSongTitle)
    TextView qcSongTitle;
    @BindView(R.id.qcArtist)
    TextView qcArtist;
    @BindView(R.id.qcAlbumArt)
    ImageView qcAlbumArt;
    @BindView(R.id.qcSongProgressbar)
    ProgressBar qcSongProgressbar;
    @BindView(R.id.quickPlayingControl)
    RelativeLayout quickPlayingControl;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbarTop);
        PermissionRequest.init(getApplicationContext());
        /**
         * Check OS Version > 6 ask for permission and else Todo smt
         * */
        if (MiApplication.isMarshmallow())
        {
            checkPermissionAndThenLoad();
        }else {
            gotoHomeFragment.run();
        }
        /**
         * Setup palnel
         *
         * */






    }


    /**
     * Check permission and load
     * */
    private void checkPermissionAndThenLoad() {
        //check for permission
        if (PermissionRequest.checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            gotoHomeFragment.run();
        } else {
            if (PermissionRequest.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                /*Snackbar.make(panelLayout, "MiMusic Need READ_EXTERNAL_STORAGE Permission to load  media", Snackbar.LENGTH_LONG)
                        .setAction("OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                PermissionRequest.askForPermission(MiMainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE, permissionReadStorageCallback);
                            }
                        }).show();*/
            } else {
                PermissionRequest.askForPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE, permissionReadStorageCallback);
            }
        }
    }
    final PermissionCallback permissionReadStorageCallback = new PermissionCallback() {
        @Override
        public void permissionGranted() {
            gotoHomeFragment.run();

        }

        @Override
        public void permissionRefused() {
            finish();
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionRequest.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    /**
     *Open Fragment
     * */
    Runnable gotoHomeFragment = new Runnable() {
        @Override
        public void run() {
            openScreenBackgroundTask(ScreenIDs.ID.HOME,AlbumFragment.class,R.id.frameAlbumContent,null,false);
            openScreenBackgroundTask(ScreenIDs.ID.HOME,SongFragment.class,R.id.frameSongContent,null,false);

        }
    };
    private void openScreenBackgroundTask(ScreenIDs.ID tab, final Class<? extends MiBaseFragment> fragmentClass,int contentFrame, Bundle bundles, boolean addToBackStack) {

        if (getBaseContext() == null) return;
        mCurrentTab = tab;
        FragmentManager fragmentManager = getFragmentManager();
        String tag = fragmentClass.getName();
        try {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            Log.e(TAG, "openScreen: fragment " + fragmentClass.getSimpleName() + " is NOT from back stack");
            mCurrentFragment = fragmentClass.newInstance();
            mCurrentFragment.setRetainInstance(true);
            if (bundles == null) bundles = new Bundle();
            mCurrentFragment.setArguments(bundles);
            if (addToBackStack) {
                fragmentTransaction.addToBackStack(tag);
                Log.e(TAG, "openScreen: add " + tag + " to back stack");
            }
            fragmentTransaction.replace(contentFrame, mCurrentFragment, tag);
            fragmentTransaction.commitAllowingStateLoss();
            fragmentManager.executePendingTransactions();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    @Override
    public void onMetaChanged() {
        updateState();
        updateNowPlayingCard();
    }

    /**
     * Update now playing card
     * */
    public void updateState(){
        if(MusicPlayer.isPlaying()) {
            if (!mPlayPauseButton.isPlayed()) {
                mPlayPauseButton.setPlayed(true);
                mPlayPauseButton.setColor(R.color.colorAccent);
                mPlayPauseButton.startAnimation();
            }

            }else {
            mPlayPauseButton.setPlayed(false);
            mPlayPauseButton.setColor(R.color.colorAccent);
            mPlayPauseButton.startAnimation();
        }
    }
    public void updateNowPlayingCard(){
        Log.i(TAG, "updateNowPlayingCard: "+ MusicPlayer.getArtistName() + MusicPlayer.getTrackName());
        qcSongTitle.setText(MusicPlayer.getTrackName());
        qcArtist.setText(MusicPlayer.getArtistName());
        if (!duetoplaypause) {
            ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(getApplicationContext()));
            ImageLoader.getInstance().displayImage(MiApplication.getAlbumUri(MusicPlayer.getCurrentAlbumId()).toString(), qcAlbumArt,
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
        }
        duetoplaypause = false;
        qcSongProgressbar.setMax((int) MusicPlayer.duration());
        qcSongProgressbar.postDelayed(mUpdateProgress, 10);
    }
    /**
     *
     * Update Progressbar when playing music
     * */
    public Runnable mUpdateProgress = new Runnable() {
        @Override
        public void run() {
            long position = MusicPlayer.position();
            qcSongProgressbar.setProgress((int) position);
            overflowCounter --;
            if (MusicPlayer.isPlaying());
            {
                int delay = (int) (1500 - (position % 1000));
                if (overflowCounter < 0 ) {
                    overflowCounter++;
                    qcSongProgressbar.postDelayed(mUpdateProgress, delay);
                } else {
                    qcSongProgressbar.removeCallbacks(this);
                }
            }

        }
    };


    @Override
    protected void onResume() {
        super.onResume();
    }
    @OnClick(R.id.quickPlayingControl)
        public void openNowPlaying() {
        openScreenBackgroundTask(ScreenIDs.ID.HOME, NowPlayingFragment.class, R.id.frameMainContent, null, false);
        isShowQuickControl(false);
    }


    public void isShowQuickControl(boolean isShow)
    {
        quickPlayingControl.setVisibility(isShow ? View.VISIBLE:View.GONE);
    }

}
