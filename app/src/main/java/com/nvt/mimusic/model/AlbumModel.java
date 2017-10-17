package com.nvt.mimusic.model;

/**
 * Created by Admin on 10/16/17.
 */

public class AlbumModel {
    private String name;
    private int numberOfSongs;
    private int albumCover;

    public AlbumModel() {
    }

    public AlbumModel(String name, int numberOfSongs, int albumCover) {
        this.name = name;
        this.numberOfSongs = numberOfSongs;
        this.albumCover = albumCover;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumberOfSongs() {
        return numberOfSongs;
    }

    public void setNumberOfSongs(int numberOfSongs) {

        this.numberOfSongs = numberOfSongs;
    }

    public int getAlbumCover() {
        return albumCover;
    }

    public void setAlbumCover(int albumCover) {
        this.albumCover = albumCover;
    }
}
