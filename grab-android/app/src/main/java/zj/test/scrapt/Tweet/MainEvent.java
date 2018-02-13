package zj.test.scrapt.Tweet;

/**
 * Created by Administrator on 2017/11/13.
 */

public class MainEvent {
    public final String message;
    public final int type;
    public final boolean clear;

    public MainEvent(String message, boolean clear) {
        this.message = message;
        this.type = EventType.MESSAGE_DIS;
        this.clear = clear;
    }

    public MainEvent(String message, int type, boolean clear) {
        this.message = message;
        this.type = type;
        this.clear = clear;
    }

    public class EventType {

        public static final int MESSAGE_DIS = 0;
        public static final int RECORD_BTN = 1;
        public static final int EDIT_FOCUS = 2;

    }
}

