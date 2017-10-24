package com.nvt.mimusic.adapter;

import android.content.ContentUris;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.nvt.mimusic.core.MiCoreApplication;

import com.nvt.mimusic.model.AlbumModel;

import java.io.IOException;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.nvt.mimusic.R;
/**
 * Created by Admin on 10/16/17.
 */

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder>{

    List<AlbumModel> albumModelList ;
    Context mAppContext;

    public AlbumAdapter(List<AlbumModel> albumModelList, Context mAppContext) {
        this.albumModelList = albumModelList;
        this.mAppContext = mAppContext;

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_album,parent,false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final AlbumModel albumModelItem = albumModelList.get(position);
        holder.txtAlbumTile.setText(albumModelItem.getName());
        holder.txtAlbumSongs.setText(String.valueOf(albumModelItem.getNumberOfSongs()) + " songs");

        //Set album thumbnail
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(mAppContext));
        ImageLoader.getInstance().displayImage(MiCoreApplication.getAlbumUri(albumModelItem.getAlbumId()).toString(),holder.imgAlbumThumbnail
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

        holder.imgOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long id = albumModelItem.getAlbumId();
                Uri contentUri = ContentUris.withAppendedId(
                        android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,id);

                MediaPlayer mMediaPlayer = new MediaPlayer();
                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                try {
                    mMediaPlayer.setDataSource(mAppContext, contentUri);
                    mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    mMediaPlayer.prepare();
                    mMediaPlayer.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }




    @Override
    public int getItemCount() {
        return albumModelList.size();
    }

     class ViewHolder extends RecyclerView.ViewHolder{
         @BindView(R.id.txtAlbumTile)
         TextView txtAlbumTile;
         @BindView(R.id.txtAlbumSongs)
         TextView txtAlbumSongs;
         @BindView(R.id.imgAlbumThumbnail)
         ImageView imgAlbumThumbnail;
         @BindView(R.id.imgOptions)
         ImageView imgOptions;

         ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);


        }
    }


}
