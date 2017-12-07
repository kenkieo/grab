package zj.test.scrapt.Time;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2017/11/15.
 */

public class NowDataTime {
    public static String  getDataTime() {
        long time = System.currentTimeMillis();//long now = android.os.SystemClock.uptimeMillis();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d1 = new Date(time);
        String t1 = format.format(d1);
        //Log.e("msg", t1);
        return t1;
    }

    public static String  getTime() {
        long time = System.currentTimeMillis();//long now = android.os.SystemClock.uptimeMillis();
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        Date d1 = new Date(time);
        String t1 = format.format(d1);
        //Log.e("msg", t1);
        return t1;
    }
}
