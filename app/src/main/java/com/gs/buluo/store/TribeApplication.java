package com.gs.buluo.store;


import android.content.Context;
import android.support.multidex.MultiDex;

import com.gs.buluo.common.BaseApplication;
import com.gs.buluo.common.utils.TribeCrashCollector;
import com.gs.buluo.store.bean.HomeMessageEnum;
import com.gs.buluo.store.bean.StoreInfo;
import com.gs.buluo.store.bean.UnReadMessageBean;
import com.gs.buluo.store.dao.StoreInfoDao;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.HashMap;

//import com.squareup.leakcanary.LeakCanary;


/**
 * Created by hjn on 2016/11/1.
 */
public class TribeApplication extends BaseApplication {
    private static TribeApplication instance;
    private DbManager.DaoConfig daoConfig;
    private StoreInfo user;
    private String pwd;
//    private LatLng positon;

    @Override
    public void onCreate() {
        super.onCreate();
//        SDKInitializer.initialize(this);  //map initialize
        initCrashCollect();
        instance = this;
        x.Ext.init(this);//X utils初始化
//        x.Ext.setDebug(BuildConfig.DEBUG);
//        LeakCanary.install(this);
        initDb();
    }

    private void initCrashCollect() {
        if (Constant.Base.BASE_URL.contains("dev"))
            TribeCrashCollector.getIns(getApplicationContext());
    }

    private void initDb() {
        daoConfig = new DbManager.DaoConfig()
                .setDbName("store")
                .setDbVersion(3)
                .setDbOpenListener(new DbManager.DbOpenListener() {
                    @Override
                    public void onDbOpened(DbManager db) {
                        db.getDatabase().enableWriteAheadLogging(); //WAL
                    }
                })
                .setDbUpgradeListener(new DbManager.DbUpgradeListener() { //更新数据库
                    @Override
                    public void onUpgrade(DbManager db, int oldVersion, int newVersion) {
                        if (oldVersion == 1) {
                            update1To2(db);
                        } else if (oldVersion == 2) {
                            update2To3(db);
                        }
                    }
                });
    }

    private void update1To2(DbManager db) {
        try {
            db.addColumn(StoreInfo.class, "type");
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    private void update2To3(DbManager db) {
        try {
            db.addColumn(StoreInfo.class, "phone");
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    public static synchronized TribeApplication getInstance() {
        return instance;
    }

    @Override
    public void onInitialize() {

    }

    @Override
    public String getFilePath() {
        return Constant.DIR_PATH;
    }

    public DbManager.DaoConfig getDaoConfig() {
        return daoConfig;
    }

    public void setUserInfo(StoreInfo info) {
        user = info;
    }

    public StoreInfo getUserInfo() {
        if (user == null) {
            user = new StoreInfoDao().findFirst();
        }
        return user;
    }

//    public LatLng getPosition() {
//        return positon;
//    }
//
//    public void setPosition(LatLng latLng) {
//        positon = latLng;
//    }

    private boolean bf_recharge;
    private boolean bf_withdraw;

    public boolean isBf_recharge() {
        return bf_recharge;
    }

    public void setBf_recharge(boolean bf_recharge) {
        this.bf_recharge = bf_recharge;
    }

    public boolean isBf_withdraw() {
        return bf_withdraw;
    }

    public void setBf_withdraw(boolean bf_withdraw) {
        this.bf_withdraw = bf_withdraw;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    private HashMap<HomeMessageEnum, Integer> messageMap;
    private int totalCount = 0;

    public void setMessageMap(UnReadMessageBean messageMap) {
        totalCount = messageMap.totalCount;
        this.messageMap = messageMap.messageTypeCount;
    }

    public HashMap<HomeMessageEnum, Integer> getMessageMap() {
        if (messageMap == null) {
            return new HashMap<>();
        }
        return messageMap;
    }

    public int getUnReadAmount() {
        return totalCount;
    }
}
