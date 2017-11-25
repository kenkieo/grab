package com.test.scrapt.Tweet;

/**
 * Created by Administrator on 2017/11/23.
 */

public class UserInfo {
    public String uid;
    public int integral = 0;
    public int tweet = 0;
    public int follow = 0;
    public int fan = 0;

    public UserInfo(String uid) {
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

}
