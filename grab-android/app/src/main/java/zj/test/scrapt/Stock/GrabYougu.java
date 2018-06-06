package zj.test.scrapt.Stock;

import android.content.Context;
import android.util.Log;

import com.jhss.youguu.common.e.g;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/11/9.
 */

public class GrabYougu {
    private final long TIME_EXPRERS = 2 * 7 * 24 * 60 * 60 * 1000;
    public Context mContext;
    HashMap<String, UserInfo> mp;

    private static GrabYougu instance = null;


    public static GrabYougu newInstance() {
        if (null == instance) {
            instance = new GrabYougu();
        }
        return instance;
    }

    public GrabYougu() {
        mp = new HashMap<String, UserInfo>();
    }

    public GrabYougu setContext(Context context) {
        mContext = context;
        return this;
    }


    public String cutString(String s, String start, String end) {
        String s1 = s.substring(s.indexOf(start), s.lastIndexOf(end) + 1);
        return s1;
    }

    public String getJsonArray(String s, String name) {
        try {
            JSONObject jsonObject = new JSONObject(s);
            if (!jsonObject.has(name)) return null;
            String a = jsonObject.getJSONArray(name).toString();
            return a;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getJsonObject(String s, String name) {
        try {
            JSONObject jsonObject = new JSONObject(s);

            if (jsonObject == null) return null;
            if (!jsonObject.has(name)) return null;
            String a = jsonObject.getString(name);
            return a;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }


    }

    public List<UserInfo> getListUser() {
        List<UserInfo> s = new ArrayList<UserInfo>();

        for (String id : mp.keySet()) {
            s.add(mp.get(id));
        }
//        s.add(mp.get("446348"));
        return s;
    }

    private String getUrlResponse(String spec) {
        String s = "";
        try {
            URL url = new URL(spec);
            OkHttpClient mOkHttpClient = new OkHttpClient();
            Request request = new Request.Builder().url(url).build();
            Response response = mOkHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                String resp = response.body().string();
                if (resp == null) {
                    return s;
                }
                return resp;
            }
        } catch (Exception e) {
            e.printStackTrace();
            s = e.getMessage();
        }

        return s;
    }

    public void getAttUsers() {
        String a = "http://user.youguu.com/jhss/member/newQueryFollows?";
        try {
            a += "uid=" + g.a("1628501".getBytes("UTF-8"));
            a += "&fromId=" + g.a("0".getBytes("UTF-8"));
            a += "&reqNum=" + g.a("100".getBytes("UTF-8"));
            Log.w("ZTAG", "getAttUsers:" + a);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        String spec;
        // = "http://mncg.youguu.com/youguu/trade/conclude/query?matchid=~9FJ&fromtid=~2D3&reqnum=~3Lh-&uid=~5KANyJvN7&version=1&youguu_random=765913f86c2276868dd334f15b793961";
        spec = a;
        try {
            String resp = getUrlResponse(spec);
            String result = getJsonObject(resp, "result");
            String userList = getJsonArray(result, "userList");
            JSONArray jsonArray = new JSONArray(userList);
            int l = jsonArray.length();
            for (int i = 0; i < l; i++) {
                String b = jsonArray.get(i).toString();
                JSONObject jsonObject2 = new JSONObject(b);
                String nickName = jsonObject2.getString("nickName");
                int id = jsonObject2.getInt("userId");
                UserInfo mUserInfo = new UserInfo(id);
                mUserInfo.setNickname(nickName);
                mp.put(id + "", mUserInfo);
            }
//            String followList = getJsonArray(result, "followList");
//            jsonArray = new JSONArray(followList);
//            l = jsonArray.length();

//            for (int i = 0; i < l; i++) {
//                String ja = jsonArray.get(i).toString();
//                JSONObject jsonObject2 = new JSONObject(ja);
//                String profitRate = jsonObject2.getString("profitRate");
//                int id = jsonObject2.getInt("uid");
//                UserInfo mUserInfo = (UserInfo) mp.get(id + "");
//                mUserInfo.setProfitRate(profitRate);
//                mp.put(id + "", mUserInfo);
//            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        return;
    }


    public String getAttUserInfo(String uid) {
        String a = "";
        String s = "";
        a = "http://mncg.youguu.com/youguu/rating/score?";
        try {

            a += "userid=" + g.a(uid.getBytes("UTF-8"));
            a += "&matchid=" + g.a("1".getBytes("UTF-8"));
            Log.w("ZTAG", "getAttUserInfo:" + a);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String spec = a;
        try {
            String resp = getUrlResponse(spec);
            Log.d("ZTAG", resp);
            String result = getJsonObject(resp, "result");

            if (result == null) {
                return s;
            }

            JSONObject jsonObject = new JSONObject(result);
            if (jsonObject == null) {
                return s;
            }

            UserInfo mUserInfo = mp.get(uid);
            Log.e("ZTAG", jsonObject.getString("nick") + " rrr");
            mUserInfo.setNickname(jsonObject.getString("nick"))
                    .setAccuracy((int) jsonObject.get("accuracy"))
                    .setProfitability(jsonObject.getString("profitability"))
                    .setRatingGrade(jsonObject.getString("ratingGrade"))
                    .setTotalDays((int) jsonObject.get("totalDays"))
                    .setTotalScore(jsonObject.getString("totalScore"))
                    .setStability((int) jsonObject.get("stability"))
                    .setAccountId(jsonObject.getString("accountId"));
            mp.put(uid, mUserInfo);


        } catch (Exception e) {
            e.printStackTrace();
            s = e.getMessage();
        }

        return s;
    }

    public String getAttUserInfo2(String uid) {
        String a = "";
        String s = "";
        a = "http://mncg.youguu.com/youguu/position/closed/stat?";
        try {
            a += "userid=" + g.a(uid.getBytes("UTF-8"));
            a += "&matchid=" + g.a("1".getBytes("UTF-8"));
            Log.w("ZTAG", "getAttUserInfo2:" + a);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String spec = a;
        try {
            String resp = getUrlResponse(spec);
            String result = getJsonObject(resp, "result");
            Log.i("ZTAG", mp.get(uid).nickname);
            Log.i("ZTAG", "" + result);
            if (result == null) {
                return s;
            }

            JSONObject jsonObject = new JSONObject(result);
            if (jsonObject == null) {
                return s;
            }


            UserInfo mUserInfo = mp.get(uid);
            mUserInfo.setAvgDays(jsonObject.getString("avgDays"))
                    .setAvgProfit(jsonObject.getString("avgProfit"))
                    .setCloseNum(jsonObject.getString("closeNum"))
                    .setSucNum(jsonObject.getString("sucNum"))
                    .setSucRate(jsonObject.getString("sucRate"))
                    .setTotalDays2((int) jsonObject.get("totalDays"))
                    .setTotalStock(jsonObject.getString("totalStock"))
                    .setTradingFrequency(jsonObject.getString("tradingFrequency"));
            mp.put(uid, mUserInfo);


        } catch (Exception e) {
            e.printStackTrace();
            s = e.getMessage();
        }

        return s;
    }

    public String getAttUserInfo3(String uid) {
        String s = "";
        String a = "http://mncg.youguu.com/youguu/simtrade/showmyrank/";

        try {
            a += g.a("0530010010921".getBytes("UTF-8")) + "/";
            a += g.a("201712011138191628501".getBytes("UTF-8")) + "/";
            a += g.a(uid.getBytes("UTF-8")) + "/";
            a += g.a("1".getBytes("UTF-8"));
            Log.w("ZTAG", "getAttUserInfo3:" + a);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String spec = a;
        try {
            String resp = getUrlResponse(spec);
            a = com.jhss.youguu.common.e.g.a(resp);
            Log.v("ZTAG", "" + a);

            JSONObject jsonObject = new JSONObject(a);
            if (jsonObject == null) {
                return s;
            }


            UserInfo mUserInfo = mp.get(uid);
            mUserInfo.settRank(jsonObject.getString("tRank"))
                    .setmRise(jsonObject.getString("mRise"))
                    .setwProfit(jsonObject.getString("wProfit"))
                    .setmRank(jsonObject.getString("mRank"))
                    .setStatus(jsonObject.getString("status"))
                    .settProfit(jsonObject.getString("tProfit"))
                    .settRise(jsonObject.getString("tRise"))
                    .setmProfit(jsonObject.getString("mProfit"))
                    .setwRise(jsonObject.getString("wRise"))
                    .setwRank(jsonObject.getString("wRank"));
            mp.put(uid, mUserInfo);


        } catch (Exception e) {
            e.printStackTrace();
            s = e.getMessage();
        }

        return s;
    }

    public String getUserTrade(String uid) {
        String a = "";
        String s = "";
        a = "http://mncg.youguu.com/youguu/trade/conclude/query?";
        try {
            a += "matchid=" + g.a("1".getBytes("UTF-8"));
            a += "&fromtid=" + g.a("0".getBytes("UTF-8"));
            a += "&reqnum=" + g.a("20".getBytes("UTF-8"));
            a += "&uid=" + g.a(uid.getBytes("UTF-8"));
            a += "&version=1";
            Log.w("ZTAG", "getUserTrade:" + a);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String spec = a;
        try {
            String resp = getUrlResponse(spec);
            String result = getJsonArray(resp, "result");
            if (result == null) return s;
            JSONArray jsonArray = new JSONArray(result);
            String r;
            StringBuffer ss = new StringBuffer();
            long nowtime = System.currentTimeMillis();
            for (int i = 0; i < jsonArray.length(); i++) {
                s = jsonArray.get(i).toString();
                JSONObject jsonObject = new JSONObject(s);
                long l = jsonObject.getLong("ctime");
                l = nowtime - l;
                if (l >= TIME_EXPRERS)
                    break;
                s = jsonObject.getString("shareText") + "\n";
                ss.append("\n").append(s);
                r = jsonObject.getString("content");
                r = cutString(r, "<", ">");
                String[] temp = r.split(" ");
                r = temp[3];
                ss.append(r).append("\n");
                s = s + r;
            }
            s = ss.toString();

        } catch (Exception e) {
            e.printStackTrace();
            s = e.getMessage();
        }

        return s;
    }

    public String getUserTradeNoTime(String uid) {
        String a = "";
        String s = "";
        String RED_COLOR = "<font color=\'#ff0000\'>";
        String BLUE_COLOR = "<font color=\'#0000ff\'>";
        String GREEN_COLOR = "<font color=\'#00ff00\'>";
        String VIOLET_COLOR = "<font color=\'#9966cc\'>";
        String CORAL3_COLOR = "<font color=\'#cd5b45\'>";
        String COLOR_START = RED_COLOR;
        String COLOR_END = "</font>";

        a = "http://mncg.youguu.com/youguu/trade/conclude/query?";
        try {
            a += "matchid=" + g.a("1".getBytes("UTF-8"));
            a += "&fromtid=" + g.a("0".getBytes("UTF-8"));
            a += "&reqnum=" + g.a("100".getBytes("UTF-8"));
            a += "&uid=" + g.a(uid.getBytes("UTF-8"));
            a += "&version=1";
            Log.w("ZTAG", "getUserTrade:" + a);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String spec = a;
        try {
            String resp = getUrlResponse(spec);
            String result = getJsonArray(resp, "result");
            if (result == null) return s;
            JSONArray jsonArray = new JSONArray(result);
            String r;
            StringBuffer ss = new StringBuffer();
            long nowtime = System.currentTimeMillis();
            for (int i = 0; i < jsonArray.length(); i++) {
                s = jsonArray.get(i).toString();
                JSONObject jsonObject = new JSONObject(s);
                long l = jsonObject.getLong("ctime");
                l = nowtime - l;
                s = jsonObject.getString("shareText") + "\n";
//                s = jsonObject.getString("content") + "\n";
                //s = s.split("以")[0] + "\n" + s.split("以")[1];
                if (s.contains("买入")) {
                    COLOR_START = RED_COLOR;
                    s = s.split("买入")[0] + "<br>" + COLOR_START + "买入 " + s.split("买入")[1] + COLOR_END + "<br>";
                }
                if (s.contains("卖出")) {
                    COLOR_START = BLUE_COLOR;
                    s = s.split("卖出")[0] + "<br>" + COLOR_START + "卖出 " + s.split("卖出")[1] + COLOR_END + "<br>";
                }
                ss.append("<br>").append(s);
                r = jsonObject.getString("content");
                r = cutString(r, "<", ">");
                String[] temp = r.split(" ");
                r = temp[3];
                ss.append(r).append("<br>");
                s = s + r;
            }
            s = ss.toString();

        } catch (Exception e) {
            e.printStackTrace();
            s = e.getMessage();
        }

        return s;
    }


}
