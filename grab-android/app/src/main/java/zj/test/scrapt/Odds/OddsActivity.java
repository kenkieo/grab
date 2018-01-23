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

/**
 * Created by Administrator on 2018/1/23.
 */

public class OddsActivity extends Activity {
    EditText win_edt = null;
    EditText peace_edt = null;
    EditText lose_edt = null;
    Button do_btn = null;
    TextView tv = null;

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
                doCaculate();
            }
        });
    }

    public void doCaculate() {
        float w = String2Float(win_edt.getText().toString());
        float p = String2Float(peace_edt.getText().toString());
        float l = String2Float(lose_edt.getText().toString());
        float s = 100 / (1 / w + 1 / p + 1 / l);//87.5%
        tv.setText("total: " + s + "%\n");

        tv.append("              " + s / w + "    " + s / p + "    " + s / l);

        Log.e("ZTAG", " " + s / w + " : " + s / p + " : " + s / l + " : " + s);

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
    public void onResume() {
        super.onResume();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MainEvent event) {

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
