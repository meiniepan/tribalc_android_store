package com.gs.buluo.store.bean.ResponseBody;

import com.gs.buluo.store.bean.ListReservation;

import java.util.List;

/**
 * Created by hjn on 2016/11/29.
 */
public class ReserveResponse {
    public int code;
    public ReserveResponseBody data;

    public class ReserveResponseBody{
        public String category;
        public String sort;
        public String prevSkip;
        public String nextSkip;
        public boolean hasMore;
        public List<ListReservation> content;
    }
}
