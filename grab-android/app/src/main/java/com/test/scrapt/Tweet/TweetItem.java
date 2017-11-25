package com.test.scrapt.Tweet;

/**
 * Created by Administrator on 2017/11/10.
 */

public class TweetItem {
    public int tweet = 0;
    public int follow = 0;
    public int fan = 0;
    public String uid;
    public int integral = 0;

    public TweetItem(String u) {
        this.uid = u;
    }

    public void TweetItem() {

    }

    public String toString() {
        return uid + "\nfollow: " + this.follow + " " + " fan: " + this.fan + " tweete: " + this.tweet + " code: " + this.integral;
    }

    public String getUid() {
        return uid;
    }

//    public TweetNote TweetConvert
}
