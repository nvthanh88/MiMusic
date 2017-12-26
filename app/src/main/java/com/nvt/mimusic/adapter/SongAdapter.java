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
import com.nvt.mimusic.model.Song;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Admin on 12/20/17.
 */

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.ViewHolder>{
    Activity context;
    List<Song> songList;

    public SongAdapter(Activity context, List<Song> songList) {
        this.context = context;
        this.songList = songList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_song,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Song songItem = songList.get(position);
        holder.songTitle.setText(songItem.getName());
        holder.artistName.setText(songItem.getArtistName());
        holder.albumName.setText(songItem.getAlbumName());
        ImageLoader.getInstance().displayImage(MiApplication.getAlbumUri(songItem.getAlbumId()).toString(),holder.songImg
                ,new DisplayImageOptions.Builder().cacheInMemory(true)
                        .showImageOnFail(R.drawable.album1)
                        .resetViewBeforeLoading(true)
                        .build()
                );
    }

    @Override
    public int getItemCount() {
        return songList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.allSong_SongTile)
        TextView songTitle;
        @BindView(R.id.allSong_ArtistName)
        TextView artistName;
        @BindView(R.id.allSongImgThumbnail)
        ImageView songImg;
        @BindView(R.id.allSong_AlbumName)
        TextView albumName;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
