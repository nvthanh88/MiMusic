package com.nvt.mimusic.core;

import android.os.RemoteException;
import android.util.Log;

import com.nvt.mimusic.MiCoreService;
import com.nvt.mimusic.helper.MusicPlaybackTrack;

import java.lang.ref.WeakReference;

/**
 * Created by Admin on 11/22/17.
 */

public class ServiceStub extends MiCoreService.Stub {
    private static final String TAG = "ServiceStub";
    private final WeakReference<MusicService> mService;
    public ServiceStub(MusicService musicService) {
        mService = new WeakReference<MusicService>(musicService);
    }

    @Override
    public void openFile(String path) throws RemoteException {

    }

    @Override
    public void prepareData(long[] list, int position, long sourceId, int sourceType) throws RemoteException {
        Log.i(TAG, "prepareData: " + list + position + sourceId + sourceType);
        mService.get().prepareData(list, position, sourceId, MiApplication.IdType.getTypeById(sourceType));

    }



    @Override
    public void stop() throws RemoteException {
        mService.get().stop(true);
    }

    @Override
    public void pause() throws RemoteException {
        mService.get().pause();
    }


    @Override
    public void play() throws RemoteException {
        mService.get().play();
    }

    @Override
    public void prev(boolean forcePrevious) throws RemoteException {
        mService.get().prev(forcePrevious);
    }

    @Override
    public void next() throws RemoteException {
        mService.get().gotoNext(true);
    }

    @Override
    public void enqueue(final long[] list, final int action, long sourceId, int sourceType)
            throws RemoteException {
        //mService.get().enqueue(list, action, sourceId, IdType.getTypeById(sourceType));
    }

    @Override
    public void moveQueueItem(final int from, final int to) throws RemoteException {
        //mService.get().moveQueueItem(from, to);
    }

    @Override
    public void refresh() throws RemoteException {
        //mService.get().refresh();
    }

    @Override
    public void playlistChanged() throws RemoteException {
        //mService.get().playlistChanged();
    }

    @Override
    public boolean isPlaying() throws RemoteException {
        return mService.get().isPlaying();
    }

    @Override
    public long[] getQueue() throws RemoteException {
        return mService.get().getQueue();
    }

    @Override
    public long getQueueItemAtPosition(int position) throws RemoteException {
        //return mService.get().getQueueItemAtPosition(position);
        return 0;
    }

    @Override
    public int getQueueSize() throws RemoteException {
        //return mService.get().getQueueSize();
        return 0;
    }

    @Override
    public int getQueueHistoryPosition(int position) throws RemoteException {
        //return mService.get().getQueueHistoryPosition(position);
        return 0;
    }

    @Override
    public int getQueueHistorySize() throws RemoteException {
        //return mService.get().getQueueHistorySize();
        return 0;
    }

    @Override
    public int[] getQueueHistoryList() throws RemoteException {
        //return mService.get().getQueueHistoryList();

        return new int[0];
    }

    @Override
    public long duration() throws RemoteException {
        return mService.get().duration();
    }

    @Override
    public long position() throws RemoteException {
        return mService.get().position();
    }

    @Override
    public long seek(final long position) throws RemoteException {
        return mService.get().seek(position);
    }

    @Override
    public void seekRelative(final long deltaInMs) throws RemoteException {
        //mService.get().seekRelative(deltaInMs);
    }

    @Override
    public long getAudioId() throws RemoteException {
        return mService.get().getAudioId();
    }

    @Override
    public MusicPlaybackTrack getCurrentTrack() throws RemoteException {
        return mService.get().getCurrentTrack();
    }

    @Override
    public MusicPlaybackTrack getTrack(int index) throws RemoteException {
        return mService.get().getTrack(index);
    }

    @Override
    public long getNextAudioId() throws RemoteException {
        /*return mService.get().getNextAudioId();*/
        return 0;
    }

    @Override
    public long getPreviousAudioId() throws RemoteException {
        //return mService.get().getPreviousAudioId();
        return 0;
    }

    @Override
    public long getArtistId() throws RemoteException {
        //return mService.get().getArtistId();
        return 0;
    }

    @Override
    public long getAlbumId() throws RemoteException {
        return mService.get().getAlbumId();
    }

    @Override
    public String getArtistName() throws RemoteException {
        return mService.get().getArtistName();
    }

    @Override
    public String getTrackName() throws RemoteException {
        return mService.get().getTrackName();
    }

    @Override
    public String getAlbumName() throws RemoteException {
        return mService.get().getAlbumName();
    }

    @Override
    public String getPath() throws RemoteException {
        //return mService.get().getPath();
        return null;
    }

    @Override
    public int getQueuePosition() throws RemoteException {
        return mService.get().getQueuePosition();
    }

    @Override
    public void setQueuePosition(final int index) throws RemoteException {
        //mService.get().setQueuePosition(index);
    }

    @Override
    public int getShuffleMode() throws RemoteException {
        //return mService.get().getShuffleMode();
        return 0;
    }

    @Override
    public void setShuffleMode(final int shufflemode) throws RemoteException {
        //mService.get().setShuffleMode(shufflemode);
    }

    @Override
    public int getRepeatMode() throws RemoteException {
        //return mService.get().getRepeatMode();
        return 0;
    }

    @Override
    public void setRepeatMode(final int repeatmode) throws RemoteException {
        //mService.get().setRepeatMode(repeatmode);
    }

    @Override
    public int removeTracks(final int first, final int last) throws RemoteException {
        //return mService.get().removeTracks(first, last);
        return 0;
    }


    @Override
    public int removeTrack(final long id) throws RemoteException {
        //return mService.get().removeTrack(id);
        return 0;
    }


    @Override
    public boolean removeTrackAtPosition(final long id, final int position)
            throws RemoteException {
        //return mService.get().removeTrackAtPosition(id, position);
        return false;
    }


    @Override
    public int getMediaMountedCount() throws RemoteException {
        //return mService.get().getMediaMountedCount();
        return 0;
    }


    @Override
    public int getAudioSessionId() throws RemoteException {
        return mService.get().getAudioSessionId();
    }


}
