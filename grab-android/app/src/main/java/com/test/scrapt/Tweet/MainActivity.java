package com.test.scrapt.Tweet;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Proxy;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.test.scrapt.R;
import com.test.scrapt.Stock.StockActivity;
import com.test.scrapt.Wifi.WifiControl;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_ACCESS_EXTERNAL_STORAGE = 12334;

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    Proxy a = new Proxy();

    String spec;
    TextView tv = null;
    TextView uidtv = null;
    Button button = null;
    Button stock_btn = null;
    String uid;
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            String s;
            boolean isconnect = WifiControl.isConnect(MainActivity.this);

            if (isconnect == true) {
                GrabTweet a = new GrabTweet(MainActivity.this);

                a.login();
//            Log.e("ZTAG", "GrabTweet" + a.login_bak());
//                String proxy = Settings.Secure.getString((MainActivity.this).getContentResolver(), Settings.Secure.HTTP_PROXY);

                s = a.GrabTweetInfo(uid);
//                s += proxy + "\n";
                s += a.GrabTweetInfo("3373931552");
                s += a.GrabTweetInfo("1789247505");
            } else {
                s = "NetWork is disconnect!!!";
            }

            EventBus.getDefault().post(new MessageEvent(s));

        }
    };


    private boolean allowWriteExternal() {
        if (selfPermissionGranted(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != true) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_CODE_ACCESS_EXTERNAL_STORAGE);
            }

            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_ACCESS_EXTERNAL_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                ;//takePhoto();
                Toast.makeText(MainActivity.this, "Permission allowed", Toast.LENGTH_SHORT).show();
                new Thread(runnable).start();
            } else {
                // Permission Denied
                Toast.makeText(MainActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
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
    public void onMessageEvent(MessageEvent event) {
        tv.setText("" + event.message);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // Example of a call to a native method
        tv = findViewById(R.id.sample_text);
        tv.setText(stringFromJNI());

        button = findViewById(R.id.button);
        stock_btn = findViewById(R.id.stock);
//        button.setVisibility(View.INVISIBLE);

        uidtv = findViewById(R.id.textView2);
        uidtv.setText(ShareP.getUIDFromPref(this));
        uid = uidtv.getText().toString();


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String spec = "https://weibo.cn/u/" + uid + "?page=1";
                OpenWebView.open(MainActivity.this, spec);
//                WifiControl mwc = new WifiControl();
//                try {
//                    mwc.setHttpPorxySetting(MainActivity.this, "192.168.1.176", 8888);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
            }
        });

        stock_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                //指定intent要启动的类
                intent.setClass(MainActivity.this, StockActivity.class);
                startActivity(intent);
            }
        });



    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

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
        if (allowWriteExternal())
            new Thread(runnable).start();
    }


}
