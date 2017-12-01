package com.nvt.mimusic.view.activity;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.nvt.mimusic.R;
import com.nvt.mimusic.base.fragment.MiBaseFragment;
import com.nvt.mimusic.constant.ScreenIDs;
import com.nvt.mimusic.core.MiApplication;
import com.nvt.mimusic.utils.PermissionCallback;
import com.nvt.mimusic.utils.PermissionRequest;

import com.nvt.mimusic.view.fragment.home.AlbumFragment;
import com.nvt.mimusic.view.fragment.home.SongFragment;
import com.nvt.mimusic.view.panel.SlidingUpPanelLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Admin on 11/16/17.
 */

public class MiMainActivity extends MiBaseActivity {
    private final String TAG = getClass().getSimpleName();
    private Context context;
    private ScreenIDs.ID mCurrentTab;
    private MiBaseFragment mCurrentFragment;
   /* @BindView(R.id.slidingLayout)
    SlidingUpPanelLayout panelLayout;*/
    @BindView(R.id.toolbarTop)
    Toolbar toolbarTop;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbarTop);
        PermissionRequest.init(getApplicationContext());
        /**
         * Check OS Version > 6 ask for permission and else Todo smt
         * */
        //setupSlidePanel(panelLayout);
        if (MiApplication.isMarshmallow())
        {
            checkPermissionAndThenLoad();
        }else {
            gotoHomeFragment.run();
            //new initQuickControls().execute("");
        }
        /**
         * Setup palnel
         * */



    }

    /**
     * Check permission and load
     * */
    private void checkPermissionAndThenLoad() {
        //check for permission
        if (PermissionRequest.checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            gotoHomeFragment.run();
            //new initQuickControls().execute("");
        } else {
            if (PermissionRequest.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                /*Snackbar.make(panelLayout, "MiMusic Need READ_EXTERNAL_STORAGE Permission to load  media", Snackbar.LENGTH_LONG)
                        .setAction("OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                PermissionRequest.askForPermission(MiMainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE, permissionReadStorageCallback);
                            }
                        }).show();*/
            } else {
                PermissionRequest.askForPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE, permissionReadStorageCallback);
            }
        }
    }
    final PermissionCallback permissionReadStorageCallback = new PermissionCallback() {
        @Override
        public void permissionGranted() {
            gotoHomeFragment.run();
            //new initQuickControls().execute("");
        }

        @Override
        public void permissionRefused() {
            finish();
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionRequest.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    /**
     *Open Fragment
     * */
    Runnable gotoHomeFragment = new Runnable() {
        @Override
        public void run() {
            openScreenBackgroundTask(ScreenIDs.ID.HOME,AlbumFragment.class,R.id.frameAlbumContent,null,false);
            openScreenBackgroundTask(ScreenIDs.ID.HOME,SongFragment.class,R.id.frameSongContent,null,false);

        }
    };
    private void openScreenBackgroundTask(ScreenIDs.ID tab, final Class<? extends MiBaseFragment> fragmentClass,int contentFrame, Bundle bundles, boolean addToBackStack) {

        if (getBaseContext() == null) return;
        mCurrentTab = tab;

        FragmentManager fragmentManager = getFragmentManager();
        String tag = fragmentClass.getName();
        try {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            Log.e(TAG, "openScreen: fragment " + fragmentClass.getSimpleName() + " is NOT from back stack");
            mCurrentFragment = fragmentClass.newInstance();
            mCurrentFragment.setRetainInstance(true);
            if (bundles == null) bundles = new Bundle();
            mCurrentFragment.setArguments(bundles);
            if (addToBackStack) {
                fragmentTransaction.addToBackStack(tag);
                Log.e(TAG, "openScreen: add " + tag + " to back stack");
            }
            fragmentTransaction.replace(contentFrame, mCurrentFragment, tag);
            fragmentTransaction.commitAllowingStateLoss();
            fragmentManager.executePendingTransactions();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
