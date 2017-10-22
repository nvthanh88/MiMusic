package com.nvt.mimusic.view.activity;

import android.Manifest;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.nvt.mimusic.R;
import com.nvt.mimusic.adapter.AlbumAdapter;

import com.nvt.mimusic.base.fragment.MiBaseFragment;
import com.nvt.mimusic.constant.ScreenIDs;

import com.nvt.mimusic.helper.SessionManager;
import com.nvt.mimusic.model.AlbumModel;

import com.nvt.mimusic.view.home.AlbumFragment;
import com.nvt.mimusic.view.home.SongFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MiMainActivity extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 001;
    @BindView(R.id.toolbarTop)
    Toolbar toolbarTop;

    private String TAG = getClass().getSimpleName();
    private ScreenIDs.ID mCurrentTab;
    private MiBaseFragment mCurrentFragment;
    private FragmentManager mFragmentManager;
    private long mLastClickTime = 0;
    private List<AlbumModel> albumModelList ;
    private AlbumAdapter mAlbumAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbarTop);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE))
            {


            } else
            {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
            }
        }
        openScreen(ScreenIDs.ID.HOME,AlbumFragment.class,R.id.frameAlbumContent,null,false);
        openScreen(ScreenIDs.ID.HOME,SongFragment.class,R.id.frameSongContent,null,false);

    }

    public void openScreen(final ScreenIDs.ID tab, final Class<? extends MiBaseFragment> fragmentClass, final int frameID, final Bundle bundles, final boolean addToBackStack) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                openScreenBackgroundTask(tab, fragmentClass,frameID, bundles, addToBackStack);
            }


        });

    }
    private void openScreenBackgroundTask(ScreenIDs.ID tab, final Class<? extends MiBaseFragment> fragmentClass,int frameID, Bundle bundles, boolean addToBackStack) {

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
            fragmentTransaction.replace(frameID, mCurrentFragment, tag);
            fragmentTransaction.commitAllowingStateLoss();
            fragmentManager.executePendingTransactions();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void clearBackStack() {
        Log.e(TAG, "clearBackStack() called:" + mFragmentManager.getBackStackEntryCount());
        if (mFragmentManager.getBackStackEntryCount() > 0) {
            FragmentManager.BackStackEntry first = mFragmentManager.getBackStackEntryAt(0);
            boolean didPop = mFragmentManager.popBackStackImmediate(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
            Log.d(TAG, "clearBackStack: didPop " + didPop);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openScreen(ScreenIDs.ID.HOME,AlbumFragment.class,R.id.frameAlbumContent,null,false);
                    openScreen(ScreenIDs.ID.HOME,SongFragment.class,R.id.frameSongContent,null,false);

                } else {
                }
                return;
            }

        }
    }
}
