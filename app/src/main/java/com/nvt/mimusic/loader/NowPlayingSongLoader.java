package com.nvt.mimusic.loader;

import android.content.Context;
import android.database.AbstractCursor;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.util.Log;

import com.nvt.mimusic.core.MusicPlayer;
import com.nvt.mimusic.model.Song;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Admin on 12/12/17.
 */

public class NowPlayingSongLoader {
    private static NowPlayingCursor mCursor;

    public static List<Song> getQueueSongs(Context context) {

        final ArrayList<Song> mSongList = new ArrayList<>();
        mCursor = new NowPlayingCursor(context);

        if (mCursor != null && mCursor.moveToFirst()) {
            do {

                final long id = mCursor.getLong(0);

                final String songName = mCursor.getString(1);

                final String artist = mCursor.getString(2);

                final long albumId = mCursor.getLong(3);

                final String album = mCursor.getString(4);

                final int duration = mCursor.getInt(5);

                final long artistid = mCursor.getInt(7);

                final int tracknumber = mCursor.getInt(6);

                final Song song = new Song(songName,id,artist,artistid, album,  albumId, tracknumber,   duration);

                mSongList.add(song);
            } while (mCursor.moveToNext());
        }
        if (mCursor != null) {
            mCursor.close();
            mCursor = null;
        }
        return mSongList;
    }

    public static  class NowPlayingCursor extends AbstractCursor {


        private static final String[] PROJECTION = new String[]{

                BaseColumns._ID,

                MediaStore.Audio.AudioColumns.TITLE,

                MediaStore.Audio.AudioColumns.ARTIST,

                MediaStore.Audio.AudioColumns.ALBUM_ID,

                MediaStore.Audio.AudioColumns.ALBUM,

                MediaStore.Audio.AudioColumns.DURATION,

                MediaStore.Audio.AudioColumns.TRACK,

                MediaStore.Audio.AudioColumns.ARTIST_ID,

                MediaStore.Audio.AudioColumns.TRACK,
        };
        private final Context mContext;

        private long[] mNowPlaying;

        private long[] mCursorIndexes;

        private int mSize;

        private int mCurPos;

        private Cursor mQueueCursor;

        public NowPlayingCursor(Context mContext) {
            this.mContext = mContext;
            makeNowPlayingCursor();
        }

        private void makeNowPlayingCursor() {
            mQueueCursor = null;
            mNowPlaying = MusicPlayer.getQueue();
            Log.d("lol1", mNowPlaying.toString() + "   " + mNowPlaying.length);
            mSize = mNowPlaying.length;
            if (mSize == 0) {
                return;
            }

            final StringBuilder selection = new StringBuilder();
            selection.append(MediaStore.Audio.Media._ID + " IN (");
            for (int i = 0; i < mSize; i++) {
                selection.append(mNowPlaying[i]);
                if (i < mSize - 1) {
                    selection.append(",");
                }
            }
            selection.append(")");

            mQueueCursor = mContext.getContentResolver().query(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, PROJECTION, selection.toString(),
                    null, MediaStore.Audio.Media._ID);

            if (mQueueCursor == null) {
                mSize = 0;
                return;
            }

            final int playlistSize = mQueueCursor.getCount();
            mCursorIndexes = new long[playlistSize];
            mQueueCursor.moveToFirst();
            final int columnIndex = mQueueCursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID);
            for (int i = 0; i < playlistSize; i++) {
                mCursorIndexes[i] = mQueueCursor.getLong(columnIndex);
                mQueueCursor.moveToNext();
            }
            mQueueCursor.moveToFirst();
            mCurPos = -1;

            int removed = 0;
            for (int i = mNowPlaying.length - 1; i >= 0; i--) {
                final long trackId = mNowPlaying[i];
                final int cursorIndex = Arrays.binarySearch(mCursorIndexes, trackId);
                if (cursorIndex < 0) {
                    removed += MusicPlayer.removeTrack(trackId);
                }
            }
            if (removed > 0) {
                mNowPlaying = MusicPlayer.getQueue();
                mSize = mNowPlaying.length;
                if (mSize == 0) {
                    mCursorIndexes = null;
                    return;
                }
            }
        }


        @Override
        public int getCount() {
            return mSize;
        }

        @Override
        public boolean onMove(final int oldPosition, final int newPosition) {
            if (oldPosition == newPosition) {
                return true;
            }

            if (mNowPlaying == null || mCursorIndexes == null || newPosition >= mNowPlaying.length) {
                return false;
            }

            final long id = mNowPlaying[newPosition];
            final int cursorIndex = Arrays.binarySearch(mCursorIndexes, id);
            mQueueCursor.moveToPosition(cursorIndex);
            mCurPos = newPosition;
            return true;
        }

        @Override
        public String getString(final int column) {
            try {
                return mQueueCursor.getString(column);
            } catch (final Exception ignored) {
                onChange(true);
                return "";
            }
        }


        @Override
        public short getShort(final int column) {
            return mQueueCursor.getShort(column);
        }


        @Override
        public int getInt(final int column) {
            try {
                return mQueueCursor.getInt(column);
            } catch (final Exception ignored) {
                onChange(true);
                return 0;
            }
        }


        @Override
        public long getLong(final int column) {
            try {
                return mQueueCursor.getLong(column);
            } catch (final Exception ignored) {
                onChange(true);
                return 0;
            }
        }


        @Override
        public float getFloat(final int column) {
            return mQueueCursor.getFloat(column);
        }


        @Override
        public double getDouble(final int column) {
            return mQueueCursor.getDouble(column);
        }


        @Override
        public int getType(final int column) {
            return mQueueCursor.getType(column);
        }

        @Override
        public boolean isNull(final int column) {
            return mQueueCursor.isNull(column);
        }


        @Override
        public String[] getColumnNames() {
            return PROJECTION;
        }


        @SuppressWarnings("deprecation")
        @Override
        public void deactivate() {
            if (mQueueCursor != null) {
                mQueueCursor.deactivate();
            }
        }

        @Override
        public boolean requery() {
            makeNowPlayingCursor();
            return true;
        }


        @Override
        public void close() {
            try {
                if (mQueueCursor != null) {
                    mQueueCursor.close();
                    mQueueCursor = null;
                }
            } catch (final Exception close) {
            }
            super.close();
        }
    }
}
