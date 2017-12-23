package zj.zfenlly.Utils;

import android.util.Log;

import java.io.File;

/**
 * Created by Administrator on 2017/12/1.
 */

public class Zlog {


    public static void d(String str) {
        Log.d("ztag", "" + str);
    }

    public static void e(String str) {
        Log.e("ztag", "" + str);
    }

    public static void i(String str) {
        Log.i("ztag", "" + str);
    }

    public static void v(String str) {
        Log.v("ztag", "" + str);
    }

    public static void w(String str) {
        Log.w("ztag", "" + str);
    }

    public static void dFile2String(File file) {
        Log.d("ztag", "file:" + file.toString());
    }

    public static void debug() {
        e("debug");
    }

    public static void trace() {
        Exception e = new Exception("this is a log");
        e.printStackTrace();
    }
}
