package com.nvt.mimusic.core;

import android.os.RemoteException;

import com.nvt.mimusic.MiCoreService;
import com.nvt.mimusic.helper.MusicPlaybackTrack;

/**
 * Created by Admin on 11/22/17.
 */

public class ServiceStub extends MiCoreService.Stub {
    public ServiceStub(MusicService musicService) {
    }

    @Override
    public void openFile(String path) throws RemoteException {

    }

    @Override
    public void prepareData(long[] list, int position, long sourceId, int sourceType) throws RemoteException {

    }

    @Override
    public void stop() throws RemoteException {

    }

    @Override
    public void pause() throws RemoteException {

    }

    @Override
    public void play() throws RemoteException {

    }

    @Override
    public void prev(boolean forcePrevious) throws RemoteException {

    }

    @Override
    public void next() throws RemoteException {

    }

    @Override
    public void enqueue(long[] list, int action, long sourceId, int sourceType) throws RemoteException {

    }

    @Override
    public void setQueuePosition(int index) throws RemoteException {

    }

    @Override
    public void setShuffleMode(int shufflemode) throws RemoteException {

    }

    @Override
    public void setRepeatMode(int repeatmode) throws RemoteException {

    }

    @Override
    public void moveQueueItem(int from, int to) throws RemoteException {

    }

    @Override
    public void refresh() throws RemoteException {

    }

    @Override
    public void playlistChanged() throws RemoteException {

    }

    @Override
    public boolean isPlaying() throws RemoteException {
        return false;
    }

    @Override
    public long[] getQueue() throws RemoteException {
        return new long[0];
    }

    @Override
    public long getQueueItemAtPosition(int position) throws RemoteException {
        return 0;
    }

    @Override
    public int getQueueSize() throws RemoteException {
        return 0;
    }

    @Override
    public int getQueuePosition() throws RemoteException {
        return 0;
    }

    @Override
    public int getQueueHistoryPosition(int position) throws RemoteException {
        return 0;
    }

    @Override
    public int getQueueHistorySize() throws RemoteException {
        return 0;
    }

    @Override
    public int[] getQueueHistoryList() throws RemoteException {
        return new int[0];
    }

    @Override
    public long duration() throws RemoteException {
        return 0;
    }

    @Override
    public long position() throws RemoteException {
        return 0;
    }

    @Override
    public long seek(long pos) throws RemoteException {
        return 0;
    }

    @Override
    public void seekRelative(long deltaInMs) throws RemoteException {

    }

    @Override
    public long getAudioId() throws RemoteException {
        return 0;
    }

    @Override
    public MusicPlaybackTrack getCurrentTrack() throws RemoteException {
        return null;
    }

    @Override
    public MusicPlaybackTrack getTrack(int index) throws RemoteException {
        return null;
    }

    @Override
    public long getNextAudioId() throws RemoteException {
        return 0;
    }

    @Override
    public long getPreviousAudioId() throws RemoteException {
        return 0;
    }

    @Override
    public long getArtistId() throws RemoteException {
        return 0;
    }

    @Override
    public long getAlbumId() throws RemoteException {
        return 0;
    }

    @Override
    public String getArtistName() throws RemoteException {
        return null;
    }

    @Override
    public String getTrackName() throws RemoteException {
        return null;
    }

    @Override
    public String getAlbumName() throws RemoteException {
        return null;
    }

    @Override
    public String getPath() throws RemoteException {
        return null;
    }

    @Override
    public int getShuffleMode() throws RemoteException {
        return 0;
    }

    @Override
    public int removeTracks(int first, int last) throws RemoteException {
        return 0;
    }

    @Override
    public int removeTrack(long id) throws RemoteException {
        return 0;
    }

    @Override
    public boolean removeTrackAtPosition(long id, int position) throws RemoteException {
        return false;
    }

    @Override
    public int getRepeatMode() throws RemoteException {
        return 0;
    }

    @Override
    public int getMediaMountedCount() throws RemoteException {
        return 0;
    }

    @Override
    public int getAudioSessionId() throws RemoteException {
        return 0;
    }
}
