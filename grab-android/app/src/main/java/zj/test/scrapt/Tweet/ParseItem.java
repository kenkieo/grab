package zj.test.scrapt.Tweet;

/**
 * Created by Administrator on 2017/11/23.
 */

public class ParseItem {

    public static String splitsqrt(String d) {
        int len = d.length();
        int start = d.indexOf("[");
        int end = d.indexOf("]");
        if (start < end) {
            if (start > 0) {
                if (len > 0)
                    return d.substring(start + 1, end);
            }
        }

        return null;
    }
}
