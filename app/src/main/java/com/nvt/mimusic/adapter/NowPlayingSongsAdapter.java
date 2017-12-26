package com.nvt.mimusic.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.nvt.mimusic.R;
import com.nvt.mimusic.core.MiApplication;
import com.nvt.mimusic.core.MusicPlayer;
import com.nvt.mimusic.model.Song;
import com.nvt.mimusic.wiget.MusicVisualizer;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Admin on 12/12/17.
 */

public class NowPlayingSongsAdapter extends RecyclerView.Adapter<NowPlayingSongsAdapter.ViewHolder>{
    Activity mAppContext;
    List<Song> songList;
    private int currentPosition;

    public NowPlayingSongsAdapter(Activity mAppContext, List<Song> songList) {
        this.mAppContext = mAppContext;
        this.songList = songList;
        this.currentPosition = MusicPlayer.getQueuePosition();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_playing_queue,parent,false);
        return new ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Song queueSong = songList.get(position);
        holder.queuePlayingTxtArtistName.setText(queueSong.getArtistName());
        holder.queuePlayingTxtSongTile.setText(queueSong.getName());
        ImageLoader.getInstance().displayImage(MiApplication.getAlbumUri(queueSong.getAlbumId()).toString(),holder.queuePlayingImgSongThumbnail
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

            holder.musicVisualizer.setColor(R.color.cmn_accent);
            if (MusicPlayer.isPlaying())
            holder.musicVisualizer.setVisibility(MusicPlayer.getCurrentAudioId() == queueSong.getSongId() ? View.VISIBLE : View.GONE);
            else
                holder.musicVisualizer.setVisibility(View.GONE);

    }

    @Override
    public int getItemCount() {
        return songList.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.queuePlayingImgSongThumbnail)
        ImageView queuePlayingImgSongThumbnail;
        @BindView(R.id.queuePlayingTxtArtistName)
        TextView queuePlayingTxtArtistName;
        @BindView(R.id.queuePlayingTxtSongTile)
        TextView queuePlayingTxtSongTile;
        @BindView(R.id.musicVisualizer)
        MusicVisualizer musicVisualizer;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
