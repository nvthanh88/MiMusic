package com.nvt.mimusic.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nvt.mimusic.R;
import com.nvt.mimusic.model.Song;
import com.nvt.mimusic.wiget.CircleImageView;

import java.util.List;
import java.util.zip.Inflater;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Admin on 12/6/17.
 */

public class AlbumDetailsAdapter extends RecyclerView.Adapter<AlbumDetailsAdapter.ViewHolder> {
    private Activity mAppContext;
    private List<Song> songList;
    private long[] songIds;
    private long albumId;

    public AlbumDetailsAdapter(Activity mAppContext, List<Song> songList, long albumId) {
        this.mAppContext = mAppContext;
        this.songList = songList;
        this.songIds = getSongIds();
        this.albumId = albumId;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_album_songs,parent,false);
        return new ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Song songItem = songList.get(position);
        holder.txtAlbumArtistName.setText(songItem.getArtistName());
        holder.txtAlbumSongTitle.setText(songItem.getName());
        holder.txtAlbumSongIndex.setText(String.valueOf(songItem.getTrackNumber()));

    }

    @Override
    public int getItemCount() {
        return songList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txtAlbumSongTitle)
        TextView txtAlbumSongTitle;
        @BindView(R.id.txtAlbumSongIndex)
        TextView txtAlbumSongIndex;
        @BindView(R.id.txtAlbumArtistName)
        TextView txtAlbumArtistName;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
    public long[] getSongIds() {
        long[] ret = new long[getItemCount()];
        for (int i = 0; i < getItemCount(); i++) {
            ret[i] = songList.get(i).getSongId();
        }

        return ret;
    }
}
