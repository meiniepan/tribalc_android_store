package com.gs.buluo.store.bean;

/**
 * Created by hjn on 2017/7/14.
 */

public class Privilege {
    public String id;
    public String ownerId;
    public PrivilegeType type;
    public String condition;
    public String value;
    public int[] activityTime;
    public long startDate;
    public long endDate;

    public enum PrivilegeType {
        DISCOUNT, REDUCE, ALIQUOT
    }

}
