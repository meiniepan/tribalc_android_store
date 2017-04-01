package com.gs.buluo.store.dao;

import com.gs.buluo.store.TribeApplication;
import com.gs.buluo.store.bean.StoreInfo;

import org.xutils.DbManager;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;
import org.xutils.x;

/**
 * Created by hjn on 2016/11/3.
 */
public class StoreInfoDao {
    private DbManager db;

    public StoreInfoDao() {
        db = x.getDb(TribeApplication.getInstance().getDaoConfig());
    }

    public void clear() {
        try {
            db.delete(StoreInfo.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    public void saveBindingId(StoreInfo userInfo) {
        try {
            TribeApplication.getInstance().setUserInfo(userInfo);
            db.saveBindingId(userInfo);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    public void update(StoreInfo userInfo) {
        TribeApplication.getInstance().setUserInfo(userInfo);
        try {
            db.update(userInfo);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    public StoreInfo findFirst() {
        try {
            return db.findFirst(StoreInfo.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
        return null;
    }

    public StoreInfo find(String assigned) {
        try {
            return db.selector(StoreInfo.class).where(WhereBuilder.b("uid", "=", assigned)).findFirst();
        } catch (DbException e) {
            e.printStackTrace();
        }
        return null;
    }
}
