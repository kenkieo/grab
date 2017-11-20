package com.test.scrapt.Tweet;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Administrator on 2017/11/10.
 */

public class ShareP {
    static final String UID = "1772392290";

    public static void saveItemToPref(Context context, TweetItem item) {
        SharedPreferences sp = context.getSharedPreferences("items", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("tweet", item.tweet);
        editor.putInt("follow", item.follow);
        editor.putInt("fan", item.fan);
        editor.commit();
    }

    public static String getUIDFromPref(Context context) {
        SharedPreferences sp = context.getSharedPreferences("items", MODE_PRIVATE);
        String s = sp.getString("uid", UID);
        return s;
    }

    public static TweetItem getItemFromPref(Context context) {
        SharedPreferences sp = context.getSharedPreferences("items", MODE_PRIVATE);
        String uid = sp.getString("uid", UID);
        TweetItem item = new TweetItem(uid);
        item.tweet = sp.getInt("tweet", 0);
        item.follow = sp.getInt("follow", 0);
        item.fan = sp.getInt("fan", 0);
        return item;
    }
}
