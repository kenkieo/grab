package zj.test.scrapt.Tweet;

/**
 * Created by Administrator on 2017/11/13.
 */

public class MainEvent {
    public final String message;
    public final boolean clear;

    public MainEvent(String message, boolean clear) {
        this.message = message;
        this.clear = clear;
    }
}

