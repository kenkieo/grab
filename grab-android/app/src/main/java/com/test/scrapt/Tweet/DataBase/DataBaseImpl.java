package com.test.scrapt.Tweet.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/11/15.
 */

public class DataBaseImpl {

    static final String DATABASENAME = "tweet.db";
    static final String DATABASEPATH = "xxx";

    static final String DATABASE_PATH_NAME = Environment.getExternalStorageDirectory() + "/"
            + DATABASEPATH + "/" + DATABASENAME;


    public static void insert(Context mContext, TweetNote mMsc) {
        SQLiteDatabase db;
        DaoMaster daoMaster;
        DaoSession daoSession;
        TweetNoteDao mscDao;

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(mContext, DATABASE_PATH_NAME, null);
        db = helper.getWritableDatabase();
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
        mscDao = daoSession.getTweetNoteDao();
        mscDao.insert(mMsc);
    }

    public static void delete(Context mContext, TweetNote mTweet) {
        SQLiteDatabase db;
        DaoMaster daoMaster;
        DaoSession daoSession;
        TweetNoteDao mscDao;

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(mContext, DATABASE_PATH_NAME, null);
        db = helper.getWritableDatabase();
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
        mscDao = daoSession.getTweetNoteDao();
        mscDao.deleteByKey(mTweet.getId());
    }

    public static TweetNote getCurrTweet(Context mContext) {
        SQLiteDatabase db;
        DaoMaster daoMaster;
        DaoSession daoSession;
        TweetNoteDao mscDao;
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(mContext, DATABASE_PATH_NAME, null);
        db = helper.getReadableDatabase();
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
        mscDao = daoSession.getTweetNoteDao();
        QueryBuilder<TweetNote> qb = mscDao.queryBuilder().orderAsc(TweetNoteDao.Properties.Date);
        TweetNote mTweet = null;
        if (qb.list().size() >= 1)
            mTweet = qb.list().get(qb.list().size() - 1);
        return mTweet;
    }

    public static TweetNote getTweet(Context mContext, int id) {
        SQLiteDatabase db;
        DaoMaster daoMaster;
        DaoSession daoSession;
        TweetNoteDao mscDao;
        if (id < 0) return null;
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(mContext, DATABASE_PATH_NAME, null);
        db = helper.getReadableDatabase();
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
        mscDao = daoSession.getTweetNoteDao();
        QueryBuilder<TweetNote> qb = mscDao.queryBuilder().orderAsc(TweetNoteDao.Properties.Date);
        TweetNote mTweet = null;
        if (qb.list().size() >= 1) {
            //mWB = qb.list().get(id);
            int len = qb.list().size();
            mTweet = qb.list().get(len - id - 1);
        }
        return mTweet;
    }

    public static TweetNote getTweet(Context mContext, String uid) {
        List<TweetNote> list = getListTweet(mContext);
        TweetNote a;
        for (int i = 0; i < list.size(); i++) {
            a = list.get(i);
            if (a.getUid().equals(uid)) {
                return a;
            }
        }

        return null;
    }

    public static List<TweetNote> getListTweet(Context mContext) {
        SQLiteDatabase db;
        DaoMaster daoMaster;
        DaoSession daoSession;
        TweetNoteDao mscDao;
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(mContext, DATABASE_PATH_NAME, null);
        db = helper.getReadableDatabase();
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
        mscDao = daoSession.getTweetNoteDao();
        QueryBuilder<TweetNote> qb = mscDao.queryBuilder().orderAsc(TweetNoteDao.Properties.Date);
//            TweetNote mTweet = null;
//            if (qb.list().size() >= 1)
//                mTweet = qb.list().get(qb.list().size() - 1);

        List<TweetNote> goods = new ArrayList<TweetNote>();
        List<TweetNote> tmpgoods = mscDao.queryBuilder().orderAsc(TweetNoteDao.Properties.Date).list();
        int len = tmpgoods.size();
        for (int i = len - 1; i >= 0; i--) {
            goods.add(tmpgoods.get(i));
        }
        return goods;
    }

}
