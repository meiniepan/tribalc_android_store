package com.gs.buluo.store.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.gs.buluo.store.Constant;
import com.gs.buluo.store.R;
import com.gs.buluo.common.utils.ToastUtils;
import com.gs.buluo.store.view.widget.panel.AddressPickPanel;

import butterknife.Bind;

/**
 * Created by hjn on 2017/1/18.
 */
public class CreateStoreAddressActivity extends BaseActivity implements OnGetGeoCoderResultListener {
    @Bind(R.id.create_map)
    MapView mapView;
    @Bind(R.id.create_address_area)
    TextView tvAddress;
    @Bind(R.id.create_address_edit)
    EditText edAddress;

    BaiduMap mBaiduMap;
    LocationClient mLocClient;
    GeoCoder mSearch = null;
    boolean isFirstLoc = true; // 是否首次定位
    MapLocationListenner myListener = new MapLocationListenner();
    private LatLng currentPt;

    Context mCtx;
    private String chooseCity;
    private double latitude;
    private double longitude;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        mCtx = this;
        initMap();
        findViewById(R.id.create_map_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tvAddress.length()==0||edAddress.length()==0)return;
                Intent intent=new Intent();
                intent.putExtra(Constant.AREA,tvAddress.getText().toString().trim());
                intent.putExtra(Constant.ADDRESS,edAddress.getText().toString().trim());
                intent.putExtra(Constant.LATITUDE,latitude);
                intent.putExtra(Constant.LONGITUDE,longitude);
                setResult(RESULT_OK,intent);
                finish();
            }
        });
        findViewById(R.id.ll_create_address).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initAddressPicker();
            }
        });
        findView(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.create_address_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tvAddress.length()==0||edAddress.length()==0){
                    ToastUtils.ToastMessage(mCtx,"请选择和输入地址");
                    return;
                }
                mSearch.geocode(new GeoCodeOption().city(
                        chooseCity).address(edAddress.getText().toString().trim()));
            }
        });

        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mSearch.reverseGeoCode(new ReverseGeoCodeOption()
                        .location(latLng));
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                return false;
            }
        });
    }

    private void initMap() {
        mBaiduMap = mapView.getMap();
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        // 定位初始化
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        mLocClient.setLocOption(option);
        mLocClient.start();
        mSearch = GeoCoder.newInstance();
        mSearch.setOnGetGeoCodeResultListener(this);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_create_address;
    }

    private void initAddressPicker() {
        AddressPickPanel pickPanel = new AddressPickPanel(this, new AddressPickPanel.OnSelectedFinished() {
            @Override
            public void onSelected(String area, String province, String city, String district) {
                tvAddress.setText(area);
                chooseCity = city;
            }
        });
        pickPanel.show();
    }

    @Override
    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
        if (geoCodeResult == null || geoCodeResult.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(getCtx(), "抱歉，未能找到结果", Toast.LENGTH_LONG)
                    .show();
            return;
        }
        mBaiduMap.clear();
        mBaiduMap.addOverlay(new MarkerOptions().position(geoCodeResult.getLocation())
                .icon(BitmapDescriptorFactory
                        .fromResource(R.mipmap.icon_marka)));
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(geoCodeResult
                .getLocation()));
        latitude = geoCodeResult.getLocation().latitude;
        longitude =  geoCodeResult.getLocation().longitude;
        String strInfo = String.format("纬度：%f 经度：%f",
                latitude,longitude);
    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
        if (reverseGeoCodeResult == null || reverseGeoCodeResult.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(mCtx, "抱歉，未能找到结果", Toast.LENGTH_LONG)
                    .show();
            return;
        }
        mBaiduMap.clear();
        LatLng location = reverseGeoCodeResult.getLocation();
        latitude = location.latitude;
        longitude = location.longitude;
        mBaiduMap.addOverlay(new MarkerOptions().position(location)
                .icon(BitmapDescriptorFactory
                        .fromResource(R.mipmap.location)));
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(location));
        ReverseGeoCodeResult.AddressComponent addressDetail = reverseGeoCodeResult.getAddressDetail();
        chooseCity = addressDetail.city;
        tvAddress.setText(addressDetail.province+"-"+addressDetail.city+"-"+addressDetail.district);
        edAddress.setText(addressDetail.street+addressDetail.streetNumber);
        edAddress.setSelection(edAddress.length());
    }

    /*
    * 定位SDK监听函数
    */
    public class MapLocationListenner implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mapView == null) {
                return;
            }
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
                mSearch.reverseGeoCode(new ReverseGeoCodeOption()
                        .location(ll));
            }
        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }
}
