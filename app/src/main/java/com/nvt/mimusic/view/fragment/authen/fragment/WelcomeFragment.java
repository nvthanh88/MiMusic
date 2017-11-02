package com.nvt.mimusic.view.fragment.authen.fragment;

import android.widget.Button;

import com.nvt.mimusic.R;
import com.nvt.mimusic.base.fragment.MiBaseFragment;
import com.nvt.mimusic.constant.ScreenIDs;

import butterknife.BindFloat;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Admin on 10/16/17.
 */

public class WelcomeFragment extends MiBaseFragment {
    @BindView(R.id.btnHaveAccount)
    Button btnHaveAccount;
    @BindView(R.id.btnRegister)
    Button btnRegister;
    @Override
    protected void initData() {

    }

    @Override
    protected void initUI() {

    }

    @Override
    protected int getViewContent() {
        return R.layout.fragment_welcome;
    }

    @Override
    protected void initControls() {

    }
    /*@OnClick(R.id.btnHaveAccount)
    public void openLoginFragment(){
        mActivity.openScreen(ScreenIDs.ID.LOGIN,LoginFragment.class,null,false);
    }
    @OnClick(R.id.btnRegister)
    public void openRegisterFragment(){
        mActivity.openScreen(ScreenIDs.ID.REGISTER,RegisterFragment.class,null,false);
    }*/
}
