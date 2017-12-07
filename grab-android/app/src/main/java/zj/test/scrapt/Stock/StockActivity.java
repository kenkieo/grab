package zj.test.scrapt.Stock;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import zj.test.scrapt.R;

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
                }
                sa = g.getListUser();
                for (UserInfo a : sa) {
                    EventBus.getDefault().post(new StockEvent("---------------------------------------------------------------------", false));
                    EventBus.getDefault().post(new StockEvent(a.toString(), false));
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
            tvs.append(event.message + "\n");
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
