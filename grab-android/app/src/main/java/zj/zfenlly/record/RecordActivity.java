package zj.zfenlly.record;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;


import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.List;

import zj.test.scrapt.R;
import zj.test.scrapt.Tweet.DataBase.DataBaseImpl;
import zj.test.scrapt.Tweet.DataBase.TweetNote;
import zj.zfenlly.record.daodb.MSC;
import zj.zfenlly.record.daodb.DataBaseImpl.MSCDataBaseOp;

import static zj.test.scrapt.Tweet.UserID.second_uid;
import static zj.test.scrapt.Tweet.UserID.third_uid;

@ContentView(R.layout.fragment_record)
public class RecordActivity extends Activity {

    private final String TAG = this.getClass().getSimpleName();

    @ViewInject(R.id.music_seven_day_minites)
    TextView mMusic7DayMinites;
    @ViewInject(R.id.mscListView)
    ListView mMscListView;

    @ViewInject(R.id.weibo_score)
    TextView mWeiboScore;
    @ViewInject(R.id.next_score)
    TextView mNextScore;
    @ViewInject(R.id.weibo_level)
    TextView mWeiboLevel;
    @ViewInject(R.id.wbListView)
    ListView mWbListView;

    @ViewInject(R.id.weibo2_score)
    TextView mWeibo2Score;
    @ViewInject(R.id.next2_score)
    TextView mNext2Score;
    @ViewInject(R.id.weibo2_level)
    TextView mWeibo2Level;
    @ViewInject(R.id.wb2ListView)
    ListView mWb2ListView;

    WBDataAdapter mWBDataAdapter;
    WBDataAdapter mWB2DataAdapter;
    MSCDataAdapter mMSCDataAdapter;
    Activity mActivity = null;


    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    @OnClick(R.id.mscEdit_btn)
    public void mscEdit_btn(View v) {
        final MSCDialog.Builder builder = new MSCDialog.Builder(this);
        //builder.setMessage("MSC");
        builder.setTitle("MSC");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //builder.addMSCDate();
                ((MSCDialog) dialog).addMSCDate();
                dialog.dismiss();
                freshDBView();
                //设置你的操作事项
            }
        });

        builder.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.setDatePickerButton("data picker", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ((MSCDialog) dialog).openDialogDate();
            }
        });
        builder.create().show();
    }

    @OnClick(R.id.wbEdit_btn)
    public void wbEdit_btn(View v) {
        WBDialog.Builder builder = new WBDialog.Builder(this);
        builder.setTitle("WB");
        //builder.setDateButton("",null);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //builder.addMSCDate();//.addMSCDate();
                ((WBDialog) dialog).addWBDate();
                dialog.dismiss();
                freshDBView();
                //设置你的操作事项
            }
        });
        builder.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.setDatePickerButton("data picker", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ((WBDialog) dialog).openDialogDate();
            }
        });
        builder.create().show();
    }

    @OnClick(R.id.wb2Edit_btn)
    public void wb2Edit_btn(View v) {
//        WB2Dialog.Builder builder = new WB2Dialog.Builder(this);
//        builder.setTitle("WB2");
//        //builder.setDateButton("",null);
//        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//                //builder.addMSCDate();//.addMSCDate();
//                ((WB2Dialog) dialog).addWBDate();
//                dialog.dismiss();
//                freshDBView();
//                //设置你的操作事项
//            }
//        });
//        builder.setNegativeButton("取消",
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//        builder.setDatePickerButton("data picker", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                ((WB2Dialog) dialog).openDialogDate();
//            }
//        });
//        builder.create().show();
    }

    private void print(String msg) {
        Log.e(TAG, msg);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtils.inject(this);
        mActivity = this;
        print("onCreate");
        print("" + dip2px(this, 128));

        mWBDataAdapter = new WBDataAdapter(getApplicationContext(), R.layout.wb_item_list);
        mWbListView.setAdapter(mWBDataAdapter);
        mWbListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                //DataBaseImpl.MSCDataBaseOp mMSCOp = new DataBaseImpl.MSCDataBaseOp();
                final int key = (int) arg3;
                List<TweetNote> ltn = DataBaseImpl.getListTweet(getApplicationContext(), second_uid);
                print(ltn.get(key).toString());
                final TweetNote tn = ltn.get(key);
                final AlertDialog dialog = new AlertDialog.Builder(mActivity)
                        .setTitle(second_uid)//在这里把写好的这个listview的布局加载dialog中
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
//                                print(":" + key);
                                print(tn.toString());
                                DataBaseImpl.delete(getApplicationContext(), tn);
                                dialog.dismiss();
                                freshDBView();
                            }
                        })
                        .setMessage(tn.toString())
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                                dialog.cancel();
                            }
                        }).create();
                dialog.setCanceledOnTouchOutside(false);//使除了dialog以外的地方不能被点击
                dialog.show();
            }
        });

        mWB2DataAdapter = new WBDataAdapter(getApplicationContext(), R.layout.wb_item_list);
        mWb2ListView.setAdapter(mWB2DataAdapter);
        mWb2ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                //DataBaseImpl.MSCDataBaseOp mMSCOp = new DataBaseImpl.MSCDataBaseOp();
                final int key = (int) arg3;
                List<TweetNote> ltn = DataBaseImpl.getListTweet(getApplicationContext(), third_uid);
                print(ltn.get(key).toString());
                final TweetNote tn = ltn.get(key);
                final AlertDialog dialog = new AlertDialog.Builder(mActivity)
                        .setTitle(third_uid)//在这里把写好的这个listview的布局加载dialog中
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
//                                print(":" + key);
                                DataBaseImpl.delete(getApplicationContext(), tn);
                                dialog.dismiss();
                                freshDBView();
                            }
                        })
                        .setMessage(tn.toString())
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                                dialog.cancel();
                            }
                        }).create();
                dialog.setCanceledOnTouchOutside(false);//使除了dialog以外的地方不能被点击
                dialog.show();
            }
        });

        mMSCDataAdapter = new MSCDataAdapter(this.getApplicationContext(), R.layout.msc_item_list);
        mMSCDataAdapter.setAliases(MSCDataBaseOp.getListMSC(this));
        mMscListView.setAdapter(mMSCDataAdapter);
        mMscListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                //DataBaseImpl.MSCDataBaseOp mMSCOp = new DataBaseImpl.MSCDataBaseOp();
                final int key = (int) arg3;
                final MSC mmsc = MSCDataBaseOp.getMSC(getApplicationContext(), key);
                final AlertDialog dialog = new AlertDialog.Builder(mActivity)
                        .setTitle("MSC")//在这里把写好的这个listview的布局加载dialog中
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
//                                print(":" + key);
                                MSCDataBaseOp.delete(getApplicationContext(), mmsc);
                                freshDBView();
                            }
                        })
                        .setMessage(mmsc.toString())
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                                dialog.cancel();
                            }
                        }).create();
                dialog.setCanceledOnTouchOutside(false);//使除了dialog以外的地方不能被点击
                dialog.show();
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public void freshDBView() {
        MSC mMSC = zj.zfenlly.record.daodb.DataBaseImpl.MSCDataBaseOp.getCurrMSC(getApplicationContext());
        if (mMSC != null) {
            mMusic7DayMinites.setText(mMSC.getLast7minites()); //.setText(mMSC.getLast7minites() != null ? mMSC.getLast7minites().toString() : "e");
            mMSCDataAdapter.setAliases(zj.zfenlly.record.daodb.DataBaseImpl.MSCDataBaseOp.getListMSC(getApplicationContext()));
        }

        TweetNote tn = DataBaseImpl.getCurrTweet(getApplicationContext(), second_uid);
        if (tn != null) {
            mWeiboScore.setText(tn.getIntegral());
            mNextScore.setText(CaculateLevel.getNextLevelScore(tn.getIntegral()));
            String lv = CaculateLevel.getCaculateLevel(tn.getIntegral());
            mWeiboLevel.setText(lv);
            mWBDataAdapter.setAliases(DataBaseImpl.getListTweet(getApplicationContext(), second_uid));
        }

        TweetNote tn2 = DataBaseImpl.getCurrTweet(getApplicationContext(), third_uid);
        if (tn2 != null) {
            mWeibo2Score.setText(tn2.getIntegral());
            mNext2Score.setText(CaculateLevel.getNextLevelScore(tn2.getIntegral()));
            String lv = CaculateLevel.getCaculateLevel(tn2.getIntegral());
            mWeibo2Level.setText(lv);
            mWB2DataAdapter.setAliases(DataBaseImpl.getListTweet(getApplicationContext(), third_uid));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        freshDBView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void showDialog(View mView) {
        DialogFragment newFragment = MyAlertDialogFragment.newInstance(
                R.string.alert_dialog_two_buttons_title, mView);
        newFragment.show(getFragmentManager(), "dialog");
    }
}
