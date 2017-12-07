package zj.test.scrapt.Stock;

/**
 * Created by Administrator on 2017/11/13.
 */

public class StockEvent {
    public final String message;
    public final boolean clear;

    public StockEvent(String message, boolean clear) {
        this.message = message;
        this.clear = clear;
    }
}
