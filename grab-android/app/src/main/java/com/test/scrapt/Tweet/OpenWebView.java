package com.test.scrapt.Tweet;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * Created by Administrator on 2017/11/10.
 */

public class OpenWebView {

    public static void open(Context mContext,String url) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(url);
        intent.setData(content_url);
        mContext.startActivity(intent);
    }
}
