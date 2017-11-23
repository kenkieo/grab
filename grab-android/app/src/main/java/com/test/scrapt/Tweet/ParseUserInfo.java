package com.test.scrapt.Tweet;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


/**
 * Created by Administrator on 2017/11/23.
 */

public class ParseUserInfo extends ParseItem {

    public static String parse(Document document) {
        Elements e = document.select("script");

        String s = e.text();
        String n = null;
        Element t;
        int start = 0;
        int last = 0;
        for (int i = 0; i < e.size(); i++) {
            t = e.get(i);
            s = t.toString();
            if (s.contains("\"domid\":\"Pl_Official_RightGrowNew__57\"")) {
                start = s.indexOf("{") + 1;
                last = s.lastIndexOf("}");
                s = s.substring(start, last);
                String[] ss = s.split(",");
                for (int j = 0; j < ss.length; j++) {
//                    String[] sss = ss[j].split(":");
                    if (ss[j].startsWith("\"html\"")) {
                        start = ss[j].indexOf("<");
                        last = ss[j].lastIndexOf(">") + 1;
                        n = ss[j].substring(start, last);
//                        Log.e("ZTAG", "" + n);
                        n = n.replaceAll("\\t", "");
                        n = n.replaceAll("\\n", "");
                        n = n.substring(n.indexOf("<span  class=\\\"S_txt1\\\">"));
                        Document d = Jsoup.parse(n);

//                        Elements e1 = d.select("span.S_txt1");
                        Elements e1 = d.getElementsByAttributeValue("class", "S_txt1");
//                        "//*[@id=\"Pl_Official_RightGrowNew__57\"]/div/div/div/div[2]/div/div[2]/div/div/p/span[2]/span/span"
                        n = e1.toString();
                    }
                }

            }
        }
        s = n;
//        String[] f = e.text().split(" ");
//        s = (splitsqrt(f[0]));
//        s = (f[0]) + " " + (f[1]) + " " + (f[2]);

        return s;
    }
}
