package com.nvt.mimusic.view.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.nvt.mimusic.R;
import com.nvt.mimusic.adapter.AlbumAdapter;
import com.nvt.mimusic.base.fragment.MiBaseFragment;
import com.nvt.mimusic.constant.ScreenIDs;
import com.nvt.mimusic.helper.GridSpacingItemDecoration;
import com.nvt.mimusic.helper.SessionManager;
import com.nvt.mimusic.model.AlbumModel;
import com.nvt.mimusic.view.authen.fragment.WelcomeFragment;
import com.nvt.mimusic.view.home.HomeFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MiMainActivity extends AppCompatActivity {
    @BindView(R.id.toolbarTop)
    Toolbar toolbarTop;

    private String TAG = getClass().getSimpleName();
    private ScreenIDs.ID mCurrentTab;
    private MiBaseFragment mCurrentFragment;
    private FragmentManager mFragmentManager;
    private long mLastClickTime = 0;
    private SessionManager mSessionManager;
    private List<AlbumModel> albumModelList ;
    private AlbumAdapter mAlbumAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbarTop);
        openScreen(ScreenIDs.ID.HOME, HomeFragment.class,null,false);

}

    public void openScreen(final ScreenIDs.ID tab, final Class<? extends MiBaseFragment> fragmentClass, final Bundle bundles, final boolean addToBackStack) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                openScreenBackgroundTask(tab, fragmentClass, bundles, addToBackStack);
            }


        });

    }
    private void openScreenBackgroundTask(ScreenIDs.ID tab, final Class<? extends MiBaseFragment> fragmentClass, Bundle bundles, boolean addToBackStack) {

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
            fragmentTransaction.replace(R.id.frameContent, mCurrentFragment, tag);
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





}
