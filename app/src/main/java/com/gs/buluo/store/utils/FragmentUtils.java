package com.gs.buluo.store.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.gs.buluo.store.R;

/**
 * Created by hjn on 2016/11/2.
 */
public class FragmentUtils {

    public static void replaceFragmentWithStacks(FragmentActivity context, Fragment newFragment){
        FragmentTransaction transaction =context.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.mine_container,newFragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }
}
