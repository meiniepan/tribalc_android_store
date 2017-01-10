package com.gs.buluo.store.bean;

import com.gs.buluo.store.bean.ResponseBody.IBaseResponse;

import java.util.List;

/**
 * Created by hjn on 2016/11/28.
 */
public class CommunityDetail extends CommunityPlate implements IBaseResponse {
    public List<String> pictures;
    public String district;
    public String desc;
    public String map;
    public List<ListStore> repastList;
    public List<ListStore> entertainmentList;
}
