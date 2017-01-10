package com.gs.buluo.app.bean;

import java.util.List;

/**
 * Created by fs on 2016/12/13.
 */
public class PropertyManagement {
    public String id;
    public String communityName;
    public String companyName;
    public String applyPersonName;
    public String floor;
    public String fixProject;
    public String appointTime;
    public String problemDesc;
    public List<String> pictures;

    @Override
    public String toString() {
        return "PropertyManagement{" +
                "id='" + id + '\'' +
                ", communityName='" + communityName + '\'' +
                ", companyName='" + companyName + '\'' +
                ", applyPersonName='" + applyPersonName + '\'' +
                ", floor='" + floor + '\'' +
                ", fixProject='" + fixProject + '\'' +
                ", appointTime='" + appointTime + '\'' +
                ", problemDesc='" + problemDesc + '\'' +
                ", pictures=" + pictures +
                '}';
    }
}
