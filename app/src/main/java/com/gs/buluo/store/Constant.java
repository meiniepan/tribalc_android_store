package com.gs.buluo.store;

import android.os.Environment;

/**
 * Created by admin on 2016/11/1.
 */
public class Constant {
    public static final String VCODE = "vcode";
    public static final String ENVIRONMENT = "environment";
    public static final String GOODS = "GOODS";
    public static final String SET_MEAL = "SET_MEAL";
    public static final String AUTH = "auth";
    public static final String SERVE_TIME = "serve_time";
    public static final String STANDARD_INFO = "standard_info";
    public static final String PUBLISHED = "published";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";
    public static final String NOT_START = "NOT_START";
    public static final String SUCCEED = "SUCCESS";
    public static final String FAILURE = "FAILURE";

    public static final String UPDATE_TIME = "UPDATE_TIME";
    public static final String APK_URL = "http://www.buluo-gs.com/app-store.apk";
    public static final String DIR_PATH = Environment.getExternalStorageDirectory().toString() + "/tribe_store/";
    public static final String RE_LOGIN = "re_login";
    public static final String WALLET_AMOUNT = "wallet_amount";
    public static final String CASH_FLAG = "cash_flag";
    public static final String BANK_CARD = "bank_card";
    public static final String POUNDAGE = "poundage";
    public static final String FACE = "face";
    public static final String CANCEL_UPDATE_VERSION = "cancel_update";
    public static final String PAY_SIGN = "pay://stores/";
    public static final String APP_START = "app_start";
    public static final String WITHDRAW_FLAG = "withdraw_flag";
    public static final String WITHDRAW_BILL = "withdraw_bill";
    public static final String WEB_URL = "web_url";
    public static final String GOODS_SWITCH = "goods_switch";
    public static final String WX_CODE = "wx_code";
    public static final String BILL_ID = "bill_id";
    public static final String ORDER_ID = "order_id";

    public final static class Base {
        public static final String BASE = "https://app-services.buluo-gs.com/";
        public static final String BASE_URL = BuildConfig.API_SERVER_URL;
        public static final String BASE_IMG_URL = "http://dev-app-services.buluo-gs.com/resources/";   //图片地址要加此前缀
        public static final String BASE_ONLINE_URL = "http://pictures.buluo-gs.com/";   //阿里云图片地址base
        public static final String WX_ID = "wx9d6532f6628e8fe5";
    }

    public static final String VERIFICATION = "verificationCode";
    public static final String NAME = "name";
    public static final String LINKMAN = "linkman";
    public static final String LOGO = "logo";
    public static final String BIRTHDAY = "birthday";
    public static final String AREA = "province,city,district";
    public static final String PHONE = "phone";
    public static final String ADDRESS = "address";
    public static final String ADDRESS_ID = "address_id";
    public static final String WALLET_PWD = "wallet_pwd";
    public static final String OLD_PWD = "old_pwd";

    public static final String BILL = "bill_entity";
    public static final String GOODS_ID = "goods_id";
    public static final String ORDER = "order";
    public static final int REQUEST_ADDRESS = 208;
    public static final String RECEIVER = "receiver";

    public static final String TYPE = "types";
    public static final String COMMUNITY_ID = "community_id";
    public static final String COMMUNITY_NAME = "community_name";

    public static final String REPAST = "repast";
    public static final String ENTERTAINMENT_ALL = "HAIRDRESSING,FITNESS,ENTERTAINMENT,KEEPHEALTHY";
    public static final String SORT_POPULAR = "popularValue,desc";
    public static final String SORT_PERSON_EXPENSE_DESC = "personExpense,desc";
    public static final String SORT_PERSON_EXPENSE_ASC = "personExpense,asc";
    public static final String SORT_COORDINATE_DESC = "coordinate,desc";
    public static final String SERVE_ID = "serve_id";
    public static final String STORE_ID = "store_id";


    public final static class ForIntent {
        public static final int REQUEST_CODE = 0;
        public static final String FLAG = "flag";
        public static final String INTRODUCTION = "introduction";
        public static final String FROM_ORDER = "fromOrder";
        public static final String MODIFY = "self_modify";
        public static final String PHOTO_TYPE = "photo_type";
        public static final String STORE_BEAN = "store_bean";
        public static final String STORE_NAME = "store_name";
        public static final String GOODS_BEAN = "goods_bean";
        public static final String META = "goods_meta";
        public static final String GOODS_CATEGORY = "goods_category";
        public static final String GOODS_STANDARD = "goods_standard";
        public static final String STATUS = "status";
        public static final String KEY = "key";
        public static final String COORDINATE = "coordinate";
        public static final String FANCILITY = "facility";
        public static final String SERVE_POSITION = "serve_position";
    }

}
