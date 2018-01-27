package zj.test.scrapt.Odds;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import zj.test.scrapt.R;
import zj.test.scrapt.Tweet.MainEvent;

import static zj.test.scrapt.Tweet.MainEvent.EventType.EDIT_FOCUS;

/**
 * Created by Administrator on 2018/1/23.
 */

public class OddsActivity extends Activity {
    EditText win_edt = null;
    EditText peace_edt = null;
    EditText lose_edt = null;
    Button do_btn = null;
    TextView tv = null;

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            doCaculate();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.odds);
        win_edt = findViewById(R.id.win_et);
        peace_edt = findViewById(R.id.peace_et);
        lose_edt = findViewById(R.id.lose_et);
        do_btn = findViewById(R.id.docaculate);
        tv = findViewById(R.id.resault_text);

        do_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(runnable).start();
//                for (int i = 0; i < 10; i++) {
//                    doCaculate();
//                }
            }
        });
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

    private int doRandom(float l, float r) {
        int num = 0, flag = 0;

        for (int i = 0; i < 10; i++) {
            num = (int) ((Math.random() * 9 + 1) * 10000000);

            if (num <= l) {
                flag = 1;
            } else if (num <= r) {
                flag = 2;
            } else {
                flag = 3;
            }

            if (flag == 1) {
                EventBus.getDefault().post(new MainEvent("1", EDIT_FOCUS, false));
            } else if (flag == 2) {
                EventBus.getDefault().post(new MainEvent("2", EDIT_FOCUS, false));
            } else if (flag == 3) {
                EventBus.getDefault().post(new MainEvent("3", EDIT_FOCUS, false));
            }
        }

        return flag;
    }

    public void doCaculate() {
        float w = String2Float(win_edt.getText().toString());
        float p = String2Float(peace_edt.getText().toString());
        float l = String2Float(lose_edt.getText().toString());
        float s = 100 / (1 / w + 1 / p + 1 / l);//87.5%
        int flag = 0;


        String a = ("total: " + s + "%\n");
        EventBus.getDefault().post(new MainEvent(a, true));
        EventBus.getDefault().post(new MainEvent(("              " + s / w + "    " + s / p + "    " + s / l + "\n"), false));

        float left = s / w * 1000000;
        float right = s / p * 1000000 + s / w * 1000000;
        for (int i = 0; i < 20; i++) {
            flag = doRandom(left, right);
            try {
                Thread.sleep(50);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
//        Log.e("ZTAG", " " + s / w + " : " + s / p + " : " + s / l + " : " + s);

        if (flag == 1) {
//            win_edt.setFocusable(true);
//            win_edt.requestFocus();
            EventBus.getDefault().post(new MainEvent("select     1", false));
        } else if (flag == 2) {
//            peace_edt.setFocusable(true);
//            peace_edt.requestFocus();
            EventBus.getDefault().post(new MainEvent("select     2", false));
        } else if (flag == 3) {
//            lose_edt.setFocusable(true);
//            lose_edt.requestFocus();
            EventBus.getDefault().post(new MainEvent("select     3", false));
        } else {
            EventBus.getDefault().post(new MainEvent("select     error", false));
        }
        Log.e("ztag", "  ===============  " + flag);
    }


    private float String2Float(String s) {
        float a = 0;
        try {
            a = Float.parseFloat(s);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return a;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MainEvent event) {
        if (event.type == MainEvent.EventType.MESSAGE_DIS) {
            if (event.clear) {
                if (tv != null) tv.setText("" + event.message);
            } else {
                if (tv != null) tv.append(event.message);
            }
        } else if (event.type == MainEvent.EventType.EDIT_FOCUS) {
            if (event.message == "1") {
                win_edt.setFocusable(true);
                win_edt.requestFocus();
            } else if (event.message == "2") {
                peace_edt.setFocusable(true);
                peace_edt.requestFocus();
            } else if (event.message == "3") {
                lose_edt.setFocusable(true);
                lose_edt.requestFocus();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
