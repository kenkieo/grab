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
                    g.getAttUserInfo(a.getId() + "");
                    g.getAttUserInfo2(a.getId() + "");
                    g.getAttUserInfo3(a.getId() + "");
//                }
//                sa = g.getListUser();
//                for (UserInfo a : sa) {
                    EventBus.getDefault().post(new StockEvent("---------------------------------------------------------------------", false));
                    EventBus.getDefault().post(new StockEvent(UserInfoToString(a), COLOR_DIS, false));
                    s = g.getUserTrade(a.getId() + "");
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
        s = uinfo.toPercent(uinfo.getSucRate());
        String wp = uinfo.toPercent(uinfo.getwProfit());
        String tp = uinfo.toPercent(uinfo.gettProfit());
        String mp = uinfo.toPercent(uinfo.getmProfit());
        StringBuilder a = new StringBuilder();
        a.append(getResources().getString(R.string.id) + uinfo.getId() + " : " + uinfo.getNickname() + "<br>");
        if (s.indexOf("-") == -1) COLOR_START = RED_COLOR;
        else COLOR_START = BLUE_COLOR;
        a.append(getResources().getString(R.string.suc_rate) + COLOR_START + s + "% " + COLOR_END);
        if (uinfo.getProfitRate().indexOf("-") == -1) COLOR_START = RED_COLOR;
        else COLOR_START = BLUE_COLOR;
        a.append(getResources().getString(R.string.total_profit_rate) + COLOR_START + uinfo.getProfitRate() + COLOR_END + "<br>");
        a.append(getResources().getString(R.string.avg_days) + uinfo.getAvgDays() + " ");
        a.append(getResources().getString(R.string.avg_profit) + uinfo.getAvgProfit() + " ");
        a.append(getResources().getString(R.string.trade_freq) + uinfo.getTradingFrequency() + "<br>");
        a.append(getResources().getString(R.string.t_rank) + uinfo.gettRank() + " ");
        if (uinfo.gettRise().indexOf("-") == -1) COLOR_START = RED_COLOR;
        else COLOR_START = BLUE_COLOR;
        a.append(getResources().getString(R.string.t_rise) + COLOR_START + uinfo.gettRise() + COLOR_END + " ");
        if (tp.indexOf("-") == -1) COLOR_START = RED_COLOR;
        else COLOR_START = BLUE_COLOR;
        a.append(getResources().getString(R.string.t_profit) + COLOR_START + tp + "%" + COLOR_END + " <br>");
        a.append(getResources().getString(R.string.m_rank) + uinfo.getmRank() + " ");
        if (uinfo.getmRise().indexOf("-") == -1) COLOR_START = RED_COLOR;
        else COLOR_START = BLUE_COLOR;
        a.append(getResources().getString(R.string.m_rise) + COLOR_START + uinfo.getmRise() + COLOR_END + " ");
        if (mp.indexOf("-") == -1) COLOR_START = RED_COLOR;
        else COLOR_START = BLUE_COLOR;
        a.append(getResources().getString(R.string.m_profit) + COLOR_START + mp + "%" + COLOR_END + " <br>");
        a.append(getResources().getString(R.string.w_rank) + uinfo.getwRank() + " ");
        if (uinfo.getwRise().indexOf("-") == -1) COLOR_START = RED_COLOR;
        else COLOR_START = BLUE_COLOR;
        a.append(getResources().getString(R.string.w_rise) + COLOR_START + uinfo.getwRise() + COLOR_END + " ");
        if (wp.indexOf("-") == -1) COLOR_START = RED_COLOR;
        else COLOR_START = BLUE_COLOR;
        a.append(getResources().getString(R.string.w_profit) + COLOR_START + wp + "%" + COLOR_END + " <br>");
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
