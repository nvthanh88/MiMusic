package com.nvt.mimusic.model;

/**
 * Created by Admin on 10/16/17.
 */

public class Album {
    private long albumId;
    private String name;
    private String artistName;
    private long artistId;
    private int numberOfSongs;

    public long getAlbumId() {
        return albumId;
    }

    public Album() {
    }

    public void setAlbumId(long albumId) {
        this.albumId = albumId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public long getArtistId() {
        return artistId;
    }

    public void setArtistId(long artistId) {
        this.artistId = artistId;
    }

    public int getNumberOfSongs() {
        return numberOfSongs;
    }

    public void setNumberOfSongs(int numberOfSongs) {
        this.numberOfSongs = numberOfSongs;
    }

    public Album(long albumId, String name, String artistName, long artistId, int numberOfSongs) {

        this.albumId = albumId;
        this.name = name;
        this.artistName = artistName;
        this.artistId = artistId;
        this.numberOfSongs = numberOfSongs;
    }
}
