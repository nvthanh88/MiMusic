package com.nvt.mimusic.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
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
import com.nvt.mimusic.constant.ScreenIDs;
import com.nvt.mimusic.core.MiApplication;

import com.nvt.mimusic.model.Album;

import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.nvt.mimusic.R;
import com.nvt.mimusic.utils.NavigationUtils;

/**
 * Created by Admin on 10/16/17.
 */

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder>{

    List<Album> albumList;
    Activity mAppContext;

    public AlbumAdapter(List<Album> albumList, Activity mAppContext) {
        this.albumList = albumList;
        this.mAppContext = mAppContext;

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_album,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Album albumItem = albumList.get(position);
        holder.txtAlbumTile.setText(albumItem.getName());
        holder.txtAlbumSongs.setText(String.valueOf(albumItem.getNumberOfSongs()) + " songs");
        //Set album thumbnail
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(mAppContext));
        ImageLoader.getInstance().displayImage(MiApplication.getAlbumUri(albumItem.getAlbumId()).toString(),holder.imgAlbumThumbnail
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

        holder.imgAlbumThumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.imgAlbumThumbnail.setTransitionName("transition_album_art" + position);
                NavigationUtils.navigateToAlbum(ScreenIDs.ID.SEARCH_TAB,mAppContext,albumItem.getAlbumId(),new Pair<View, String>((View)holder.imgAlbumThumbnail, "transition_album_art" + position));


            }
        });
    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }

     class ViewHolder extends RecyclerView.ViewHolder {
         @BindView(R.id.txtAlbumTile)
         TextView txtAlbumTile;
         @BindView(R.id.txtAlbumSongs)
         TextView txtAlbumSongs;
         @BindView(R.id.imgAlbumThumbnail)
         ImageView imgAlbumThumbnail;

         ViewHolder(View itemView) {
             super(itemView);
             ButterKnife.bind(this, itemView);
             itemView.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     Handler handler = new Handler();
                     handler.postDelayed(new Runnable() {
                         @Override
                         public void run() {
                             //Todo load album content
                             NavigationUtils.navigateToAlbum(ScreenIDs.ID.SEARCH_TAB,mAppContext,albumList.get(getAdapterPosition()).getAlbumId(),new Pair<View, String>(imgAlbumThumbnail, "transition_album_art" + getAdapterPosition()));

                         }
                     }, 100);


                 }
             });
         }

     }

}
