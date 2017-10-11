package com.bitekun.helper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.bitekun.helper.R;
import com.bitekun.helper.adapter.AdapterGV;
import com.bitekun.helper.bean.GridItem;
import com.bitekun.helper.util.DensityUtil;
import com.github.dfqin.grantor.PermissionListener;
import com.github.dfqin.grantor.PermissionsUtil;
import com.umeng.analytics.MobclickAgent;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, LocationSource,AMapLocationListener
{
 RelativeLayout layout_xcqd,layout_kfcj,layout_kfdx,layout_wfwd,layout_yjfk,layout_zx;
 Intent i;

	//定位需要的声明
	//显示地图需要的变量
	private MapView mapView;//地图控件
	private AMap aMap;//地图对象
	private AMapLocationClient mLocationClient = null;//定位发起端
	private AMapLocationClientOption mLocationOption = null;//定位参数
	private OnLocationChangedListener mListener = null;//定位监听器

	//标识，用于判断是否只显示一次定位信息和用户重新定位
	private boolean isFirstLoc = true;

	TextView tv_loc;




	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		layout_xcqd = (RelativeLayout)findViewById(R.id.layout_xcqd);
				layout_kfcj = (RelativeLayout)findViewById(R.id.layout_kfcj);
		layout_kfdx = (RelativeLayout)findViewById(R.id.layout_kfdx);
				layout_wfwd = (RelativeLayout)findViewById(R.id.layout_wfwd);
		layout_yjfk = (RelativeLayout)findViewById(R.id.layout_yjfk);
				layout_zx = (RelativeLayout)findViewById(R.id.layout_zx);

		layout_xcqd.setOnClickListener(this);
		layout_kfcj.setOnClickListener(this);
		layout_kfdx.setOnClickListener(this);
		layout_wfwd.setOnClickListener(this);
		layout_yjfk.setOnClickListener(this);
		layout_zx.setOnClickListener(this);
		tv_loc = (TextView)findViewById(R.id.textView14);

		//
		int width = MyApplication.phonewidth- DensityUtil.dip2px(MainActivity.this, (float) 30.0);
		int half_width = width /2;
		int big_width = MyApplication.phonewidth- DensityUtil.dip2px(MainActivity.this, (float) 20.0);
		int height = ((MyApplication.phoneheight- DensityUtil.dip2px(MainActivity.this, (float) 55.0))-DensityUtil.dip2px(MainActivity.this, (float) 50.0)-statusbar_Height())/4;
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(big_width,height);
		LinearLayout.LayoutParams half_lp = new LinearLayout.LayoutParams(half_width,height);
		int densityvalue = DensityUtil.dip2px(MainActivity.this,10);
		half_lp.setMargins(densityvalue,densityvalue,0,0);
		lp.setMargins(densityvalue,densityvalue,0,0);
		layout_xcqd.setLayoutParams(half_lp);
		layout_kfcj.setLayoutParams(half_lp);
		layout_kfdx.setLayoutParams(lp);
		layout_wfwd.setLayoutParams(half_lp);
		layout_yjfk.setLayoutParams(half_lp);
        layout_zx.setLayoutParams(lp);




//显示地图
		mapView = (MapView) findViewById(R.id.map);
		//必须要写
		mapView.onCreate(savedInstanceState);
		//获取地图对象
		aMap = mapView.getMap();
		//
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
		myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher));
		myLocationStyle.radiusFillColor(android.R.color.transparent);
		myLocationStyle.strokeColor(android.R.color.transparent);
		aMap.setMyLocationStyle(myLocationStyle);
		//tv_loc.setText("定位中..."+" "+MyApplication.currentUserName+" "+MyApplication.rolename);
		//开始定位
		//initLoc();
		requestLocation();


	}


	@Override
	public void onClick(View view) {
		switch (view.getId())
		{
			case R.id.layout_xcqd:
                i = new Intent(this,SignActivity.class);
				startActivity(i);
				break;

			case R.id.layout_kfcj:
				i = new Intent(this,SingListActvitity.class);
				startActivity(i);
				break;

			case R.id.layout_kfdx:
				i = new Intent(this,HelpPeopleActvitity.class);
				startActivity(i);
				break;

			case R.id.layout_wfwd:
				i = new Intent(this,MyHelpPeopleActvitity.class);
				startActivity(i);
				break;

			case R.id.layout_yjfk:
				i = new Intent(this,FeedbackActivity.class);
				startActivity(i);
				break;

			case R.id.layout_zx:
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
                builder.setTitle("注销登录");
                builder.setMessage("确定要注销登录吗?");
                builder.setNegativeButton("取消", null);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //finish();
                        //i = new Intent(MainActivity.this,LoginActivity.class);
                        //startActivity(i);
                        //finish();// 或原finish()方法：finish();
						try {
						    if(mapView!=null)
							mapView.onDestroy();
						}catch (Exception e)
						{

						}
						finish();
                        //System.exit(0);
                    }
                });
                builder.show();
				break;
		}
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
		builder.setTitle("退出应用");
		builder.setMessage("确定要退出吗?");
		builder.setNegativeButton("取消", null);
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				//finish();// 或原finish()方法：finish();
				try {
				    if(mapView!=null)
					mapView.onDestroy();
				}catch (Exception e)
				{

				}
				finish();
				//System.exit(0);
			}
		});
		builder.show();

		return super.onKeyDown(keyCode, event);
	}

	//
	public int statusbar_Height() {
		int result = 0;
		int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
		if (resourceId > 0) {
			result = getResources().getDimensionPixelSize(resourceId);
		}
		return result;
	}

	//
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
				amapLocation.getLongitude();//获取经度
				MyApplication.lat = amapLocation.getLatitude();
				MyApplication.lon = amapLocation.getLongitude();

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

				MyApplication.location = amapLocation.getCity()+amapLocation.getDistrict()+amapLocation.getStreet();

				tv_loc.setText(amapLocation.getCity()+amapLocation.getDistrict()+amapLocation.getStreet()+"   "+MyApplication.currentUserName+"   "+MyApplication.rolename);

				// 如果不设置标志位，此时再拖动地图时，它会不断将地图移动到当前的位置
				if (isFirstLoc) {
					//设置缩放级别
					//aMap.moveCamera(CameraUpdateFactory.zoomTo(17));
					//将地图移动到定位点
					//aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude())));
					//点击定位按钮 能够将地图的中心移动到定位点
					//mListener.onLocationChanged(amapLocation);
					//添加图钉
					//aMap.addMarker(getMarkerOptions(amapLocation));
					//获取定位信息
					StringBuffer buffer = new StringBuffer();
					buffer.append(amapLocation.getCountry() + "" + amapLocation.getProvince() + "" + amapLocation.getCity() + "" + amapLocation.getProvince() + "" + amapLocation.getDistrict() + "" + amapLocation.getStreet() + "" + amapLocation.getStreetNum());
					//Toast.makeText(getApplicationContext(), buffer.toString(), Toast.LENGTH_LONG).show();
					isFirstLoc = false;
				}


			} else {
				//显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
				Log.e("AmapError", "location Error, ErrCode:"
						+ amapLocation.getErrorCode() + ", errInfo:"
						+ amapLocation.getErrorInfo());

				Toast.makeText(getApplicationContext(), "定位失败", Toast.LENGTH_LONG).show();
			}
		}
	}

	//自定义一个图钉，并且设置图标，当我们点击图钉时，显示设置的信息
	/*private MarkerOptions getMarkerOptions(AMapLocation amapLocation) {
		//设置图钉选项
		MarkerOptions options = new MarkerOptions();
		//图标
		options.icon(BitmapDescriptorFactory.fromResource(R.mipmap.logo));
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
		MobclickAgent.onResume(this);
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onPause() {
		super.onPause();
		mapView.onPause();
		MobclickAgent.onPause(this);
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
			//Toast.makeText(MainActivity.this, "已授权定位权限 开始定位", Toast.LENGTH_LONG).show();
			//sign();
			initLoc();
		} else {
			PermissionsUtil.requestPermission(this, new PermissionListener() {
				@Override
				public void permissionGranted(@NonNull String[] permissions) {
					//Toast.makeText(MainActivity.this, "用户授权了定位权限", Toast.LENGTH_LONG).show();
					//sign();
					initLoc();
				}

				@Override
				public void permissionDenied(@NonNull String[] permissions) {
					Toast.makeText(MainActivity.this, "用户拒绝了定位权限,无法获取到具体位置!", Toast.LENGTH_LONG).show();
				}
			}, Manifest.permission.ACCESS_FINE_LOCATION);
		}
	}

}
