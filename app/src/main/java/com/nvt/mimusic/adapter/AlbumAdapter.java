package com.nvt.mimusic.adapter;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.nvt.mimusic.model.AlbumModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


import com.nvt.mimusic.R;
import com.nvt.mimusic.view.activity.MiMainActivity;

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
        Glide.with(mAppContext).load(albumModelItem.getAlbumCover()).into(holder.imgAlbumThumbnail);
        holder.imgOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopUpMenu(holder.imgOptions);
                Toast.makeText(mAppContext,"Onclick :" + albumModelItem.getName(),Toast.LENGTH_LONG).show();
            }
        });
    }
    private void showPopUpMenu(View view){
        PopupMenu popup = new PopupMenu(mAppContext,view);
        popup.getMenuInflater().inflate(R.menu.menu_album, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
        popup.show();
    }
    private class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {
        private MyMenuItemClickListener() {
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch(item.getItemId()){
                case R.id.action_add_favourite:
                    Toast.makeText(mAppContext,"Add to Favourite",Toast.LENGTH_LONG).show();
                    break;
                case R.id.action_play_next:
                    Toast.makeText(mAppContext,"Add to play next",Toast.LENGTH_LONG).show();
            }

            return false;
        }
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
