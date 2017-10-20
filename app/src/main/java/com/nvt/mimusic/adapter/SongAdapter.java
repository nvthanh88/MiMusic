package com.nvt.mimusic.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
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
import com.nvt.mimusic.core.MiCoreApplication;
import com.nvt.mimusic.model.SongModel;
import com.nvt.mimusic.wiget.CircleImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Admin on 10/19/17.
 */

public class SongAdapter  extends RecyclerView.Adapter<SongAdapter.ViewHolder>{
    List<SongModel> songModelList ;
    Context mAppContext;

    public SongAdapter(List<SongModel> songModelList, Context mAppContext) {
        this.songModelList = songModelList;
        this.mAppContext = mAppContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_song,parent,false);

        return new ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        SongModel songModelItem = songModelList.get(position);
        holder.txtSongTile.setText(songModelItem.getName());
        holder.txtArtistName.setText(songModelItem.getArtistName());

        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(mAppContext));
        ImageLoader.getInstance().displayImage(MiCoreApplication.getAlbumUri(songModelItem.getAlbumId()).toString(),holder.imgSongThumbnail
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

    }

    @Override
    public int getItemCount() {
        return songModelList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.txtSongTile)
        TextView txtSongTile;
        @BindView(R.id.txtArtistName)
        TextView txtArtistName;
        @BindView(R.id.imgSongThumbnail)
        CircleImageView imgSongThumbnail;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

        }
    }
}
