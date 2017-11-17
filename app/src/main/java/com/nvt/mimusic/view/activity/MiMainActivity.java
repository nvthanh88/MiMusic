package com.nvt.mimusic.view.activity;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.nvt.mimusic.R;
import com.nvt.mimusic.utils.PermissionCallback;
import com.nvt.mimusic.utils.PermissionRequest;
import com.nvt.mimusic.view.panel.SlidingUpPanelLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Admin on 11/16/17.
 */

public class MiMainActivity extends MiBaseActivity {
    private final String TAG = getClass().getSimpleName();
    private Context context;
    SlidingUpPanelLayout panelLayout;
    @BindView(R.id.toolbarTop)
    Toolbar toolbarTop;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbarTop);
        PermissionRequest.init(getApplicationContext());
        checkPermissionAndThenLoad();

    }
    final PermissionCallback permissionReadstorageCallback = new PermissionCallback() {
        @Override
        public void permissionGranted() {
            Toast.makeText(getApplicationContext(),"Permission granted",Toast.LENGTH_LONG).show();
        }

        @Override
        public void permissionRefused() {
            finish();
        }
    };
    /**
     * Check permission and load
     * */
    private void checkPermissionAndThenLoad() {
        //check for permission
        if (PermissionRequest.checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Toast.makeText(getApplicationContext(), "Permission is granted", Toast.LENGTH_LONG).show();
        } else {
            if (PermissionRequest.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Snackbar.make(panelLayout, "MiMusic Need READ_EXTERNAL_STORAGE Permission to load  media", Snackbar.LENGTH_LONG)
                        .setAction("OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                PermissionRequest.askForPermission(MiMainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE, permissionReadstorageCallback);
                            }
                        }).show();
            } else {
                PermissionRequest.askForPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE, permissionReadstorageCallback);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionRequest.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
