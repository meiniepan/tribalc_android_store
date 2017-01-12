package com.gs.buluo.store;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.gs.buluo.store.bean.StoreInfo;

import org.greenrobot.eventbus.EventBus;
import org.xutils.DbManager;
import org.xutils.x;

/**
 * Created by hjn on 2016/11/1.
 */
public class TribeApplication extends Application {
    private static TribeApplication instance;
    private DbManager.DaoConfig daoConfig;
    private StoreInfo user;

    @Override
    public void onCreate() {
        super.onCreate();
        SDKInitializer.initialize(this);  //map initialize
        instance = this;
        x.Ext.init(this);//X utils初始化
        Fresco.initialize(this);
//        x.Ext.setDebug(BuildConfig.DEBUG);
        initDb();

        EventBus.getDefault();
    }

    private void initDb() {
        daoConfig = new DbManager.DaoConfig()
                .setDbName("store")
                .setDbOpenListener(new DbManager.DbOpenListener() {
                    @Override
                    public void onDbOpened(DbManager db) {
                        db.getDatabase().enableWriteAheadLogging(); //WAL
                    }
                })
                .setDbUpgradeListener(new DbManager.DbUpgradeListener() { //更新数据库
                    @Override
                    public void onUpgrade(DbManager db, int oldVersion, int newVersion) {
                    }
                });
    }

    public static synchronized TribeApplication getInstance() {
        return instance;
    }

    public DbManager.DaoConfig getDaoConfig() {
        return daoConfig;
    }

    public void setUserInfo(StoreInfo info) {
        user = info;
    }

    public StoreInfo getUserInfo() {
        return user;
    }
}
