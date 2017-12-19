/*
 * Copyright (C) 2015 Naman Dwivedi
 *
 * Licensed under the GNU General Public License v3
 *
 * This is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 */

package com.nvt.mimusic.utils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.util.Pair;
import android.view.View;


import com.nvt.mimusic.R;
import com.nvt.mimusic.base.fragment.MiBaseFragment;
import com.nvt.mimusic.constant.ScreenIDs;
import com.nvt.mimusic.core.MiApplication;
import com.nvt.mimusic.view.fragment.album.AlbumDetailsFragment;
import com.nvt.mimusic.view.fragment.home.AlbumFragment;
import com.nvt.mimusic.view.fragment.home.SongFragment;
import com.nvt.mimusic.view.fragment.now_playing.NowPlayingFragment;

import java.util.ArrayList;

public class NavigationUtils {
    private static final String TAG = NavigationUtils.class.getSimpleName();

    @SuppressLint("ResourceType")
    @TargetApi(21)
    public static void navigateToAlbum(ScreenIDs.ID tab,Activity context, long albumID, Pair<View, String> transitionViews) {
        Log.i(TAG, "navigateToAlbum: " + albumID);
        FragmentManager fragmentManager = context.getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment mFragment;
        if (MiApplication.isLollipop())
        {
            Transition changeImage = TransitionInflater.from(context).inflateTransition(R.transition.image_transform);
            fragmentTransaction.addSharedElement(transitionViews.first,transitionViews.second);
            mFragment = AlbumDetailsFragment.newInstance(albumID,true,transitionViews.second);
            mFragment.setSharedElementEnterTransition(changeImage);

        }else
        {
            fragmentTransaction.setCustomAnimations(R.anim.activity_fade_in,
                    R.anim.activity_fade_out, R.anim.activity_fade_in, R.anim.activity_fade_out);
            mFragment = AlbumDetailsFragment.newInstance(albumID,false,null);
        }

        fragmentTransaction.hide(fragmentManager.findFragmentById(R.id.frameSongContent));
        fragmentTransaction.hide(fragmentManager.findFragmentById(R.id.frameAlbumContent));
        fragmentTransaction.replace(R.id.frameMainContent,mFragment);
        fragmentTransaction.addToBackStack(null).commit();


    }

    @TargetApi(21)
    public static void navigateToArtist(Activity context, long artistID, Pair<View, String> transitionViews) {



    }


    @TargetApi(21)
    public static void navigateToPlaylistDetail(ScreenIDs.ID tab,Activity context, String action, long firstAlbumID, String playlistName, int foregroundcolor, long playlistID, ArrayList<Pair> transitionViews) {

    }
    public static void navigateToNowPlaying(Activity context) {
        Log.i(TAG, "navigateToNowPlaying: ");
        FragmentManager fragmentManager = context.getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment mFragment = new NowPlayingFragment();
        fragmentTransaction.hide(fragmentManager.findFragmentById(R.id.frameSongContent));
        fragmentTransaction.hide(fragmentManager.findFragmentById(R.id.frameAlbumContent));
        fragmentTransaction.replace(R.id.frameMainContent,mFragment);
        fragmentTransaction.addToBackStack(null).commit();
    }
    public static void navigateToHome( Activity context)
    {
        Log.i(TAG, "navigateToHome: ");
        FragmentManager fragmentManager = context.getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment mSongFragment = new SongFragment();
        Fragment mAlbumFragment = new AlbumFragment();
        fragmentTransaction.hide(fragmentManager.findFragmentById(R.id.frameMainContent));
        fragmentTransaction.replace(R.id.frameSongContent,mSongFragment);
        fragmentTransaction.replace(R.id.frameAlbumContent,mAlbumFragment);
        fragmentTransaction.addToBackStack(null).commit();
    }







}
