package zj.test.scrapt.Tweet;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import zj.test.scrapt.Odds.OddsActivity;
import zj.test.scrapt.R;
import zj.test.scrapt.Stock.Stock2Activity;
import zj.test.scrapt.Stock.StockActivity;
import zj.test.scrapt.Wifi.WifiControl;
import zj.zfenlly.gua.MPermissions;
import zj.zfenlly.record.RecordActivity;

import static zj.test.scrapt.Tweet.UserID.first_uid;
import static zj.test.scrapt.Tweet.UserID.fourth_uid;
import static zj.test.scrapt.Tweet.UserID.second_uid;
import static zj.test.scrapt.Tweet.UserID.third_uid;

public class MainActivity extends Activity {

    public static final int REQUEST_CODE_ACCESS_EXTERNAL_STORAGE = 12334;


    static {
        System.loadLibrary("native-lib");
    }

    TextView tv = null;
    EditText uidedt = null;
    EditText pwdedt = null;
    Button login_btn = null;
    Button uc_btn = null;
    Button stock_btn = null;
    Button ball_btn = null;
    //    Button record_btn = null;
    Button odds_btn = null;
    Button records_btn = null;
    Button stock2_btn = null;
    String uid;
    String pwd;
    Context mContext;
    Runnable ballRunnable = new Runnable() {
        @Override
        public void run() {
            String s = "         ";
            EventBus.getDefault().post(new MainEvent(s, true));
            EventBus.getDefault().post(new MainEvent("" + random(6, 1, 34), false));
        }
    };

    public void set_led(int a) {
        try {
            Process process = null;
            DataOutputStream dos = null;
            process = Runtime.getRuntime().exec("sh");
            dos = new DataOutputStream(process.getOutputStream());
            if (a == 0) {
                dos.writeBytes("echo 0 > /sys/devices/leds.28/leds/camera-led/brightness\n");
            } else {
                dos.writeBytes("echo 1 > /sys/devices/leds.28/leds/camera-led/brightness\n");
            }
            Log.e("TAG", "1:" + a);
            dos.flush();
//            process.waitFor();
            dos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e("TAG", "2:" + a);
    }

    static int led_on_off = 0;
    Runnable runnableled = new Runnable() {
        @Override
        public void run() {
            if (led_on_off == 1) {
                led_on_off = 0;
            } else {
                led_on_off = 1;
                reboot();
            }
            set_led(led_on_off);
        }
    };

    public void reboot() {
        mPowerManager = (PowerManager) mContext.getSystemService(Context.POWER_SERVICE);
        mPowerManager.reboot("reboot");
    }

    private PowerManager mPowerManager;
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            String s = "";
            EventBus.getDefault().post(new MainEvent(s, true));
            if (checkForReady()) {
                GrabTweet a = new GrabTweet(MainActivity.this);
                s = a.login(uid, pwd);
                EventBus.getDefault().post(new MainEvent(s, false));
                s = a.getUserInfo(first_uid);
                EventBus.getDefault().post(new MainEvent(s, false));
                s = a.getUserInfo(second_uid);
                EventBus.getDefault().post(new MainEvent(s, false));
                s = a.getUserInfo(third_uid);
                EventBus.getDefault().post(new MainEvent(s, false));
                s = a.getUserInfo(fourth_uid);
                EventBus.getDefault().post(new MainEvent(s, false));
            } else {
                s = "NetWork is disconnect!!!";
                EventBus.getDefault().post(new MainEvent(s, false));
            }
        }
    };

    public String random(int num, int minValue, int maxValue) {
        if (num > maxValue) {
            num = maxValue;
        }
        if (num < 0 || maxValue < 0) {
            throw new RuntimeException("num or maxValue must be greater than zero");
        }
        List<Integer> result = new ArrayList<Integer>(num);
        String a = "";
        String b = "";

        int len = maxValue - minValue;
        int[] tmpArray = new int[len];
        int[] tmp2Array = new int[num];

        for (int i = 0; i < len; i++) {
            tmpArray[i] = i + minValue;
        }
        for (int i = 0; i < num; i++) {
            tmp2Array[i] = 0;
        }

        Random random = new Random();
        b = "" + (random.nextInt(16) + 1);
        for (int i = 0; i < num; i++) {
            int index = random.nextInt(len - i);
            int tmpValue = tmpArray[index];
            tmp2Array[i] = tmpValue;
            result.add(tmpValue);
            int lastIndex = len - i - 1;
            if (index == lastIndex) {
                continue;
            } else {
                tmpArray[index] = tmpArray[lastIndex];
            }
        }

        for (int i = 0; i < num; i++) {
            for (int j = i + 1; j < num; j++) {
                if (tmp2Array[i] > tmp2Array[j]) {
                    int z = tmp2Array[j];
                    tmp2Array[j] = tmp2Array[i];
                    tmp2Array[i] = z;
                }
            }
            a += tmp2Array[i] + " ";
        }

        a += " : " + b;
        return a;
    }

    private String getNumsFromRange(int n, int start, int end) {
        String a = "";
        long[] l = new long[n];
        boolean flag = false;

        for (int i = 0; i < n; i++) {
            int d = (int) (longFromJNI() % (end - start)) + start;
            flag = false;
            for (int j = 0; j < i; j++) {
                if (d == l[j]) {
                    flag = true;
                }
            }
            if (flag) {
                i--;
            } else {
                l[i] = d;
            }
        }
        for (int i = 0; i < n; i++) {
            a += l[i] + " ";
        }
        return a;
    }

    boolean checkForReady() {
        boolean isconnect = WifiControl.isConnect(MainActivity.this);
        boolean ready = false;
        if (isconnect == true) {
            uid = uidedt.getText().toString();
            pwd = pwdedt.getText().toString();
            if ((uid == null) || (uid.equals(""))) {
                Toast.makeText(MainActivity.this, "UID is null !!!", Toast.LENGTH_SHORT).show();
                return false;
            }
            if ((pwd == null) || (pwd.equals(""))) {
                Toast.makeText(MainActivity.this, "passwd is null !!!", Toast.LENGTH_SHORT).show();
                return false;
            }
            ready = true;
        }
        return ready;
    }

    private boolean allowWriteExternal() {
        if (selfPermissionGranted(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != true) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Log.e("ztag", "request write external storage");
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_CODE_ACCESS_EXTERNAL_STORAGE);
            }
            Log.e("ztag", "request write external storage return false");
            return false;
        } else {
            Log.e("ztag", "request write external storage return true");
            return true;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        MPermissions.onResult(this, requestCode);//gua by linurm
    }

    //gua by linurm
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.e("ztag", "result: " + permissions.toString());
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//
        MPermissions.onPermissionsResult(this, requestCode, permissions, grantResults);
//        if (requestCode == REQUEST_CODE_ACCESS_EXTERNAL_STORAGE) {
//            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
////                Toast.makeText(this, "Permission allowed", Toast.LENGTH_SHORT).show();
//                Log.e("ztag", "request write external storage granted");
////                askForPermission();
//            } else {
//                Log.e("ztag", "request write external storage not granted");
////                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
//            }
//        } else if (requestCode == OVERLAY_PERMISSION_REQ_CODE) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                if (!Settings.canDrawOverlays(this)) {
//                    Log.e("ztag", "draw overlay permission not granted");
////                    Toast.makeText(this, "权限授予失败，无法开启悬浮窗", Toast.LENGTH_SHORT).show();
//                } else {
//                    Log.e("ztag", "draw overlay permission granted");
////                    Toast.makeText(this, "权限授予成功！", Toast.LENGTH_SHORT).show();
//                    //启动FxService
//                    startService(floatWinIntent);
//                }
//            } else {
//                startService(floatWinIntent);
//            }
//        }
    }

    public boolean selfPermissionGranted(Context context, String permission) {
        boolean result = true;
        int targetSdkVersion;
        try {
            final PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0);
            targetSdkVersion = info.applicationInfo.targetSdkVersion;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (targetSdkVersion >= Build.VERSION_CODES.M) {
                result = ContextCompat.checkSelfPermission(context, permission)
                        == PackageManager.PERMISSION_GRANTED;
            } else {
                result = PermissionChecker.checkSelfPermission(context, permission)
                        == PermissionChecker.PERMISSION_GRANTED;
            }
        }
        return result;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MainEvent event) {
        if (event.type == MainEvent.EventType.MESSAGE_DIS) {
            if (event.clear) {
                if (tv != null) tv.setText("" + event.message);
            } else {
                if (tv != null) tv.append(event.message);
            }
        } else if (event.type == MainEvent.EventType.RECORD_BTN) {
//            if (event.clear) {
//                if (record_btn != null) record_btn.setVisibility(View.VISIBLE);
//            } else {
//                if (record_btn != null) record_btn.setVisibility(View.INVISIBLE);
//            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        tv = findViewById(R.id.sample_text);
        mContext = this;
        login_btn = findViewById(R.id.login);
        uc_btn = findViewById(R.id.uc);
        stock_btn = findViewById(R.id.stock);
        ball_btn = findViewById(R.id.genDoubleBall);

        uidedt = findViewById(R.id.uid_et);
        pwdedt = findViewById(R.id.pswd_et);
//        record_btn = findViewById(R.id.record);
        odds_btn = findViewById(R.id.odds);
        records_btn = findViewById(R.id.records);
        stock2_btn = findViewById(R.id.stock2);
        stock2_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, Stock2Activity.class);
                startActivity(intent);
//                new Thread(runnableled).start();
            }
        });


        uidedt.setText(ShareP.getUidFromPref(this));
        pwdedt.setText(ShareP.getPwdFromPref(this));

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(runnable).start();
            }
        });

        ball_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(ballRunnable).start();
            }
        });
        odds_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, OddsActivity.class);
                startActivity(intent);
            }
        });
        records_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, RecordActivity.class);
                startActivity(intent);
            }
        });
//        record_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(Intent.ACTION_MAIN);
//                intent.addCategory(Intent.CATEGORY_LAUNCHER);
//                String pkg = "zj.zfenlly.tools";
//                String className = "zj.zfenlly.main.MainActivity";
//                ComponentName cn = new ComponentName(pkg, className);
//                intent.setComponent(cn);
//                startActivity(intent);
//            }
//        });

        uc_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String spec = "https://weibo.cn/u/" + second_uid;
                OpenWebView.open(MainActivity.this, spec);
            }
        });

        stock_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, StockActivity.class);
                startActivity(intent);
            }
        });
        // gua by linurm
        //LoadInjectLib.init(getPackageName());
        tv.setText(stringFromJNI());
//gua by linurm
        //MPermissions.requestPermission(this);
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

    public native long longFromJNI();

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
        Log.e("ztag", "resume");

//        if (allowWriteExternal()) {
//            ;//new Thread(runnable).start();
////            Log.e("ztag","ask   ....");
////        askForPermission();
//        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.option_normal_1:
                Intent intent = new Intent(this, RecordActivity.class);
                startActivity(intent);
                return true;
            case R.id.option_normal_2:
                return true;
            case R.id.option_normal_3:
                return true;
            case R.id.option_normal_4:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
