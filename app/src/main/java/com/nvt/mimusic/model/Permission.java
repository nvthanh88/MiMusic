package com.nvt.mimusic.model;

import com.nvt.mimusic.utils.PermissionCallback;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Admin on 11/16/17.
 */

public class Permission {
    private  Random random;
    private  int requestCode;
    private PermissionCallback permissionCallback;
    private ArrayList<String> permissions;

    public Permission(int requestCode) {
        this.requestCode = requestCode;
    }

    public Permission(ArrayList<String> permissions, PermissionCallback permissionCallback) {
        this.permissions = permissions;
        if (random == null)
            random = new Random();
        this.requestCode = random.nextInt();
        this.permissionCallback = permissionCallback;
    }
    public ArrayList<String> getPermissions() {
        return permissions;
    }

    public int getRequestCode() {
        return requestCode;
    }

    public PermissionCallback getPermissionCallback() {
        return permissionCallback;
    }

    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (object instanceof Permission) {
            return ((Permission) object).requestCode == this.requestCode;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return requestCode;
    }
}
