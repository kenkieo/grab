package zj.zfenlly.gua;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import zj.test.scrapt.Tweet.MainActivity;

/**
 * Created by linurm on 2018/2/16.
 */

public class MPermissions {

    public static int request_permission = 0x00099;
    public static final int WRITE_EXTERNAL = 0x0002;

    /**
     * 请求权限
     *
     * @param permissions 请求的权限
     * @param requestCode 请求权限的请求码
     */
    public static void requestPermission(Activity mActivity, String[] permissions, int requestCode) {
        request_permission = requestCode;
        if (checkPermissions(mActivity, permissions)) {
            permissionSuccess(mActivity, request_permission);
        } else {
            List<String> needPermissions = getDeniedPermissions(mActivity, permissions);
            ActivityCompat.requestPermissions(mActivity, needPermissions.toArray(new String[needPermissions.size()]), request_permission);
        }
    }

    public static void requestPermission(Activity mActivity) {

        requestPermission(mActivity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MPermissions.WRITE_EXTERNAL);

    }

    /**
     * 检测所有的权限是否都已授权
     *
     * @param permissions
     * @return
     */
    private static boolean checkPermissions(Activity mActivity, String[] permissions) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }

        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(mActivity, permission) !=
                    PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * 获取权限集中需要申请权限的列表
     *
     * @param permissions
     * @return
     */
    private static List<String> getDeniedPermissions(Activity mActivity, String[] permissions) {
        List<String> needRequestPermissionList = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(mActivity, permission) != PackageManager.PERMISSION_GRANTED || mActivity.shouldShowRequestPermissionRationale(permission)) {
                needRequestPermissionList.add(permission);
            }
        }
        return needRequestPermissionList;
    }


    /**
     * 确认所有的权限是否都已授权
     *
     * @param grantResults
     * @return
     */
    public static boolean verifyPermissions(int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * 获取权限成功
     *
     * @param requestCode
     */
    public static void permissionSuccess(Activity mActivity, int requestCode) {
        Log.d("ztag", "获取权限成功=" + requestCode);

        switch (requestCode) {
            case WRITE_EXTERNAL:
                askForPermission(mActivity);
//                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:13468857714"));
//                startActivity(intent);

                break;
        }
    }

    public static final int OVERLAY_PERMISSION_REQ_CODE = 12335;
    static Intent floatWinIntent = null;

    public static void onResult(Activity mAcitivty, int requestCode) {
        if (requestCode == OVERLAY_PERMISSION_REQ_CODE) {
            if (Settings.canDrawOverlays(mAcitivty)) {
                Log.e("ztag", "permission granted start float win");
                //                    Toast.makeText(this, "权限授予成功！", Toast.LENGTH_SHORT).show();
                //启动FxService
                mAcitivty.startService(floatWinIntent);
            } else {
                Log.e("ztag", "draw overlay permission not granted");
//                    Toast.makeText(this, "权限授予失败，无法开启悬浮窗", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static void askForPermission(Activity mActivity) {
        floatWinIntent = new Intent(mActivity, FloatWinService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(mActivity)) {
                Toast.makeText(mActivity, "当前无权限，请授权！", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + mActivity.getPackageName()));
                Log.e("ztag", "request overlay permission");
                mActivity.startActivityForResult(intent, OVERLAY_PERMISSION_REQ_CODE);
            } else {
                Log.e("ztag", "can draw overlay permission");
                mActivity.startService(floatWinIntent);
            }
        }
    }

    /**
     * 权限获取失败
     *
     * @param requestCode
     */
    public static void permissionFail(int requestCode) {
        Log.d("ztag", "获取权限失败=" + requestCode);
    }


    public static void onPermissionsResult(Activity mActivity, int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == MPermissions.request_permission) {
            if (MPermissions.verifyPermissions(grantResults)) {
                MPermissions.permissionSuccess(mActivity, MPermissions.request_permission);
            } else {
                MPermissions.permissionFail(MPermissions.request_permission);
//                showTipsDialog();
            }
        }
    }
}
