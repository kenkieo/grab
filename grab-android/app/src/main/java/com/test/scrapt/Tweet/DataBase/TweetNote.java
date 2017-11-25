package com.test.scrapt.Tweet.DataBase;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.NotNull;

/**
 * Created by Administrator on 2017/11/15.
 */
@Entity(indexes = {
        @Index(value = "uid, date DESC", unique = true)
})
public class TweetNote {
    @Id
    private Long id;

    @NotNull
    private String uid;
    private String tweet;
    private String follow;
    private String integral;
    private String fan;
    private String date;
    @Generated(hash = 1027362690)
    public TweetNote(Long id, @NotNull String uid, String tweet, String follow,
            String integral, String fan, String date) {
        this.id = id;
        this.uid = uid;
        this.tweet = tweet;
        this.follow = follow;
        this.integral = integral;
        this.fan = fan;
        this.date = date;
    }
    @Generated(hash = 1042784436)
    public TweetNote() {
    }
    public String getIntegral() {
        return integral;
    }

    public void setIntegral(String integral) {
        this.integral = integral;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Keep
    public String toString() {
        return uid + ":" + follow + ":" + follow + ":" + fan + ":" + date;
    }

    @Keep
    public boolean isEqual(TweetNote tn1) {
        if (tn1 == null) return false;
        if (tn1.getUid().equals(getUid())) {
            if (tn1.getTweet().equals(getTweet())) {
                if (tn1.getFollow().equals(getFollow())) {
                    if (tn1.getFan().equals(getFan())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public String getUid() {

        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTweet() {
        return tweet;
    }

    public void setTweet(String tweet) {
        this.tweet = tweet;
    }

    public String getFollow() {
        return follow;
    }

    public void setFollow(String follow) {
        this.follow = follow;
    }

    public String getFan() {
        return fan;
    }

    public void setFan(String fan) {
        this.fan = fan;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
