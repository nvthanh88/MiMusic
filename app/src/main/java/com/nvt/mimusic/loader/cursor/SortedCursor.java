package com.nvt.mimusic.loader.cursor;

import android.database.AbstractCursor;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Admin on 12/13/17.
 */


/**
 * This cursor basically wraps a song cursor and is given a list of the order of the ids of the
 * contents of the cursor. It wraps the Cursor and simulates the internal cursor being sorted
 * by moving the point to the appropriate spot
 */

public class SortedCursor extends AbstractCursor {
    //Cursor to wrap
    private static Cursor mCursor;
    //List order
    private ArrayList<Integer> mOrderedPositions;
    // this contains the ids that weren't found in the underlying cursor
    private ArrayList<Long> mMissingIds;
    // this contains the mapped cursor positions and afterwards the extra ids that weren't found
    private HashMap<Long, Integer> mMapCursorPositions;
    // extra we want to store with the cursor
    private ArrayList<Object> mExtraData;


    public static Cursor getmCursor() {
        return mCursor;
    }

    public static void setmCursor(Cursor mCursor) {
        SortedCursor.mCursor = mCursor;
    }

    public ArrayList<Integer> getmOrderedPositions() {
        return mOrderedPositions;
    }

    public void setmOrderedPositions(ArrayList<Integer> mOrderedPositions) {
        this.mOrderedPositions = mOrderedPositions;
    }

    public ArrayList<Long> getmMissingIds() {
        return mMissingIds;
    }

    public void setmMissingIds(ArrayList<Long> mMissingIds) {
        this.mMissingIds = mMissingIds;
    }

    public HashMap<Long, Integer> getmMapCursorPositions() {
        return mMapCursorPositions;
    }

    public void setmMapCursorPositions(HashMap<Long, Integer> mMapCursorPositions) {
        this.mMapCursorPositions = mMapCursorPositions;
    }

    public ArrayList<Object> getmExtraData() {
        return mExtraData;
    }

    public void setmExtraData(ArrayList<Object> mExtraData) {
        this.mExtraData = mExtraData;
    }

    /**
     * @param cursor     to wrap
     * @param order      the list of unique ids in sorted order to display
     * @param columnName the column name of the id to look up in the internal cursor
     */
    public SortedCursor(final Cursor cursor,long[] whatOrder , final String columnName, final List<? extends Object> extraData) {
        if (cursor == null) throw new IllegalArgumentException();
        mCursor = cursor;
        mMissingIds = buildMissingIdsMap(whatOrder ,columnName ,extraData);

    }
    /**
     * This function populates mOrderedPositions with the cursor positions in the order based
     * on the order passed in
     *
     * @param order     the target order of the internal cursor
     * @param extraData Extra data we want to add to the cursor
     * @return returns the ids that aren't found in the underlying cursor
     */
    private ArrayList<Long> buildMissingIdsMap(long[] whatOrder, String columnName, List<?> extraData) {
        ArrayList<Long> missingIds = new ArrayList<>();
        mOrderedPositions = new ArrayList<>(mCursor.getCount());
        mExtraData = new ArrayList<>();
        mMapCursorPositions = new HashMap<>(mCursor.getCount());
        final  int columnIndex = mCursor.getColumnIndex(columnName);
        if (mCursor != null && mCursor.moveToFirst()) {
            // first figure out where each of the ids are in the cursor
            do {
                mMapCursorPositions.put(mCursor.getLong(columnIndex),mCursor.getPosition());
            } while (mCursor.moveToNext());
            // now create the ordered positions to map to the internal cursor given the
            // external sort order
            for (int i = 0 ; whatOrder != null && i < whatOrder.length ; i++)
            {
                final  long id = whatOrder[i];
                if (mMapCursorPositions.containsKey(id))
                {
                    mOrderedPositions.add(mMapCursorPositions.get(id));
                    mMapCursorPositions.remove(id);
                    if (extraData != null)
                    {
                        mExtraData.add(extraData.get(i));
                    }
                }else missingIds.add(id);
            }
            mCursor.moveToFirst();
        }

        return missingIds;

    }




    @Override
    public int getCount() {
        return mOrderedPositions.size();
    }

    @Override
    public String[] getColumnNames() {
        return mCursor.getColumnNames();
    }

    @Override
    public String getString(int column) {
        return mCursor.getString(column);
    }

    @Override
    public short getShort(int column) {
        return mCursor.getShort(column);
    }

    @Override
    public int getInt(int column) {
        return mCursor.getInt(column);
    }

    @Override
    public long getLong(int column) {
        return mCursor.getLong(column);
    }

    @Override
    public float getFloat(int column) {
        return mCursor.getFloat(column);
    }

    @Override
    public double getDouble(int column) {
        return mCursor.getDouble(column);
    }

    @Override
    public boolean isNull(int column) {
        return mCursor.isNull(column);
    }

    @Override
    public void close() {
        mCursor.close();
        super.close();
    }

    @Override
    public boolean onMove(int oldPosition, int newPosition) {
        if (newPosition >= 0 && newPosition < getCount()) {
            mCursor.moveToPosition(mOrderedPositions.get(newPosition));
            return true;
        }
        return false;
    }
}
