package zj.test.scrapt;

import android.app.Application;



/**
 * Created by Administrator on 2017/11/15.
 */

public class ScraptApplication extends Application {
    public static final boolean ENCRYPTED = false;

//    private DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();
//        initDataBase();
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(this);
    }

//    public DaoSession getDaoSession() {
//        return daoSession;
//    }
//
//    private void initDataBase() {
//        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, ENCRYPTED ? "notes-db-encrypted" : "notes-db");
//        Database db = ENCRYPTED ? helper.getEncryptedWritableDb("super-secret") : helper.getWritableDb();
//        daoSession = new DaoMaster(db).newSession();
//    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
