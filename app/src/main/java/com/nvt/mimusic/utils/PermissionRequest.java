package com.nvt.mimusic.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;

import com.nvt.mimusic.model.PermissionModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Admin on 11/16/17.
 */

public class PermissionRequest {
    private static final String TAG = PermissionRequest.class.getSimpleName();
    private static ArrayList<PermissionModel> permissionRequests = new ArrayList<PermissionModel>();
    private static Context mContext;
    private static SharedPreferences sharedPreferences;
    private static final String KEY_PREV_PERMISSIONS = "previous_permissions";

   /*Save Permission use SharedPreferences*/
    public static void init(Context context) {
        sharedPreferences = context.getSharedPreferences("pl.tajchert.runtimepermissionhelper", Context.MODE_PRIVATE);
        PermissionRequest.mContext = mContext;
    }
    /**
     * If we override other methods, lets do it as well, and keep name same as it is already weird enough.
     * Returns true if we should show explanation why we need this permission.
     */
    public static boolean shouldShowRequestPermissionRationale(Activity activity, String permissions) {
        return activity.shouldShowRequestPermissionRationale(permissions);
    }
    public static void askForPermission(Activity activity, String permission, PermissionCallback permissionCallback) {
        askForPermission(activity, new String[]{permission}, permissionCallback);
    }

    public static void askForPermission(Activity activity, String[] permissions, PermissionCallback permissionCallback) {
        if (permissionCallback == null) {
            return;
        }
        if (hasPermission(activity, permissions)) {
            permissionCallback.permissionGranted();
            return;
        }
        PermissionModel permissionModel = new PermissionModel(new ArrayList<>(Arrays.asList(permissions)), permissionCallback);
        permissionRequests.add(permissionModel);

        activity.requestPermissions(permissions, permissionModel.getRequestCode());
    }
    /**
     * Returns true if the Activity has access to a all given permission.
     */
    public static boolean hasPermission(Activity activity, String[] permissions) {
        for (String permission : permissions) {
            if (activity.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }
    public static boolean verifyPermissions(int[] grantResults) {
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }
    public static void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        PermissionModel requestResult = new PermissionModel(requestCode);
        if (permissionRequests.contains(requestResult)) {
            PermissionModel permissionModel = permissionRequests.get(permissionRequests.indexOf(requestResult));
            if (verifyPermissions(grantResults)) {
                //Permission has been granted
                permissionModel.getPermissionCallback().permissionGranted();
            } else {
                permissionModel.getPermissionCallback().permissionRefused();

            }
            permissionRequests.remove(requestResult);
        }
        refreshMonitoredList();
    }
    /**
     * Refresh currently granted permission list, and save it for later comparing using @permissionCompare()
     */
    public static void refreshMonitoredList() {
        ArrayList<String> permissions = getGrantedPermissions();
        Set<String> set = new HashSet<>();
        for (String perm : permissions) {
            set.add(perm);
        }
        sharedPreferences.edit().putStringSet(KEY_PREV_PERMISSIONS, set).apply();
    }

    /**
     * Get list of currently granted permissions, without saving it inside
     *
     * @return currently granted permissions
     */
    public static ArrayList<String> getGrantedPermissions() {
        if (mContext == null) {
            throw new RuntimeException("Must call init() earlier");
        }
        ArrayList<String> permissions = new ArrayList<String>();
        ArrayList<String> permissionsGranted = new ArrayList<String>();
        //Group location
        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        //Group Calendar
        permissions.add(Manifest.permission.WRITE_CALENDAR);
        permissions.add(Manifest.permission.READ_CALENDAR);
        //Group Camera
        permissions.add(Manifest.permission.CAMERA);
        //Group Contacts
        permissions.add(Manifest.permission.WRITE_CONTACTS);
        permissions.add(Manifest.permission.READ_CONTACTS);
        permissions.add(Manifest.permission.GET_ACCOUNTS);
        //Group Microphone
        permissions.add(Manifest.permission.RECORD_AUDIO);
        //Group Phone
        permissions.add(Manifest.permission.CALL_PHONE);
        permissions.add(Manifest.permission.READ_PHONE_STATE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            permissions.add(Manifest.permission.READ_CALL_LOG);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            permissions.add(Manifest.permission.WRITE_CALL_LOG);
        }
        permissions.add(Manifest.permission.ADD_VOICEMAIL);
        permissions.add(Manifest.permission.USE_SIP);
        permissions.add(Manifest.permission.PROCESS_OUTGOING_CALLS);
        //Group Body sensors
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            permissions.add(Manifest.permission.BODY_SENSORS);
        }
        //Group SMS
        permissions.add(Manifest.permission.SEND_SMS);
        permissions.add(Manifest.permission.READ_SMS);
        permissions.add(Manifest.permission.RECEIVE_SMS);
        permissions.add(Manifest.permission.RECEIVE_WAP_PUSH);
        permissions.add(Manifest.permission.RECEIVE_MMS);
        //Group Storage
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        for (String permission : permissions) {
            if (mContext.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED) {
                permissionsGranted.add(permission);
            }
        }
        return permissionsGranted;
    }
    /**
     * Check Permission
     * */
    public static boolean checkPermission(String permissionName)
    {
        if (mContext == null)
        {
            throw new RuntimeException("Must be init context");
        }
        return PackageManager.PERMISSION_GRANTED == mContext.checkSelfPermission(permissionName);
    }
}
