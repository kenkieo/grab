package com.test.scrapt.Tweet;

/**
 * Created by Administrator on 2017/11/13.
 */

public class MessageEvent {
    public final String message;
    public final boolean clear;

    public MessageEvent(String message,boolean clear) {
        this.message = message;
        this.clear = clear;
    }
}

