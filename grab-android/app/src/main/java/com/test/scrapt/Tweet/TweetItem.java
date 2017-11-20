package com.test.scrapt.Tweet;

/**
 * Created by Administrator on 2017/11/10.
 */

public class TweetItem {
    public int tweet = 0;
    public int follow = 0;
    public int fan = 0;

    public void TweetItem(){

    }

    public String getUid() {
        return uid;
    }

    public String uid;

    public TweetItem(String u){
        this.uid = u;
    }

//    public TweetNote TweetConvert
}
