package zj.test.scrapt.Tweet;

/**
 * Created by Administrator on 2017/11/10.
 */

public class TweetItem {
    public int tweet = 0;
    public int follow = 0;
    public int fan = 0;
    public String uid;
    public int integral = 0;

    public TweetItem(String u) {
        this.uid = u;
    }

    private TweetItem(int tweet, int follow, int fan, int integral, String uid) {
        this.tweet = tweet;
        this.follow = follow;
        this.fan = fan;
        this.integral = integral;
        this.uid = uid;

    }

    public String toString() {
        return uid + "\nfollow: " + this.follow + " " + " fan: " + this.fan + " tweete: " + this.tweet + " code: " + this.integral;
    }

    public String getUid() {
        return uid;
    }

    public static class Builder {
        public int tweet = 0;
        public int follow = 0;
        public int fan = 0;
        public String uid;
        public int integral = 0;

        public int getTweet() {
            return tweet;
        }

        public Builder setTweet(int tweet) {
            this.tweet = tweet;
            return this;
        }

        public int getFollow() {
            return follow;
        }

        public Builder setFollow(int follow) {
            this.follow = follow;
            return this;
        }

        public int getFan() {
            return fan;
        }

        public Builder setFan(int fan) {
            this.fan = fan;
            return this;
        }

        public String getUid() {
            return uid;
        }

        public Builder setUid(String uid) {
            this.uid = uid;
            return this;
        }

        public int getIntegral() {
            return integral;
        }

        public Builder setIntegral(int integral) {
            this.integral = integral;
            return this;
        }

        public TweetItem build() {
            return new TweetItem(tweet, follow, fan, integral, uid);
        }
    }

//    public TweetNote TweetConvert
}
