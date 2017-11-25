package com.test.scrapt.Tweet;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Administrator on 2017/11/10.
 */

public class ShareP {

    public static void saveItemToPref(Context context, TweetItem item) {
        SharedPreferences sp = context.getSharedPreferences("items", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("tweet", item.tweet);
        editor.putInt("follow", item.follow);
        editor.putInt("fan", item.fan);
        editor.commit();
    }

    public static String getUidFromPref(Context context) {
        SharedPreferences sp = context.getSharedPreferences("items", MODE_PRIVATE);
        String s = sp.getString("uid", "");
        return s;
    }

    public static String getPwdFromPref(Context context) {
        SharedPreferences sp = context.getSharedPreferences("items", MODE_PRIVATE);
        String s = sp.getString("pwd", "");
        return s;
    }

    public static TweetItem getItemFromPref(Context context) {
        SharedPreferences sp = context.getSharedPreferences("items", MODE_PRIVATE);
        String uid = sp.getString("uid", "");
        TweetItem item = new TweetItem(uid);
        item.tweet = sp.getInt("tweet", 0);
        item.follow = sp.getInt("follow", 0);
        item.fan = sp.getInt("fan", 0);
        return item;
    }

    public static void setUidToPref(Context context, String uid) {
        SharedPreferences sp = context.getSharedPreferences("items", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("uid", uid);
        editor.commit();
    }

    public static void setPwdToPref(Context context, String pwd) {
        SharedPreferences sp = context.getSharedPreferences("items", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("pwd", pwd);
        editor.commit();
    }
}
