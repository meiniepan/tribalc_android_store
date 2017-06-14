package com.gs.buluo.store;


import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.model.LatLng;
import com.gs.buluo.common.BaseApplication;
import com.gs.buluo.store.bean.StoreInfo;
import com.gs.buluo.common.utils.TribeCrashCollector;
import com.gs.buluo.store.dao.StoreInfoDao;

import org.xutils.DbManager;
import org.xutils.x;


/**
 * Created by hjn on 2016/11/1.
 */
public class TribeApplication extends BaseApplication {
    private static TribeApplication instance;
    private DbManager.DaoConfig daoConfig;
    private StoreInfo user;
    private LatLng positon;
    @Override
    public void onCreate() {
        super.onCreate();
        SDKInitializer.initialize(this);  //map initialize
        initCrashCollect();
        instance = this;
        x.Ext.init(this);//X utils初始化
//        x.Ext.setDebug(BuildConfig.DEBUG);
        initDb();
    }

    private void initCrashCollect() {
        if (Constant.Base.BASE_URL.contains("dev"))
            TribeCrashCollector.getIns(getApplicationContext());
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
        if (user==null){
            user=new StoreInfoDao().findFirst();
        }
        return user;
    }

    public LatLng getPosition() {
        return positon;
    }

    public void setPosition(LatLng latLng){
        positon =latLng;
    }

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
}
