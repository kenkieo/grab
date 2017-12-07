package zj.test.scrapt.Tweet;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


/**
 * Created by Administrator on 2017/11/23.
 */

public class ParseTweetItem extends ParseItem {

    public static String parse(TweetItem item, Document document) {
        Elements e = document.select("script");

        String s = e.text();
        String result = "";
        String n = null;
        Element t;
        int start = 0;
        int last = 0;
        result = item.uid + "\n";
        for (int i = 0; i < e.size(); i++) {
            t = e.get(i);
            s = t.toString();
            if (s.contains("\"domid\":\"Pl_Official_RightGrowNew__")) {
                start = s.indexOf("{") + 1;
                last = s.lastIndexOf("}");
                s = s.substring(start, last);
                String[] ss = s.split(",");
                for (int j = 0; j < ss.length; j++) {
                    if (ss[j].startsWith("\"html\"")) {
                        start = ss[j].indexOf("<");
                        last = ss[j].lastIndexOf(">") + 1;
                        n = ss[j].substring(start, last);
                        Document d = Jsoup.parse(n);
                        Elements e1 = d.select("span[class*=S_txt1]");
                        n = e1.get(1).toString();
                        n = n.substring(n.indexOf(">") + 1);
                        n = n.substring(0, n.indexOf("&"));
                        result += " code: " + n + "\n";
                        item.integral = Integer.parseInt(n);
                    }
                }
            } else if (s.contains("\"domid\":\"Pl_Core_T8CustomTriColumn__")) {
                start = s.indexOf("{") + 1;
                last = s.lastIndexOf("}");
                s = s.substring(start, last);
                String[] ss = s.split(",");
                for (int j = 0; j < ss.length; j++) {
                    if (ss[j].startsWith("\"html\"")) {
                        start = ss[j].indexOf("<");
                        last = ss[j].lastIndexOf(">") + 1;
                        n = ss[j].substring(start, last);
                        Document d = Jsoup.parse(n);
                        Elements e1 = d.select("span[class*=S_txt2]");
                        Elements e2 = d.select("strong[class*=W_f18]");
//                        for (int k = 0; k < e1.size(); k++) {
                        n = e2.get(0).toString();
                        n = n.substring(n.indexOf(">") + 1);
                        n = n.substring(0, n.indexOf("&"));
                        result += n + " ";
                        item.follow = Integer.parseInt(n);
                        n = e1.get(0).toString();
                        n = n.substring(n.indexOf(">") + 1);
                        n = n.substring(0, n.indexOf("&"));
                        result += n + " ";
                        n = e2.get(1).toString();
                        n = n.substring(n.indexOf(">") + 1);
                        n = n.substring(0, n.indexOf("&"));
                        result += n + " ";
                        item.fan = Integer.parseInt(n);
                        n = e1.get(1).toString();
                        n = n.substring(n.indexOf(">") + 1);
                        n = n.substring(0, n.indexOf("&"));
                        result += n + " ";
                        n = e2.get(2).toString();
                        n = n.substring(n.indexOf(">") + 1);
                        n = n.substring(0, n.indexOf("&"));
                        result += n + " ";
                        item.tweet = Integer.parseInt(n);
                        n = e1.get(2).toString();
                        n = n.substring(n.indexOf(">") + 1);
                        n = n.substring(0, n.indexOf("&"));
                        result += n + " ";
//                        }

                    }
                }
            }
        }
//        s = n;
//        String[] f = e.text().split(" ");
//        s = (splitsqrt(f[0]));
//        s = (f[0]) + " " + (f[1]) + " " + (f[2]);

        return result;
    }
}
