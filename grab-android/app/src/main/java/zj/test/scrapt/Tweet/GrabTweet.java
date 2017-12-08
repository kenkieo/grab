package zj.test.scrapt.Tweet;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import org.greenrobot.eventbus.EventBus;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import zj.test.scrapt.Time.NowDataTime;
import zj.test.scrapt.Tweet.DataBase.DaoMaster;
import zj.test.scrapt.Tweet.DataBase.DaoSession;
import zj.test.scrapt.Tweet.DataBase.DataBaseImpl;
import zj.test.scrapt.Tweet.DataBase.TweetNote;

/**
 * Created by Administrator on 2017/11/9.
 */

public class GrabTweet {
    public static final MediaType MEDIA_TYPE_MARKDOWN
            = MediaType.parse("text/x-markdown; charset=utf-8");
    public Context mContext;
    CookiesStore mCookiesStore;
    private OkHttpClient client = new OkHttpClient().newBuilder().cookieJar(new CookieJar() {
//        private final HashMap<String, List<Cookie>> cookieStore = new HashMap<>();

        @Override
        public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {

            Cookie c;

            if (url.toString().equals("https://passport.weibo.cn/sso/login")) {
//                cookieStore.put(url.scheme(), cookies);
                for (Cookie cookie : cookies) {
                    mCookiesStore.addCookie(cookie);
//                    Log.e("ZTAGA", "B:" + cookie.toString());
//                    cookie = mCookiesStore.parseCookie(cookie.toString());
//                    Log.e("ZTAGA", "A:" + cookie.toString());
                }
                Log.e("ZTAG", "saveFromResponse:" + url.toString());
            }
//            for (int i = 0; i < cookies.size(); i++) {
//                c = cookies.get(i);
//                Log.e("ZTAG", "saveFromResponse:" + c.toString());
//            }
//            Log.e("ZTAG", "saveFromResponse:" + cookies.toString() + url.scheme());
        }

        @Override
        public List<Cookie> loadForRequest(HttpUrl url) {
            List<Cookie> cookies = null;//cookieStore.get(url.scheme());
            cookies = mCookiesStore.getCookies();
            Log.e("ZTAG", "loadForRequest:" + ((cookies != null) ? cookies.toString() : "cookies null"));
            return cookies != null ? cookies : new ArrayList<Cookie>();
        }
    }).build();
    private String COOKIE_SET = "COOKIE_SET";

    public GrabTweet(Context context) {
        mContext = context;
        mCookiesStore = new CookiesStore(context);
    }

    public static byte[] readInputStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        byte[] data = outStream.toByteArray();
        outStream.close();
        inStream.close();
        return data;
    }

    public TweetNote TweetItemConvert(TweetItem items) {
        String time = NowDataTime.getDataTime();
        TweetNote t = new TweetNote();
        t.setUid(items.getUid());
        t.setTweet(items.tweet + "");
        t.setFollow(items.follow + "");
        t.setFan(items.fan + "");
        t.setIntegral(items.integral + "");
        t.setDate(time);

        return t;
    }

    private TweetNote getTweetFromDB(String uid) {
        SQLiteDatabase db;
        DaoMaster daoMaster;
        DaoSession daoSession;
        if (uid == null) return null;
        TweetNote tn = DataBaseImpl.getTweet(mContext, uid);

        return tn;
    }

    private void SetTweetFromDB(TweetNote tn, TweetNote tdb) {
        if (tdb != null) {
            DataBaseImpl.delete(mContext, tdb);
        }
        DataBaseImpl.insert(mContext, tn);

    }

    String printDiff(String t, String tdb) {
        int pre, now;
        String s = "";
        pre = Integer.parseInt(tdb);
        now = Integer.parseInt(t);

        if (pre == now) {
            s += pre + "";
        } else if (pre > now) {
            s += pre + "-" + (pre - now);
        } else if (now > pre) {
            s += pre + "+" + (now - pre);
        }

        return s;
    }

    public String getUserInfo(String uid) {
        String s;
        TweetItem items = new TweetItem.Builder().setUid(uid).build();
        String spec = "https://weibo.com/p/100505" + uid + "/info?mod=pedit_more";
        try {
            URL url = new URL(spec);
            s = getHtml(url);
            Document d = Jsoup.parse(s);
            s = ParseTweetItem.parse(items, d);
            TweetNote t = TweetItemConvert(items);
            TweetNote tdb = getTweetFromDB(uid);
            s = items.toString() + "\n";
            if (t.isEqual(tdb) == false) {
                s = s + ((tdb == null) ? "-" : printDiff(t.getFollow(), tdb.getFollow())) + "  " +
                        ((tdb == null) ? "-" : printDiff(t.getFan(), tdb.getFan())) + "  " +
                        ((tdb == null) ? "-" : printDiff(t.getTweet(), tdb.getTweet())) + "  " +
                        ((tdb == null) ? "-" : printDiff(t.getIntegral(), tdb.getIntegral())) + "  Update !!!";
                if ((tdb == null) || (!(t.getFollow().equals("0") || t.getFan().equals("0") || t.getTweet().equals("0") || t.getIntegral().equals("0")))) {
                    SetTweetFromDB(t, tdb);
                    Log.e("ZTAG", "set to DB");
                }
                if (!t.getIntegral().equals("0")) {
//                    ((MainActivity) (mContext)).record_btn.setVisibility(View.VISIBLE);
                    EventBus.getDefault().post(new MainEvent(s, MainEvent.EventType.RECORD_BTN, true));
                }
            }
//            EventBus.getDefault().post(new MainEvent(s, 1, false));
//            Log.e("ZTAG", "getUserInfo getUserInfo getUserInfo" + s);
        } catch (Exception e) {
            e.printStackTrace();
            s = e.getMessage();

        }

        s += "\n\n";
        return s;
    }


    protected String getCookie() {
        CookieSyncManager.createInstance(mContext);
        CookieManager cookieManager = CookieManager.getInstance();
        if (cookieManager.hasCookies()) {
            Log.e("ZTAG", "has cookies");
        }
        String cookie = cookieManager.getCookie(COOKIE_SET);
        CookieSyncManager.getInstance().sync();
        if (cookie != null) {
            return cookie;
        } else {
            return "";
        }
    }

    protected void setCookie(String cookie) {
        CookieSyncManager.createInstance(mContext);
        CookieManager cookieManager = CookieManager.getInstance();
//        if(cookieManager.acceptCookie()){
//            Log.e("ZTAG", "accept cookie: ");
//        }
//        cookieManager.
        cookieManager.setCookie(COOKIE_SET, cookie);
//        cookieManager.flush();
        CookieSyncManager.getInstance().sync();
    }

    protected void setCookies(Map<String, List<String>> headerFields) {
        if (null == headerFields) {
            return;
        }
        List<String> cookies = headerFields.get("Set-Cookie");
        if (null == cookies) {
            return;
        }
        for (String cookie : cookies) {
            String z = cookie.split(";")[0];
            Log.e("ZTAG", "set cookie: " + z);
            setCookie(z);
//            break;
        }
    }

    public String getHtml(URL url) {

        try {
//            OkHttpClient mOkHttpClient = new OkHttpClient();
            Request request = new Request.Builder().url(url).build();
            Response response = client.newCall(request).execute();

            if (response.isSuccessful()) {
                String s = response.body().string();


                final String result = s;
                return result;
            } else {
                throw new IOException("Unexpected code " + response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    public String getHtml_bak(URL url) {

        try {
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (compatible;MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0)");
            String cookies = getCookie();
            urlConnection.setRequestProperty("Cookie", cookies);
            Log.e("ZTAG", "get Cookie:" + cookies);
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            OutputStream os = urlConnection.getOutputStream();
            os.flush();
            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = urlConnection.getInputStream();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();

                int len = 0;
                byte buffer[] = new byte[1024];

                while ((len = is.read(buffer)) != -1) {
                    baos.write(buffer, 0, len);
                }
                is.close();
                baos.close();

                final String result = new String(baos.toByteArray(), "utf-8");
                return result;
            } else {
                return "error";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }


    public String login(String usr, String pwd) {
        String s;
        String userName = usr;
        String userPwd = pwd;
        boolean hasFresh = false;

        if (!ShareP.getUidFromPref(mContext).equals(userName)) {
            hasFresh = true;
        }
        if (!ShareP.getPwdFromPref(mContext).equals(userPwd)) {
            hasFresh = true;
        }
//        ShareP.setUidToPref(MainActivity.this, uid);
//        ShareP.setPwdToPref(MainActivity.this, pwd);
        int cookieNum = mCookiesStore.getCookieNum();

        if (hasFresh == false) {
            Log.e("ZTAG", "have cookies: " + cookieNum);
            if (cookieNum == 4) {
                return "has cookies " + cookieNum + "\n\n\n";
            }
        }
        mCookiesStore.clear();
        try {
            String data = "username=" + URLEncoder.encode(userName, "UTF-8")
                    + "&password=" + URLEncoder.encode(userPwd, "UTF-8")
                    + "&savestate=1&r=&ec=0&pagerefer=&entry=mweibo&wentry=&loginfrom=&client_id=&code=&qq=&mainpageflag=1&hff=&hfp=";
            RequestBody formBody = new FormBody.Builder()
                    .add("username", userName)
                    .add("password", userPwd)
                    .add("savestate", "1")
                    .add("mainpageflag", "1")
                    .add("ec", "Jurassic Park")
                    .add("pagerefer", "")
                    .add("entry", "mweibo")
                    .build();

            Request request = new Request.Builder()
                    .header("Accept", "*/*")
                    .addHeader("User-Agent", "Mozilla/5.0 (compatible;MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0)")
                    .addHeader("Referer", "https://passport.weibo.cn/signin/login_bak")
                    .addHeader("Accept-Encoding", "gzip,deflate")
                    .addHeader("Accept-Language", "zh-cn")
                    .addHeader("Connection", "keep-alive")
                    .addHeader("Content-Length", String.valueOf(data.getBytes().length))
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .addHeader("Host", "passport.weibo.cn")
                    .url("https://passport.weibo.cn/sso/login")
                    .post(formBody)
                    .build();
//            Log.e("ZTAG", "==============data: " + data);
            Log.e("ZTAG", "request success: " + request.headers());
            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            s = response.code() + "";

//            s = "get cookies";
            ShareP.setUidToPref(mContext, userName);
            ShareP.setPwdToPref(mContext, userPwd);
            Log.e("ZTAG", "response success: " + s);

        } catch (Exception e) {
            e.printStackTrace();
            s = e.getMessage();
        }
        return s + "\n";
    }


}
