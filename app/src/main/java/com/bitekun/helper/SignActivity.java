package com.bitekun.helper;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.AMapLocationQualityReport;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.bitekun.helper.util.Urls;
import com.bitekun.helper.util.Utils;
import com.github.dfqin.grantor.PermissionListener;
import com.github.dfqin.grantor.PermissionsUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class SignActivity extends Activity implements
		View.OnClickListener, LocationSource,AMapLocationListener  {


	Button btn_sign;
	//显示地图需要的变量
	private MapView mapView;//地图控件
	private AMap aMap;//地图对象


	//定位需要的声明
	private AMapLocationClient mLocationClient = null;//定位发起端
	private AMapLocationClientOption mLocationOption = null;//定位参数
	private OnLocationChangedListener mListener = null;//定位监听器

	//标识，用于判断是否只显示一次定位信息和用户重新定位
	private boolean isFirstLoc = true;
    TextView tv_loc;

    double lat,lon=0.0;
    // 声明一个数组，用来存储所有需要动态申请的权限
    String[] permissions = new String[]{
            //Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            //Manifest.permission.WRITE_EXTERNAL_STORAGE,
            //Manifest.permission.READ_EXTERNAL_STORAGE,
            //Manifest.permission.READ_PHONE_STATE,
            };

    // 声明一个集合，在后面的代码中用来存储用户拒绝授权的权
    List<String> mPermissionList = new ArrayList<>();
    boolean mShowRequestPermission = true;//用户是否禁止权限
    private void checkPermission() {
        mPermissionList.clear();
        /**
         * 判断哪些权限未授予
         * 以便必要的时候重新申请
         */
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(SignActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {
                mPermissionList.add(permission);
            }
        }
        /**
         * 判断存储委授予权限的集合是否为空
         */
        if (!mPermissionList.isEmpty()) {
            Toast.makeText(SignActivity.this,"授权项需要全部满足",Toast.LENGTH_SHORT).show();
            // 后续操作...
        } else {//未授予的权限为空，表示都授予了
            // 后续操作...
            initLoc();
            Toast.makeText(SignActivity.this,"授权成功",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        //判断是否勾选禁止后不再询问
                        boolean showRequestPermission = ActivityCompat.shouldShowRequestPermissionRationale(SignActivity.this, permissions[i]);
                        if (showRequestPermission) {
                            // 后续操作...
                        } else {
                            // 后续操作...
                        }
                    }
                }
                // 授权结束后的后续操作...
                break;
            default:
                break;
        }
    }




	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign);
		ImageView iv_back;
		tv_loc = (TextView)findViewById(R.id.tv_loc);
		iv_back = (ImageView)findViewById(R.id.iv_back);
		iv_back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();

			}
		});

		btn_sign = (Button)findViewById(R.id.btn_sign);
		btn_sign.setOnClickListener(this);

		//显示地图
		mapView = (MapView) findViewById(R.id.map);
		//必须要写
		mapView.onCreate(savedInstanceState);
		//获取地图对象
		aMap = mapView.getMap();


		//设置显示定位按钮 并且可以点击
		UiSettings settings = aMap.getUiSettings();
		//设置定位监听
		aMap.setLocationSource(this);
		// 是否显示定位按钮
		settings.setMyLocationButtonEnabled(true);
		// 是否可触发定位并显示定位层
		aMap.setMyLocationEnabled(true);


		//定位的小图标 默认是蓝点 这里自定义一团火，其实就是一张图片
		MyLocationStyle myLocationStyle = new MyLocationStyle();
		myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.loc));
		myLocationStyle.radiusFillColor(android.R.color.transparent);
		myLocationStyle.strokeColor(android.R.color.transparent);
		aMap.setMyLocationStyle(myLocationStyle);

        //checkPermission();

		//开始定位
		//initLoc();

        requestLocation();


	}

	//定位
	private void initLoc() {
		//初始化定位
		mLocationClient = new AMapLocationClient(getApplicationContext());
		//设置定位回调监听
		mLocationClient.setLocationListener(this);
		//初始化定位参数
		mLocationOption = new AMapLocationClientOption();
		//设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
		mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
		//设置是否返回地址信息（默认返回地址信息）
		mLocationOption.setNeedAddress(true);
		//设置是否只定位一次,默认为false
		mLocationOption.setOnceLocation(false);
		//设置是否强制刷新WIFI，默认为强制刷新
		mLocationOption.setWifiActiveScan(true);
		//设置是否允许模拟位置,默认为false，不允许模拟位置
		mLocationOption.setMockEnable(false);
		//设置定位间隔,单位毫秒,默认为2000ms
		mLocationOption.setInterval(2000);
		//给定位客户端对象设置定位参数
		mLocationClient.setLocationOption(mLocationOption);
		//启动定位
		mLocationClient.startLocation();
	}


	//定位回调函数
	@Override
	public void onLocationChanged(AMapLocation amapLocation) {

		if (amapLocation != null) {
			if (amapLocation.getErrorCode() == 0) {
				//定位成功回调信息，设置相关消息
				amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见官方定位类型表
				amapLocation.getLatitude();//获取纬度
				lat = amapLocation.getLatitude();
                lon = amapLocation.getLongitude();

				amapLocation.getLongitude();//获取经度
				amapLocation.getAccuracy();//获取精度信息
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date date = new Date(amapLocation.getTime());
				df.format(date);//定位时间
				amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
				amapLocation.getCountry();//国家信息
				amapLocation.getProvince();//省信息
				amapLocation.getCity();//城市信息
				amapLocation.getDistrict();//城区信息
				amapLocation.getStreet();//街道信息
				amapLocation.getStreetNum();//街道门牌号信息
				amapLocation.getCityCode();//城市编码
				amapLocation.getAdCode();//地区编码
                tv_loc.setText( "当前位置:"+amapLocation.getProvince() + "" + amapLocation.getCity() + "" + amapLocation.getProvince() + "" + amapLocation.getDistrict() + "" + amapLocation.getStreet() + "" + amapLocation.getStreetNum());
				// 如果不设置标志位，此时再拖动地图时，它会不断将地图移动到当前的位置
				if (isFirstLoc) {
					//设置缩放级别
					aMap.moveCamera(CameraUpdateFactory.zoomTo(17));
					//将地图移动到定位点
					aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude())));
					//点击定位按钮 能够将地图的中心移动到定位点
					mListener.onLocationChanged(amapLocation);
					//添加图钉
					//aMap.addMarker(getMarkerOptions(amapLocation));
					//获取定位信息
					StringBuffer buffer = new StringBuffer();
					buffer.append(amapLocation.getCountry() + "" + amapLocation.getProvince() + "" + amapLocation.getCity() + "" + amapLocation.getProvince() + "" + amapLocation.getDistrict() + "" + amapLocation.getStreet() + "" + amapLocation.getStreetNum());
					//Toast.makeText(getApplicationContext(), buffer.toString(), Toast.LENGTH_LONG).show();
					isFirstLoc = false;

					//sign();
				}


			} else {
				//显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
				Log.e("AmapError", "location Error, ErrCode:"
						+ amapLocation.getErrorCode() + ", errInfo:"
						+ amapLocation.getErrorInfo());
				tv_loc.setText("定位失败，请检查是否开启App定位权限");

				//Toast.makeText(getApplicationContext(), "定位失败", Toast.LENGTH_LONG).show();
			}
		}
	}

	//自定义一个图钉，并且设置图标，当我们点击图钉时，显示设置的信息
	/*private MarkerOptions getMarkerOptions(AMapLocation amapLocation) {
		//设置图钉选项
		MarkerOptions options = new MarkerOptions();
		//图标
		options.icon(BitmapDescriptorFactory.fromResource(R.drawable.loc));
		//位置
		options.position(new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude()));
		StringBuffer buffer = new StringBuffer();
		buffer.append(amapLocation.getCountry() + "" + amapLocation.getProvince() + "" + amapLocation.getCity() +  "" + amapLocation.getDistrict() + "" + amapLocation.getStreet() + "" + amapLocation.getStreetNum());
		//标题
		options.title(buffer.toString());
		//子标题
		options.snippet("这里好火");
		//设置多少帧刷新一次图片资源
		options.period(60);

		return options;

	}*/
	//激活定位
	@Override
	public void activate(OnLocationChangedListener listener) {
		mListener = listener;

	}

	//停止定位
	@Override
	public void deactivate() {
		mListener = null;
	}


	/**
	 * 方法必须重写
	 */
	@Override
	protected void onResume() {
		super.onResume();
		mapView.onResume();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onPause() {
		super.onPause();
		mapView.onPause();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
	}
    private void requestLocation() {
        if (PermissionsUtil.hasPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            //Toast.makeText(SignActivity.this, "已授权定位权限 开始定位", Toast.LENGTH_LONG).show();
            //sign();
            initLoc();
        } else {
            PermissionsUtil.requestPermission(this, new PermissionListener() {
                @Override
                public void permissionGranted(@NonNull String[] permissions) {
                    //Toast.makeText(SignActivity.this, "用户授权了定位权限", Toast.LENGTH_LONG).show();
                    //sign();
                    initLoc();
                }

                @Override
                public void permissionDenied(@NonNull String[] permissions) {
                    Toast.makeText(SignActivity.this, "用户拒绝了定位权限,无法获取到具体位置!", Toast.LENGTH_LONG).show();
                }
            }, Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }
	@Override
	public void onClick(View v) {
switch (v.getId())
{
	case R.id.btn_sign:
        sign();
		break;
}
	}

	//commit
				private void sign() {

                    if(lat==0.0||lon==0.0)
					{
						Toast.makeText(SignActivity.this, "请检查是否授权定位权限给应用", Toast.LENGTH_LONG).show();
						return;
					}else {

						AsyncHttpClient client = new AsyncHttpClient();
						RequestParams rp = new RequestParams();
						rp.put("workerId", MyApplication.workerId);
						rp.put("workerName", MyApplication.currentUserName);
						rp.put("latitude", lat);
						rp.put("longitude", lon);
						rp.put("location", tv_loc.getText().toString().replace("当前位置:", ""));


						client.post(Urls.sign, rp, new AsyncHttpResponseHandler() {
							@Override
							public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
								if (Utils.byteArrayToStr(responseBody) != null) {
									try {
										//JSONArray arr = new JSONArray(Utils.byteArrayToStr(responseBody));
										if (Utils.byteArrayToStr(responseBody).equals("ok")) {
											//Toast.makeText(SignActivity.this, "签到成功", Toast.LENGTH_LONG).show();
											Toasty.success(SignActivity.this, "签到成功!", Toast.LENGTH_SHORT, true).show();
										}


									} catch (Exception e) {
										Toast.makeText(SignActivity.this, "解析失败", Toast.LENGTH_SHORT).show();
									}
									new Handler().postDelayed(new Runnable(){
										public void run() {
											finish();
										}
									}, 2000);


								}
							}

							@Override
							public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
								Toast.makeText(SignActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
							}
						});
					}


				}
}


