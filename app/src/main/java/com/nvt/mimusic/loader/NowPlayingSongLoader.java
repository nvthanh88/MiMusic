package com.nvt.mimusic.loader;

import android.content.Context;
import android.database.AbstractCursor;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.util.Log;

import com.nvt.mimusic.core.MusicPlayer;

import java.util.Arrays;

/**
 * Created by Admin on 12/12/17.
 */

public class NowPlayingSongLoader {

    public static  class NowPlayingCursor extends AbstractCursor{


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
            return 0;
        }

        @Override
        public String[] getColumnNames() {
            return new String[0];
        }

        @Override
        public String getString(int column) {
            return null;
        }

        @Override
        public short getShort(int column) {
            return 0;
        }

        @Override
        public int getInt(int column) {
            return 0;
        }

        @Override
        public long getLong(int column) {
            return 0;
        }

        @Override
        public float getFloat(int column) {
            return 0;
        }

        @Override
        public double getDouble(int column) {
            return 0;
        }

        @Override
        public boolean isNull(int column) {
            return false;
        }
    }
}
