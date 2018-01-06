package zj.zfenlly.gua;

import android.util.Log;

/**
 * Created by Administrator on 2017/8/1.
 */

public final class LoadInjectLib {
    static {
//        System.loadLibrary("dvm");
//        System.loadLibrary("substrate-dvm");
//        System.loadLibrary("substrate");
        System.loadLibrary("inject");
    }

    public static void init(String pkg) {
//        int i = injectLib("com.teamlava.castlestory");
        int i = injectLib("com.mine.mirs");

//        int i = injectLib(pkg);
        Log.e("JTAG", "+" + i);
        Log.e("sTAG", "#     " + SystemTime.nanoTime());
    }

    public static native int injectLib(String pkgName);
    public static native void setTime(int minite);
}
