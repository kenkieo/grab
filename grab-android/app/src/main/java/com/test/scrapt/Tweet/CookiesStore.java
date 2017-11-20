package com.test.scrapt.Tweet;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;

import okhttp3.Cookie;
import okhttp3.internal.http.HttpDate;


/**
 * Created by Administrator on 2017/11/16.
 */

public class CookiesStore {
    private static final String LOG_TAG = "PersistentCookieStore";
    private static final String COOKIE_PREFS = "CookiePrefsFile";
    private static final String COOKIE_NAME_STORE = "names";
    private static final String COOKIE_NAME_PREFIX = "cookie_";
    private final ConcurrentHashMap<String, Cookie> cookies;
    private final SharedPreferences cookiePrefs;
    private boolean omitNonPersistentCookies = false;

    /**
     * Construct a persistent cookie store.
     *
     * @param context Context to attach cookie store to
     */
    public CookiesStore(Context context) {
        cookiePrefs = context.getSharedPreferences(COOKIE_PREFS, 0);
        cookies = new ConcurrentHashMap<String, Cookie>();
        Cookie cookie;
        // Load any previously stored cookies into the store
        String storedCookieNames = cookiePrefs.getString(COOKIE_NAME_STORE, null);
        if (storedCookieNames != null) {
            String[] cookieNames = TextUtils.split(storedCookieNames, ",");
            for (String name : cookieNames) {
                String encodedCookie = cookiePrefs.getString(COOKIE_NAME_PREFIX + name, null);
                if (encodedCookie != null) {
//                    Cookie decodedCookie = decodeCookie(encodedCookie);
//                    if (decodedCookie != null) {
                    ;
                    cookie = parseCookie(encodedCookie);
                    if (cookie.expiresAt() >= (new Date()).getTime()) {
                        cookies.put(name, cookie);
                    }
//                    }
                }
            }

            // Clear out expired cookies
            clearExpired();
        }
    }

    public Cookie parseCookie(String c) {
        String[] t = c.split("; ");
        String name = t[0].split("=")[0];
        String value = t[0].split("=")[1];
        String domain = null;
        String path = "/";
        boolean httponly = false;
        Date date = null;

        int index;
        String first, second;
        for (int i = 1; i < t.length; i++) {
//            Log.e("ZTAG", t[i] + " = ");
//        int i = 0;
            index = t[i].indexOf('=');
            if (index == -1) {
//                Log.e("ZTAG", "" + t[i]);
                if (t[i].equals("httponly")) {
                    httponly = true;
                }
            } else {
//                Log.e("ZTAG", "1: " + t[i].substring(0, index));
                first = t[i].substring(0, index);
//                Log.e("ZTAG", "2: " + t[i].substring(index + 1, t[i].length()));
                second = t[i].substring(index + 1, t[i].length());
                if (first.equals("expires")) {
                    date = HttpDate.parse(second);
                } else if (first.equals("domain")) {
                    domain = second;
                } else if (first.equals("path")) {
                    path = second;
                }
            }
        }
        Cookie cookie = null;
        if (httponly) {
            cookie = new Cookie.Builder()
                    .name(name)
                    .value(value)
                    .expiresAt(date.getTime())
                    .domain(domain)
                    .httpOnly()
                    .build();
        } else {
            cookie = new Cookie.Builder()
                    .name(name)
                    .value(value)
                    .domain(domain)
                    .expiresAt(date==null?0:date.getTime())
                    .build();
        }
        return cookie;
    }

    public void addCookie(Cookie cookie) {
//        if (omitNonPersistentCookies && !cookie.persistent())
//            return;
        String name = cookie.name() + cookie.domain();
        Log.e("ZTAG", "name: " + name);
        Log.e("ZTAG", "expiresAt: " + cookie.expiresAt() + " " + (new Date()).getTime());
        // Save cookie into local store, or remove if expired
        if (cookie.expiresAt() >= (new Date()).getTime()) {
            cookies.put(name, cookie);
        } else {
            cookies.remove(name);
        }

        // Save cookie into persistent store
        SharedPreferences.Editor prefsWriter = cookiePrefs.edit();
        prefsWriter.putString(COOKIE_NAME_STORE, TextUtils.join(",", cookies.keySet()));
        prefsWriter.putString(COOKIE_NAME_PREFIX + name, cookie.toString());
        prefsWriter.commit();
    }

    public int getCookieNum() {
        return cookies.size();
    }

    public void clear() {
        // Clear cookies from persistent store
        SharedPreferences.Editor prefsWriter = cookiePrefs.edit();
        for (String name : cookies.keySet()) {
            prefsWriter.remove(COOKIE_NAME_PREFIX + name);
        }
        prefsWriter.remove(COOKIE_NAME_STORE);
        prefsWriter.commit();

        // Clear cookies from local store
        cookies.clear();
    }


    public boolean clearExpired() {
        boolean clearedAny = false;
        SharedPreferences.Editor prefsWriter = cookiePrefs.edit();

        for (ConcurrentHashMap.Entry<String, Cookie> entry : cookies.entrySet()) {
            String name = entry.getKey();
            Cookie cookie = entry.getValue();
            if (cookie.expiresAt() < (new Date()).getTime()) {
                // Clear cookies from local store
                cookies.remove(name);
                Log.e("ZTAG", "expires remove cookies ");
                // Clear cookies from persistent store
                prefsWriter.remove(COOKIE_NAME_PREFIX + name);

                // We've cleared at least one
                clearedAny = true;
            }
        }

        // Update names in persistent store
        if (clearedAny) {
            prefsWriter.putString(COOKIE_NAME_STORE, TextUtils.join(",", cookies.keySet()));
        }
        prefsWriter.commit();

        return clearedAny;
    }

    public List<Cookie> getCookies() {
        return new ArrayList<Cookie>(cookies.values());
    }

    /**
     * Will make PersistentCookieStore instance ignore Cookies, which are non-persistent by
     * signature (`Cookie.isPersistent`)
     *
     * @param omitNonPersistentCookies true if non-persistent cookies should be omited
     */
    public void setOmitNonPersistentCookies(boolean omitNonPersistentCookies) {
        this.omitNonPersistentCookies = omitNonPersistentCookies;
    }

    /**
     * Non-standard helper method, to delete cookie
     *
     * @param cookie cookie to be removed
     */
    public void deleteCookie(Cookie cookie) {
        String name = cookie.name();
        cookies.remove(name);
        SharedPreferences.Editor prefsWriter = cookiePrefs.edit();
        prefsWriter.remove(COOKIE_NAME_PREFIX + name);
        prefsWriter.commit();
    }

    /**
     * Serializes Cookie object into String
     *
     * @param cookie cookie to be encoded, can be null
     * @return cookie encoded as String
     */
    protected String encodeCookie(SerializableCookie cookie) {
        if (cookie == null)
            return null;
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(os);
            outputStream.writeObject(cookie);
        } catch (Exception e) {
            return null;
        }

        return byteArrayToHexString(os.toByteArray());
    }

    /**
     * Returns cookie decoded from cookie string
     *
     * @param cookieString string of cookie as returned from http request
     * @return decoded cookie or null if exception occured
     */
    protected Cookie decodeCookie(String cookieString) {
        byte[] bytes = hexStringToByteArray(cookieString);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        Cookie cookie = null;
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            cookie = ((SerializableCookie) objectInputStream.readObject()).getCookie();
        } catch (Exception exception) {
            Log.d(LOG_TAG, "decodeCookie failed", exception);
        }

        return cookie;
    }

    /**
     * Using some super basic byte array <-> hex conversions so we don't have to rely on any
     * large Base64 libraries. Can be overridden if you like!
     *
     * @param bytes byte array to be converted
     * @return string containing hex values
     */
    protected String byteArrayToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte element : bytes) {
            int v = element & 0xff;
            if (v < 16) {
                sb.append('0');
            }
            sb.append(Integer.toHexString(v));
        }
        return sb.toString().toUpperCase(Locale.US);
    }

    /**
     * Converts hex values from strings to byte arra
     *
     * @param hexString string of hex-encoded values
     * @return decoded byte array
     */
    protected byte[] hexStringToByteArray(String hexString) {
        int len = hexString.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4) + Character.digit(hexString.charAt(i + 1), 16));
        }
        return data;
    }
}
