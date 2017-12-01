package com.nvt.mimusic.model;

/**
 * Created by Admin on 10/19/17.
 */

public class Song {
    private String name;
    private long songId;
    private String artistName;
    private long artisId;
    private String albumName;
    private long albumId;
    private int trackNumber;

    public int getTrackNumber() {
        return trackNumber;
    }

    public void setTrackNumber(int trackNumber) {
        this.trackNumber = trackNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getSongId() {
        return songId;
    }

    public void setSongId(long songId) {
        this.songId = songId;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public long getArtisId() {
        return artisId;
    }

    public void setArtisId(long artisId) {
        this.artisId = artisId;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(long albumId) {
        this.albumId = albumId;
    }

    public Song() {

    }

    public Song(String name, long songId, String artistName, long artisId, String albumName, long albumId , int trackNumber) {

        this.name = name;
        this.songId = songId;
        this.artistName = artistName;
        this.artisId = artisId;
        this.albumName = albumName;
        this.albumId = albumId;
        this.trackNumber = trackNumber;
    }
}
