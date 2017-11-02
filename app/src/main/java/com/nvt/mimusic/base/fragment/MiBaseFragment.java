package com.nvt.mimusic.base.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.nvt.mimusic.view.activity.MiMainActivity;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Admin on 10/16/17.
 */

public abstract class MiBaseFragment extends Fragment {
    protected MiMainActivity mActivity;
    protected Activity mAppContext;
    protected View mView;
    protected Unbinder mUnBinder;
    protected Boolean mIsViewInitialized;
    protected Bundle mBundle;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if( mActivity!= null)
        {
            mAppContext = getActivity();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mActivity = (MiMainActivity) getActivity();
        if (mView == null)
        {
            mView = inflater.inflate(getViewContent(),container,false);
            mUnBinder = ButterKnife.bind(this,mView);
            mIsViewInitialized = false;
        }
        else {
            mIsViewInitialized = true;
            onComeBackFragment(mBundle);
            if (mView.getParent() != null) ;
            {
                ((ViewGroup) mView.getParent()).removeView(mView);
            }
        }
        if (!mIsViewInitialized)
        {
            onInitializeView();
            mIsViewInitialized = true;
        }

        return mView;

    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        mActivity = (MiMainActivity) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


    //Base Fragment Method
    protected abstract void initData();
    protected abstract void initUI();
    protected abstract int getViewContent();
    protected abstract void initControls();
    //Innit View
    public void onInitializeView() {
        initUI();
        initControls();
    }
    //Call Back Fragment Method
    public void onComeBackFragment(Bundle mBundle) {
    }
    //Open Screen Method
    /*protected void openScreen(final ScreenIDs.IDs tab, final Class<? extends GBaseFragment> fragmentClass, final Bundle bundles,
                              final boolean shouldAddToBackstack) {
        if(mActivity != null) mActivity.openScreen(tab, fragmentClass, bundles, shouldAddToBackstack);
    }*/

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }
}
