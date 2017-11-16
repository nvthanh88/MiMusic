package com.nvt.mimusic.view.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import com.nvt.mimusic.R;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Admin on 11/16/17.
 */

public class MiMainActivity extends MiBaseActivity {
    private final String TAG = getClass().getSimpleName();
    @BindView(R.id.toolbarTop)
    Toolbar toolbarTop;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbarTop);

    }

}
