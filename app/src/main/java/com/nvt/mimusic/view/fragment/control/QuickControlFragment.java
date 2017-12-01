package com.nvt.mimusic.view.fragment.control;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nvt.mimusic.R;
import com.nvt.mimusic.core.MiApplication;
import com.nvt.mimusic.core.MusicPlayer;
import com.nvt.mimusic.listener.MediaStateListener;
import com.nvt.mimusic.utils.ImageUtils;
import com.nvt.mimusic.view.activity.MiBaseActivity;
import com.nvt.mimusic.wiget.PlayPauseButton;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Admin on 11/22/17.
 */

public class QuickControlFragment extends Fragment implements MediaStateListener{
    @BindView(R.id.songProgressbar)
    ProgressBar songProgressbar;
    @BindView(R.id.btnPlayPause)
    PlayPauseButton btnPlayPause;
    @BindView(R.id.qcAlbumArt)
    ImageView qcAlbumArt;
    @BindView(R.id.qcSongTitle)
    TextView qcSongTitle;
    @BindView(R.id.qcArtist)
    TextView qcArtist;


    int overflowCounter = 0;
    private boolean fragmentPaused = false;
    private View mView;
    boolean duetoplaypause;



    /**
     *
     * Update Progressbar when playing music*/
    public Runnable mUpdateProgress = new Runnable() {
        @Override
        public void run() {
            long position = MusicPlayer.position();
            songProgressbar.setProgress((int) position);
            overflowCounter --;
            if (MusicPlayer.isPlaying());
            {
                int delay = (int) (1500 - (position % 1000));
                if (overflowCounter < 0 && !fragmentPaused) {
                    overflowCounter++;
                    songProgressbar.postDelayed(mUpdateProgress, delay);
                } else {
                    songProgressbar.removeCallbacks(this);
                }
            }

        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.bottom_nowplaying_card,container,false);
        this.mView =mView;
        ButterKnife.bind(this,mView);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) songProgressbar.getLayoutParams();
        songProgressbar.measure(0, 0);
        layoutParams.setMargins(0, -(songProgressbar.getMeasuredHeight() / 2), 0, 0);
        songProgressbar.setLayoutParams(layoutParams);
        btnPlayPause.setColor(R.color.cmn_accent);

        ((MiBaseActivity) getActivity()).setMediaStateListenerListener(this);


        return mView;
    }



    @Override
    public void onPause() {
        super.onPause();
        fragmentPaused = true;
    }

    @Override
    public void restartLoader() {

    }

    @Override
    public void onPlaylistChanged() {

    }

    @Override
    public void onMetaChanged() {
        updateNowPlayingCard();
        updateMediaState();
    }

    /**
     * Update Playing card on load
     * */
    public void updateNowPlayingCard(){
        qcSongTitle.setText(MusicPlayer.getTrackName());
        qcArtist.setText(MusicPlayer.getArtistName());
        if (!duetoplaypause) {
            ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(getContext()));
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
                            if (getActivity() != null)
                                new ExecuteAlbumArt().execute(failedBitmap);
                        }

                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                            if (getActivity() != null)
                                new ExecuteAlbumArt().execute(loadedImage);

                        }

                        @Override
                        public void onLoadingCancelled(String imageUri, View view) {

                        }
                    });
        }
        duetoplaypause = false;
        songProgressbar.setMax((int) MusicPlayer.duration());
        songProgressbar.postDelayed(mUpdateProgress, 10);

    }
    /**
     * Update Media State
     * */
    public void updateMediaState(){
        if (MusicPlayer.isPlaying()) {
            if (!btnPlayPause.isPlayed()) {
                btnPlayPause.setPlayed(true);
                btnPlayPause.startAnimation();
            }

        } else {
            if (btnPlayPause.isPlayed()) {
                btnPlayPause.setPlayed(false);
                btnPlayPause.startAnimation();
            }

        }

    }

    /**
     * Execute Album Art
     *  */
    private class ExecuteAlbumArt extends AsyncTask<Bitmap , Void ,Drawable>
    {

        @Override
        protected Drawable doInBackground(Bitmap... bitmaps) {
            Drawable drawable = null;
            drawable = ImageUtils.createBlurredImageFromBitmap(bitmaps[0],getActivity(),6);
            return drawable;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Drawable drawable) {
            /*if (drawable != null)
            {
                if (blurredAlbumArt.getDrawable() != null) {
                    final TransitionDrawable td =
                            new TransitionDrawable(new Drawable[]{
                                    blurredAlbumArt.getDrawable(),
                                    drawable
                            });
                    blurredAlbumArt.setImageDrawable(td);
                    td.startTransition(400);

                } else {
                    blurredAlbumArt.setImageDrawable(drawable);
                }
            }*/

        }
    }
}
