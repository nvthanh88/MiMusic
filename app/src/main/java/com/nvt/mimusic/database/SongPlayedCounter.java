package com.nvt.mimusic.database;
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
    // how many weeks worth of playback to track?
    private static final int NUM_WEEKS = 52;

    // interpolator curve applied for measuring the curve
    private static Interpolator sInterpolator = new AccelerateInterpolator(1.5f);
    /**
     * Constructor
     *
     * @param factor Degree to which the animation should be eased. Seting
     *        factor to 1.0f produces a y=x^2 parabola. Increasing factor above
     *        1.0f  exaggerates the ease-in effect (i.e., it starts even
     *        slower and ends evens faster)
     */
}
