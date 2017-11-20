package com.test.scrapt.Tweet;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Date;

import okhttp3.Cookie;

/**
 * Created by Administrator on 2017/11/16.
 */

public class SerializableCookie implements Serializable {

    private static final long serialVersionUID = 6374381828722046732L;

    private transient final Cookie cookie;
    private transient Cookie clientCookie;

    public SerializableCookie(Cookie cookie) {
        this.cookie = cookie;
    }

    public Cookie getCookie() {
        Cookie bestCookie = cookie;
        if (clientCookie != null) {
            bestCookie = clientCookie;
        }
        return bestCookie;
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeObject(cookie.name());
        out.writeObject(cookie.value());
//        out.writeObject(cookie.getComment());
        out.writeObject(cookie.domain());
//        out.writeObject(cookie.expiryAt());
        out.writeObject(cookie.path());
//        out.writeInt(cookie.getVersion());
        out.writeBoolean(cookie.secure());
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        String name = (String) in.readObject();
        String value = (String) in.readObject();
        long expiresAt = (long) in.readObject();
        String domain = (String) in.readObject();
        String path = (String) in.readObject();
        boolean secure = (((String) in.readObject()).equals("True")) ? true : false;
        boolean httpOnly = (((String) in.readObject()).equals("True")) ? true : false;
        boolean hostOnly = (((String) in.readObject()).equals("True")) ? true : false;
        boolean persistent = (((String) in.readObject()).equals("True")) ? true : false;
        clientCookie = new Cookie.Builder()
                .name(name)
                .value(value)
                .expiresAt(expiresAt)
                .domain(domain)
                .path(path)
                .build();

    }
}
