package com.nvt.mimusic.database;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;
/**
 * Created by Admin on 12/13/17.
 */
/**
 * This database tracks the number of play counts for an individual song.  This is used to drive
 * the top played tracks as well as the playlist images
 */

public class SongPlayedCounter {

    public interface SongPlayCountColumns {

        /* Table name */
        String NAME = "songplayedcount";

        /* Song IDs column */
        String ID = "songid";

        /* Week Play Count */
        String WEEK_PLAY_COUNT = "week";

        /* Weeks since Epoch */
        String LAST_UPDATED_WEEK_INDEX = "weekindex";

        /* Play count */
        String PLAYCOUNTSCORE = "playcountscore";
    }

    // how many weeks worth of playback to track?
    private static final int NUM_WEEKS = 52;
    private static SongPlayedCounter sInstance = null;
    // how high to multiply the interpolation curve
    private static int INTERPOLATOR_HEIGHT = 50;
    // interpolator curve applied for measuring the curve
    private static Interpolator sInterpolator = new AccelerateInterpolator(1.5f);
    private static int INTERPOLATOR_BASE = 25;
    private static int ONE_WEEK_IN_MS = 1000 * 60 * 60 * 24 * 7;
    private static String WHERE_ID_EQUALS = SongPlayCountColumns.ID + "=?";
    private MusicDatabase mMusicDatabase = null;
    // number of weeks since epoch time
    private int mNumberOfWeeksSinceEpoch;

    // used to track if we've walkd through the db and updated all the rows
    private boolean mDatabaseUpdated;
    /**
     * Constructor
     *
     * @param factor Degree to which the animation should be eased. Seting
     *        factor to 1.0f produces a y=x^2 parabola. Increasing factor above
     *        1.0f  exaggerates the ease-in effect (i.e., it starts even
     *        slower and ends evens faster)
     */

    /**
     * Constructor of <code>RecentStore</code>
     *
     * @param context The {@link android.content.Context} to use
     */
    public SongPlayedCounter(final Context context) {
        mMusicDatabase = MusicDatabase.getMusicDatabaseInstance(context);
        long msSinceEpoch = System.currentTimeMillis();
        mNumberOfWeeksSinceEpoch = (int) (msSinceEpoch / ONE_WEEK_IN_MS);
        mDatabaseUpdated = false;
    }
    /**
     * @param context The {@link android.content.Context} to use
     * @return A new instance of this class.
     */
    public static final synchronized SongPlayedCounter getInstance(final Context context) {
        if (sInstance == null) {
            sInstance = new SongPlayedCounter(context.getApplicationContext());
        }
        return sInstance;
    }
    /**
     * Calculates the score of the song given the play counts
     *
     * @param playCounts an array of the # of times a song has been played for each week
     *                   where playCounts[N] is the # of times it was played N weeks ago
     * @return the score
     */
    private static float calculateScore(final int[] playCounts) {
        if (playCounts == null) {
            return 0;
        }

        float score = 0;
        for (int i = 0; i < Math.min(playCounts.length, NUM_WEEKS); i++) {
            score += playCounts[i] * (i + 1);
        }

        return score;
    }
    /**
     * Gets the column name for each week #
     *
     * @param week number
     * @return the column name
     */
    private static String getColumnNameForWeek(final int week) {
        return SongPlayCountColumns.WEEK_PLAY_COUNT + String.valueOf(week);
    }

    /**
     * Gets the score multiplier for each week
     *
     * @param week number
     * @return the multiplier to apply
     */
    private static float getScoreMultiplierForWeek(final int week) {
        return sInterpolator.getInterpolation(1 - (week / (float) NUM_WEEKS)) * INTERPOLATOR_HEIGHT
                + INTERPOLATOR_BASE;
    }
    /**
     * Create table songplaycount with SQL Command: CREATE TABLE IF NOT EXISTS songplayedcount (songid INT UNIQUE,week INT NOT NULL,weekindex INT NOT NULL,playcountscore REAL DEFAULT 0);
     * */
    public void onCreate(final SQLiteDatabase database){
        StringBuilder builder = new StringBuilder();
        builder.append("CREATE TABLE IF NOT EXISTS");
        builder.append(SongPlayCountColumns.NAME + "(");
        builder.append(SongPlayCountColumns.ID);
        builder.append("INT UNIQUE,");
        for (int i = 0; i < NUM_WEEKS; i++) {
            builder.append(getColumnNameForWeek(i));
            builder.append("INT NOT NULL,");
        }

        builder.append(SongPlayCountColumns.LAST_UPDATED_WEEK_INDEX);
        builder.append("INT NOT NULL,");
        builder.append(SongPlayCountColumns.PLAYCOUNTSCORE);
        builder.append("REAL DEFAULT 0);");
        database.execSQL(builder.toString());

    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // If we ever have downgrade, drop the table to be safe
        db.execSQL("DROP TABLE IF EXISTS " + SongPlayCountColumns.NAME);
        onCreate(db);
    }

    /**
     * Increase Song count to 1 when played
     *
     * */

    public void increaseSongCount(final long songId){
        if (songId < 0) {
            return;
        }

        final SQLiteDatabase database = mMusicDatabase.getWritableDatabase();
        updateExistingRow(database,songId,true);

    }
    private void updateExistingRow(final SQLiteDatabase database, final long id, boolean bumpCount) {
        String stringId = String.valueOf(id);

        // begin the transaction
        database.beginTransaction();

        // get the cursor of this content inside the transaction
        final Cursor cursor = database.query(SongPlayCountColumns.NAME, null, WHERE_ID_EQUALS,
                new String[]{stringId}, null, null, null);

        // if we have a result
        if (cursor != null && cursor.moveToFirst()) {
            // figure how many weeks since we last updated
            int lastUpdatedIndex = cursor.getColumnIndex(SongPlayCountColumns.LAST_UPDATED_WEEK_INDEX);
            int lastUpdatedWeek = cursor.getInt(lastUpdatedIndex);
            int weekDiff = mNumberOfWeeksSinceEpoch - lastUpdatedWeek;

            // if it's more than the number of weeks we track, delete it and create a new entry
            if (Math.abs(weekDiff) >= NUM_WEEKS) {
                // this entry needs to be dropped since it is too outdated
                deleteEntry(database, stringId);
                if (bumpCount) {
                    createNewPlayedEntry(database, id);
                }
            } else if (weekDiff != 0) {
                // else, shift the weeks
                int[] playCounts = new int[NUM_WEEKS];

                if (weekDiff > 0) {
                    // time is shifted forwards
                    for (int i = 0; i < NUM_WEEKS - weekDiff; i++) {
                        playCounts[i + weekDiff] = cursor.getInt(getColumnIndexForWeek(i));
                    }
                } else if (weekDiff < 0) {
                    // time is shifted backwards (by user) - nor typical behavior but we
                    // will still handle it

                    // since weekDiff is -ve, NUM_WEEKS + weekDiff is the real # of weeks we have to
                    // transfer.  Then we transfer the old week i - weekDiff to week i
                    // for example if the user shifted back 2 weeks, ie -2, then for 0 to
                    // NUM_WEEKS + (-2) we set the new week i = old week i - (-2) or i+2
                    for (int i = 0; i < NUM_WEEKS + weekDiff; i++) {
                        playCounts[i] = cursor.getInt(getColumnIndexForWeek(i - weekDiff));
                    }
                }

                // bump the count
                if (bumpCount) {
                    playCounts[0]++;
                }

                float score = calculateScore(playCounts);

                // if the score is non-existant, then delete it
                if (score < .01f) {
                    deleteEntry(database, stringId);
                } else {
                    // create the content values
                    ContentValues values = new ContentValues(NUM_WEEKS + 2);
                    values.put(SongPlayCountColumns.LAST_UPDATED_WEEK_INDEX, mNumberOfWeeksSinceEpoch);
                    values.put(SongPlayCountColumns.PLAYCOUNTSCORE, score);

                    for (int i = 0; i < NUM_WEEKS; i++) {
                        values.put(getColumnNameForWeek(i), playCounts[i]);
                    }

                    // update the entry
                    database.update(SongPlayCountColumns.NAME, values, WHERE_ID_EQUALS,
                            new String[]{stringId});
                }
            } else if (bumpCount) {
                // else no shifting, just update the scores
                ContentValues values = new ContentValues(2);

                // increase the score by a single score amount
                int scoreIndex = cursor.getColumnIndex(SongPlayCountColumns.PLAYCOUNTSCORE);
                float score = cursor.getFloat(scoreIndex) + getScoreMultiplierForWeek(0);
                values.put(SongPlayCountColumns.PLAYCOUNTSCORE, score);

                // increase the play count by 1
                values.put(getColumnNameForWeek(0), cursor.getInt(getColumnIndexForWeek(0)) + 1);

                // update the entry
                database.update(SongPlayCountColumns.NAME, values, WHERE_ID_EQUALS,
                        new String[]{stringId});
            }

            cursor.close();
        } else if (bumpCount) {
            // if we have no existing results, create a new one
            createNewPlayedEntry(database, id);
        }

        database.setTransactionSuccessful();
        database.endTransaction();
    }
    private void deleteEntry(final SQLiteDatabase database, final String stringId) {
        database.delete(SongPlayCountColumns.NAME, WHERE_ID_EQUALS, new String[]{stringId});
    }
    /**
     * This creates a new entry that indicates a song has been played once as well as its score
     *
     * @param database a writeable database
     * @param songId   the id of the track
     */
    private void createNewPlayedEntry(final SQLiteDatabase database, final long songId) {
        // no row exists, create a new one
        float newScore = getScoreMultiplierForWeek(0);
        int newPlayCount = 1;

        final ContentValues values = new ContentValues(3);
        values.put(SongPlayCountColumns.ID, songId);
        values.put(SongPlayCountColumns.PLAYCOUNTSCORE, newScore);
        values.put(SongPlayCountColumns.LAST_UPDATED_WEEK_INDEX, mNumberOfWeeksSinceEpoch);
        values.put(getColumnNameForWeek(0), newPlayCount);

        database.insert(SongPlayCountColumns.NAME, null, values);
    }
    /**
     * For some performance gain, return a static value for the column index for a week
     * WARNIGN: This function assumes you have selected all columns for it to work
     *
     * @param week number
     * @return column index of that week
     */
    private static int getColumnIndexForWeek(final int week) {
        // ID, followed by the weeks columns
        return 1 + week;
    }
    /**
     * Gets a cursor containing the top songs played.  Note this only returns songs that have been
     * played at least once in the past NUM_WEEKS
     *
     * @param numResults number of results to limit by.  If <= 0 it returns all results
     * @return the top tracks
     */
    public Cursor getTopPlayedResults(int numResults) {
        updateResults();

        final SQLiteDatabase database = mMusicDatabase.getReadableDatabase();
        return database.query(SongPlayCountColumns.NAME, new String[]{SongPlayCountColumns.ID},
                null, null, null, null, SongPlayCountColumns.PLAYCOUNTSCORE + " DESC",
                (numResults <= 0 ? null : String.valueOf(numResults)));
    }
    /**
     * This updates all the results for the getTopPlayedResults so that we can get an
     * accurate list of the top played results
     */
    private synchronized void updateResults() {
        if (mDatabaseUpdated) {
            return;
        }

        final SQLiteDatabase database = mMusicDatabase.getWritableDatabase();

        database.beginTransaction();

        int oldestWeekWeCareAbout = mNumberOfWeeksSinceEpoch - NUM_WEEKS + 1;
        // delete rows we don't care about anymore
        database.delete(SongPlayCountColumns.NAME, SongPlayCountColumns.LAST_UPDATED_WEEK_INDEX
                + " < " + oldestWeekWeCareAbout, null);

        // get the remaining rows
        Cursor cursor = database.query(SongPlayCountColumns.NAME,
                new String[]{SongPlayCountColumns.ID},
                null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            // for each row, update it
            do {
                updateExistingRow(database, cursor.getLong(0), false);
            } while (cursor.moveToNext());

            cursor.close();
            cursor = null;
        }

        mDatabaseUpdated = true;
        database.setTransactionSuccessful();
        database.endTransaction();
    }
    /**
     * @param songId The song Id to remove.
     */
    public void removeItem(final long songId) {
        final SQLiteDatabase database = mMusicDatabase.getWritableDatabase();
        deleteEntry(database, String.valueOf(songId));
    }



}
