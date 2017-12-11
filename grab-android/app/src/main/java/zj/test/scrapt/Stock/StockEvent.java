package zj.test.scrapt.Stock;


/**
 * Created by Administrator on 2017/11/13.
 */

public class StockEvent {
    public final String message;
    public final boolean clear;
    public final int type;

    public StockEvent(String message, boolean clear) {
        this.message = message;
        this.clear = clear;
        this.type = EventType.MESSAGE_DIS;
    }

    public StockEvent(String message, int type, boolean clear) {
        this.message = message;
        this.clear = clear;
        this.type = type;
    }

    public class EventType {

        public static final int MESSAGE_DIS = 0;
        public static final int COLOR_DIS = 1;

    }
}
