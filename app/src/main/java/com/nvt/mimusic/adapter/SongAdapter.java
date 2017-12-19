package com.nvt.mimusic.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Handler;
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
import com.nvt.mimusic.wiget.CircleImageView;

import org.w3c.dom.Text;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Admin on 10/19/17.
 */

public class SongAdapter  extends RecyclerView.Adapter<SongAdapter.ViewHolder>{
    List<Song> songList;
    Activity mAppContext;
    private long albumID;
    private long[] songId;



    public SongAdapter(List<Song> songList, Activity mAppContext) {
        this.songList = songList;
        this.mAppContext = mAppContext;
        this.albumID = albumID;
        this.songId=getSongId();

    }

    private long[] getSongId() {
        long[] ret = new long[getItemCount()];
        for (int i = 0; i < getItemCount(); i++) {
            ret[i] = songList.get(i).getSongId();
        }

        return ret;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_song,parent,false);

        return new ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Song songItem = songList.get(position);
        holder.txtSongTile.setText(songItem.getName());
        holder.txtArtistName.setText(songItem.getArtistName());
        holder.songPosition.setText(String.valueOf(position + 1));
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(mAppContext));
        ImageLoader.getInstance().displayImage(MiApplication.getAlbumUri(songItem.getAlbumId()).toString(),holder.imgSongThumbnail
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
        holder.songImgOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }

        });

    }

    @Override
    public int getItemCount() {
        return songList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.txtSongTile)
        TextView txtSongTile;
        @BindView(R.id.txtArtistName)
        TextView txtArtistName;
        @BindView(R.id.imgSongThumbnail)
        CircleImageView imgSongThumbnail;
        @BindView(R.id.songImgOptions)
        ImageView songImgOptions;
        @BindView(R.id.songPosition)
        TextView songPosition;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            MusicPlayer.playAll(mAppContext,songId,getAdapterPosition(),-1, MiApplication.IdType.NA,false);
                        }
                    }, 100);

                }
            });

        }
    }
}
