package com.strider.desafio.gerenciamentotarefas.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by amanda on 01/06/18.
 */

public class Permission {
    int checkPermission;
    String permissionName;
    int permissionCode;
    boolean hasPermission = true;

    public static int REQUEST_PERMISSION_CAMERA = 1;
    public static int REQUEST_PERMISSION_READ_EXTERNAL_STORAGE = 2;
    public static int WRITE_EXTERNAL_STORAGE = 3;

    public Permission() {
    }

    public boolean checkPermission(Context context, String permissionName) {
        boolean hasPermission = true;
        //se adaptadorListas API do dispositivo for igual ou superior adaptadorTodasOfertas 23 precisa solicitar permissão
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermission = ContextCompat.checkSelfPermission(context, permissionName);
            if (checkPermission != PackageManager.PERMISSION_GRANTED) {
                hasPermission = false;
            }
        }
        return hasPermission;
    }

    public void requestPermissions(Activity activity, int permissionCode, String[] permissionsList) {
        ActivityCompat.requestPermissions(activity, permissionsList, permissionCode);
    }



    public String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }

    public int getPermissionCode() {
        return permissionCode;
    }

    public void setPermissionCode(int permissionCode) {
        this.permissionCode = permissionCode;
    }

    public boolean isHasPermission() {
        return hasPermission;
    }

    public void setHasPermission(boolean hasPermission) {
        this.hasPermission = hasPermission;
    }
}
