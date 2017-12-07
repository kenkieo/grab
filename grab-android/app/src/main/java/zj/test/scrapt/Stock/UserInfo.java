package zj.test.scrapt.Stock;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2017/12/6.
 */

public class UserInfo {

    private int id = 0;
    private String nickname = "";
    private String profitRate = "";
    private String accountId = "";
    private int accuracy = 0;
    private String profitability = "";
    private String ratingGrade = "";
    private int stability = 0;
    private int totalDays = 0;
    private String totalScore = "";

    private String avgDays = "";
    private String avgProfit = "";
    private String closeNum = "";
    private String sucNum = "";
    private String sucRate = "";
    private int totalDays2 = 0;
    private String totalStock = "";
    private String tradingFrequency = "";

    public UserInfo(int id) {
        this.id = id;
    }

    private UserInfo(int id, String nickname, String profitRate) {
        this.id = id;
        this.nickname = nickname;
        this.profitRate = profitRate;
    }

    public String getAvgDays() {
        return avgDays;
    }

    public UserInfo setAvgDays(String avgDays) {
        this.avgDays = avgDays;
        return this;
    }

    public String getAvgProfit() {
        return avgProfit;
    }

    public UserInfo setAvgProfit(String avgProfit) {
        this.avgProfit = avgProfit;
        return this;
    }

    public String getCloseNum() {
        return closeNum;
    }

    public UserInfo setCloseNum(String closeNum) {
        this.closeNum = closeNum;
        return this;
    }

    public String getSucNum() {
        return sucNum;
    }

    public UserInfo setSucNum(String sucNum) {
        this.sucNum = sucNum;
        return this;
    }

    public String getSucRate() {
        return sucRate;
    }

    public UserInfo setSucRate(String sucRate) {
        this.sucRate = sucRate;
        return this;
    }

    public int getTotalDays2() {
        return totalDays2;
    }

    public UserInfo setTotalDays2(int totalDays2) {
        this.totalDays2 = totalDays2;
        return this;
    }

    public String getTotalStock() {
        return totalStock;
    }

    public UserInfo setTotalStock(String totalStock) {
        this.totalStock = totalStock;
        return this;
    }

    public String getTradingFrequency() {
        return tradingFrequency;
    }

    public UserInfo setTradingFrequency(String tradingFrequency) {
        this.tradingFrequency = tradingFrequency;
        return this;
    }

    public String getAccountId() {
        return accountId;
    }

    public UserInfo setAccountId(String accountId) {
        this.accountId = accountId;
        return this;
    }

    public int getAccuracy() {
        return accuracy;
    }

    public UserInfo setAccuracy(int accuracy) {
        this.accuracy = accuracy;
        return this;
    }


    public String getProfitability() {
        return profitability;
    }

    public UserInfo setProfitability(String profitability) {
        this.profitability = profitability;
        return this;
    }

    public String getRatingGrade() {
        return ratingGrade;
    }

    public UserInfo setRatingGrade(String ratingGrade) {
        this.ratingGrade = ratingGrade;
        return this;
    }

    public int getStability() {
        return stability;
    }

    public UserInfo setStability(int stability) {
        this.stability = stability;
        return this;
    }

    public int getTotalDays() {
        return totalDays;
    }

    public UserInfo setTotalDays(int totalDays) {
        this.totalDays = totalDays;
        return this;
    }

    public String getTotalScore() {
        return totalScore;
    }

    public UserInfo setTotalScore(String totalScore) {
        this.totalScore = totalScore;
        return this;
    }

    public String toString() {
        String s;
        if (sucRate != "") {
            sucRate = Double.parseDouble(sucRate) * 100 + "";
            BigDecimal bigG = new BigDecimal(sucRate).setScale(1, BigDecimal.ROUND_HALF_UP);
            s = bigG.toString();
        } else {
            s = "";
        }

        return this.id + " : " + this.nickname + " " + profitRate + " \navg days: " +
                avgDays + " avg gain: " + avgProfit + " success: " + s + "% tradingFrequency: " + tradingFrequency + "\n";
    }

    public int getId() {
        return id;
    }

    public UserInfo setId(int id) {
        this.id = id;
        return this;
    }

    public String getNickname() {
        return nickname;
    }

    public UserInfo setNickname(String nickname) {
        this.nickname = nickname;
        return this;
    }

    public String getProfitRate() {

        return profitRate;
    }

    public UserInfo setProfitRate(String profitRate) {
        this.profitRate = profitRate;
        return this;
    }

//    public static class Builder {
//        private int id;
//        private String nickname;
//        private String profitRate;
//
//        public String getProfitRate() {
//            return profitRate;
//        }
//
//        public Builder setProfitRate(String profitRate) {
//            this.profitRate = profitRate;
//            return this;
//        }
//
//        public int getId() {
//            return id;
//        }
//
//        public Builder setId(int id) {
//            this.id = id;
//            return this;
//        }
//
//        public String getNickname() {
//            return nickname;
//        }
//
//        public Builder setNickname(String nickname) {
//            this.nickname = nickname;
//            return this;
//        }
//
//        public UserInfo build() {
//            return new UserInfo(id, nickname, profitRate);
//        }

}
