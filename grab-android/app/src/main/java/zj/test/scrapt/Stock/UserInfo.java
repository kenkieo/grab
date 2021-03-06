package zj.test.scrapt.Stock;

import java.math.BigDecimal;

import zj.test.scrapt.R;

/**
 * Created by Administrator on 2017/12/6.
 */

public class UserInfo {

    public int id = 0;
    public String nickname = "";
    public String profitRate = "";
    public String accountId = "";
    public int accuracy = 0;
    public String profitability = "";
    public String ratingGrade = "";
    public int stability = 0;
    public int totalDays = 0;
    public String totalScore = "";

    public String avgDays = "";
    public String avgProfit = "";
    public String closeNum = "";
    public String sucNum = "";
    public String sucRate = "";
    public int totalDays2 = 0;
    public String totalStock = "";
    public String tradingFrequency = "";

    public String tRank = "";
    public String mRise = "";
    public String wProfit = "";
    public String mRank = "";
    public String status = "";
    public String tProfit = "";
    public String tRise = "";
    public String wRise = "";
    public String mProfit = "";
    public String wRank = "";

    public UserInfo(int id) {
        this.id = id;
    }

    private UserInfo(int id, String nickname, String profitRate) {
        this.id = id;
        this.nickname = nickname;
        this.profitRate = profitRate;
    }

    public String gettRank() {
        return tRank;
    }

    public UserInfo settRank(String tRank) {
        this.tRank = tRank;
        return this;
    }
//
//    public String getmRise() {
//        return mRise;
//    }

    public UserInfo setmRise(String mRise) {
        this.mRise = mRise;
        return this;
    }

//    public String getwProfit() {
//        return wProfit;
//    }

    public UserInfo setwProfit(String wProfit) {
        this.wProfit = wProfit;
        return this;
    }

//    public String getmRank() {
//        return mRank;
//    }

    public UserInfo setmRank(String mRank) {
        this.mRank = mRank;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public UserInfo setStatus(String status) {
        this.status = status;
        return this;
    }

//    public String gettProfit() {
//        return tProfit;
//    }

    public UserInfo settProfit(String tProfit) {
        this.tProfit = tProfit;
        return this;
    }

//    public String gettRise() {
//        return tRise;
//    }

    public UserInfo settRise(String tRise) {
        this.tRise = tRise;
        return this;
    }

//    public String getwRise() {
//        return wRise;
//    }

    public UserInfo setwRise(String wRise) {
        this.wRise = wRise;
        return this;
    }

//    public String getmProfit() {
//        return mProfit;
//    }

    public UserInfo setmProfit(String mProfit) {
        this.mProfit = mProfit;
        return this;
    }

//    public String getwRank() {
//        return wRank;
//    }

    public UserInfo setwRank(String wRank) {
        this.wRank = wRank;
        return this;
    }

//    public String getAvgDays() {
//        return avgDays;
//    }

    public UserInfo setAvgDays(String avgDays) {
        this.avgDays = avgDays;
        return this;
    }

//    public String getAvgProfit() {
//        return avgProfit;
//    }

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

//    public String getSucNum() {
//        return sucNum;
//    }

    public UserInfo setSucNum(String sucNum) {
        this.sucNum = sucNum;
        return this;
    }

//    public String getSucRate() {
//        return sucRate;
//    }

    public UserInfo setSucRate(String sucRate) {
        this.sucRate = sucRate;
        return this;
    }

//    public int getTotalDays2() {
//        return totalDays2;
//    }

    public UserInfo setTotalDays2(int totalDays2) {
        this.totalDays2 = totalDays2;
        return this;
    }

//    public String getTotalStock() {
//        return totalStock;
//    }

    public UserInfo setTotalStock(String totalStock) {
        this.totalStock = totalStock;
        return this;
    }

//    public String getTradingFrequency() {
//        return tradingFrequency;
//    }

    public UserInfo setTradingFrequency(String tradingFrequency) {
        this.tradingFrequency = tradingFrequency;
        return this;
    }

//    public String getAccountId() {
//        return accountId;
//    }

    public UserInfo setAccountId(String accountId) {
        this.accountId = accountId;
        return this;
    }

//    public int getAccuracy() {
//        return accuracy;
//    }

    public UserInfo setAccuracy(int accuracy) {
        this.accuracy = accuracy;
        return this;
    }


//    public String getProfitability() {
//        return profitability;
//    }

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

//    public int getStability() {
//        return stability;
//    }

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

//    public String getTotalScore() {
//        return totalScore;
//    }

    public UserInfo setTotalScore(String totalScore) {
        this.totalScore = totalScore;
        return this;
    }

    public String toPercent(String p) {
        String s;
        if (p != "") {
            if (p.lastIndexOf("%") != -1)
                p = p.substring(0, p.lastIndexOf("%"));
            p = Double.parseDouble(p) * 100 + "";
            BigDecimal bigG = new BigDecimal(p).setScale(1, BigDecimal.ROUND_HALF_UP);
            s = bigG.toString();
        } else {
            s = "";
        }

        return s;
    }

    public String toString() {
        String s;
        s = toPercent(sucRate);
        String wp = toPercent(wProfit);
        String tp = toPercent(tProfit);
        String mp = toPercent(mProfit);

        return (R.string.suc_rate) + s + "% " + R.string.total_profit_rate + " " + R.string.id + this.id + " : " + this.nickname + "\n"
                + R.string.avg_days + this.avgDays + " " + R.string.avg_profit + this.avgProfit + " " + R.string.trade_freq + this.tradingFrequency + "\n" +
                R.string.t_rank + tRank + " " + R.string.t_rise + tRise + " " + R.string.t_profit + tp + "% \n" +
                R.string.m_rank + mRank + " " + R.string.m_rise + mRise + " " + R.string.m_profit + mp + "% \n" +
                R.string.w_rank + wRank + " " + R.string.w_rise + wRise + " " + R.string.w_profit + wp + "% \n"+
                sucNum+" "+closeNum;

    }

////    public int getId() {
//        return id;
//    }

    public UserInfo setId(int id) {
        this.id = id;
        return this;
    }

//    public String getNickname() {
//        return nickname;
//    }

    public UserInfo setNickname(String nickname) {
        this.nickname = nickname;
        return this;
    }

//    public String getProfitRate() {
//
//        return profitRate;
//    }

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
