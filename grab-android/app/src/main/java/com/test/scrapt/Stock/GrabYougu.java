package com.test.scrapt.Stock;

import android.content.Context;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/11/9.
 */

public class GrabYougu {
    public Context mContext;

    public GrabYougu(Context context) {
        mContext = context;
    }




    public String cutString(String s, String start, String end) {
        String s1 = s.substring(s.indexOf(start), s.lastIndexOf(end) + 1);
        return s1;
    }

    public String getUserTrade() {
        String s = "";
        String spec = "http://mncg.youguu.com/youguu/trade/conclude/query?matchid=~9FJ&fromtid=~2D3&reqnum=~3Lh-&uid=~5KANyJvN7&version=1&youguu_random=765913f86c2276868dd334f15b793961";
        try {
            URL url = new URL(spec);
            OkHttpClient mOkHttpClient = new OkHttpClient();
            Request request = new Request.Builder().url(url).build();
            Response response = mOkHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                s = response.body().string();
                JSONObject jsonObject = new JSONObject(s);
                s = jsonObject.getJSONArray("result").toString();
//                s = cutString(s,"{","}");
                JSONArray jsonArray = new JSONArray(s);
                Log.e("ZTAG", "----" + jsonArray.length());
                String r;
                StringBuffer ss = new StringBuffer();
                for (int i = 0; i < jsonArray.length(); i++) {
                    s = jsonArray.get(i).toString();

                    jsonObject = new JSONObject(s);
                    s = jsonObject.getString("shareText") + "\n";
                    ss.append("\n").append(s);
                    r = jsonObject.getString("content");
                    r = cutString(r,"<",">");
                    String[] temp = r.split(" ");
                    r = temp[3];
                    ss.append(r).append("\n");
                    s = s + r;
//                    Log.e("ZTAG", "" + s);
                }
                s = ss.toString();
            } else {
                throw new IOException("Unexpected code " + response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            s = e.getMessage();
        }

        return s;
    }


}
