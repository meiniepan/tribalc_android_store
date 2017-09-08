package com.gs.buluo.store.dao;

import com.gs.buluo.store.TribeApplication;
import com.gs.buluo.store.bean.StoreAccount;

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
            db.delete(StoreAccount.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    public void saveBindingId(StoreAccount userInfo) {
        try {
            TribeApplication.getInstance().setUserInfo(userInfo);
            db.saveBindingId(userInfo);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    public void update(StoreAccount userInfo) {
        TribeApplication.getInstance().setUserInfo(userInfo);
        try {
            db.update(userInfo);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    public StoreAccount findFirst() {
        try {
            return db.findFirst(StoreAccount.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
        return null;
    }

    public StoreAccount find(String assigned) {
        try {
            return db.selector(StoreAccount.class).where(WhereBuilder.b("uid", "=", assigned)).findFirst();
        } catch (DbException e) {
            e.printStackTrace();
        }
        return null;
    }
}
