package zj.test.scrapt.Stock;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import zj.test.scrapt.R;
import zj.zfenlly.gua.LoadInjectLib;

import static zj.test.scrapt.Stock.StockEvent.EventType.COLOR_DIS;

/**
 * Created by Administrator on 2017/11/13.
 */

public class StockActivity extends Activity {

    ListView lv = null;
    TextView tvs = null;
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            GrabYougu g = new GrabYougu(StockActivity.this);
            String s = "";
            EventBus.getDefault().post(new StockEvent(s, true));
            try {
                g.getAttUsers();
                List<UserInfo> sa = g.getListUser();
                for (UserInfo a : sa) {
                    g.getAttUserInfo(a.id + "");
                    g.getAttUserInfo2(a.id + "");
                    g.getAttUserInfo3(a.id + "");
//                }
//                sa = g.getListUser();
//                for (UserInfo a : sa) {
                    EventBus.getDefault().post(new StockEvent("---------------------------------------------------------------------", false));
                    EventBus.getDefault().post(new StockEvent(UserInfoToString(a), COLOR_DIS, false));
                    s = g.getUserTrade(a.id + "");
                    EventBus.getDefault().post(new StockEvent(s, false));
//                    EventBus.getDefault().post(new StockEvent("---------------------------------------------------------------------", false));
                }

            } catch (Exception e) {
                e.printStackTrace();
                s = e.getMessage();
                EventBus.getDefault().post(new StockEvent(s, true));
            }

        }
    };

    private String UserInfoToString(UserInfo uinfo) {
        String s;
        String RED_COLOR = "<font color=\'#ff0000\'>";
        String BLUE_COLOR = "<font color=\'#0000ff\'>";
        String GREEN_COLOR = "<font color=\'#00ff00\'>";
        String COLOR_START = RED_COLOR;
        String COLOR_END = "</font>";
        s = uinfo.toPercent(uinfo.sucRate);
//        String wp = uinfo.toPercent(uinfo.wProfit);
//        String tp = uinfo.toPercent(uinfo.tProfit);
//        String mp = uinfo.toPercent(uinfo.mProfit);
        StringBuilder a = new StringBuilder();
        a.append(getResources().getString(R.string.id) + uinfo.id + " : " + uinfo.nickname + "<br>");
        if (s.indexOf("-") == -1) COLOR_START = RED_COLOR;
        else COLOR_START = BLUE_COLOR;
        a.append(getResources().getString(R.string.suc_rate) + COLOR_START + s + "% " + COLOR_END);

        a.append(getResources().getString(R.string.t_rank) + uinfo.gettRank() + " ");
        if (uinfo.tRise.indexOf("-") == -1) COLOR_START = RED_COLOR;
        else COLOR_START = BLUE_COLOR;
        a.append(getResources().getString(R.string.t_rise) + COLOR_START + uinfo.tRise + COLOR_END + " ");
        if (uinfo.tProfit.indexOf("-") == -1) COLOR_START = RED_COLOR;
        else COLOR_START = BLUE_COLOR;
        a.append(getResources().getString(R.string.t_profit) + COLOR_START + uinfo.tProfit + COLOR_END + " <br>");
        if (uinfo.mProfit.indexOf("-") == -1) COLOR_START = RED_COLOR;
        else COLOR_START = BLUE_COLOR;
        a.append(getResources().getString(R.string.m_profit) + COLOR_START + uinfo.mProfit + COLOR_END);
        a.append(" " + getResources().getString(R.string.m_rank) + " " + uinfo.mRank + " ");
        if (uinfo.mRise.indexOf("-") == -1) COLOR_START = RED_COLOR;
        else COLOR_START = BLUE_COLOR;
        a.append(getResources().getString(R.string.m_rise) + COLOR_START + uinfo.mRise + COLOR_END + " <br>");

        if (uinfo.wProfit.indexOf("-") == -1) COLOR_START = RED_COLOR;
        else COLOR_START = BLUE_COLOR;
        a.append(getResources().getString(R.string.w_profit) + COLOR_START + uinfo.wProfit + COLOR_END);
        a.append(" " + getResources().getString(R.string.w_rank) + " " + uinfo.wRank + " ");
        if (uinfo.wRise.indexOf("-") == -1) COLOR_START = RED_COLOR;
        else COLOR_START = BLUE_COLOR;
        a.append(getResources().getString(R.string.w_rise) + COLOR_START + uinfo.wRise + COLOR_END + " <br>");

        a.append(getResources().getString(R.string.avg_days) + uinfo.avgDays + " ");
        a.append(getResources().getString(R.string.avg_profit) + uinfo.avgProfit + " ");
        a.append(getResources().getString(R.string.trade_freq) + uinfo.tradingFrequency + "<br>");
        return a.toString();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stock);
//        lv = findViewById(R.id.list_stock);
        tvs = findViewById(R.id.tv_stock);
        tvs.setMovementMethod(ScrollingMovementMethod.getInstance());

        new Thread(runnable).start();

//        test();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(StockEvent event) {

        if (event.clear == false) {
            if (event.type == StockEvent.EventType.MESSAGE_DIS) {
                tvs.append(event.message + "\n");
            } else if (event.type == StockEvent.EventType.COLOR_DIS) {
                tvs.append(Html.fromHtml(event.message));
//                tvs.append(Html.fromHtml("<font color=\'#ff0000\'>红色</font>其它颜色"));
            }
        } else {
            tvs.setText("" + event.message + "\n");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        tvs.setText("");
        Log.e("ZTAG", "StockActivity onDestroy");
    }

}
