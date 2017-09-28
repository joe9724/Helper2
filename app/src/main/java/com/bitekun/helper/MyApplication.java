package com.bitekun.helper;

import java.util.HashMap;
import java.util.Map;



import android.app.Application;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.view.Display;
import android.view.WindowManager;

import com.bitekun.helper.bean.JsonContent;

public class MyApplication extends Application {
	public static int phonewidth, phoneheight;
	public static final Map<String, JsonContent> FAT_DATAMAP = new HashMap<String, JsonContent>();

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate() {
		super.onCreate();

		Resources resources = getResources();// 获得res资源对象
		Configuration config = resources.getConfiguration();// 获得设置对象
		WindowManager manage = (WindowManager) getSystemService(WINDOW_SERVICE);
		Display display = manage.getDefaultDisplay();
		phonewidth = display.getWidth();
		phoneheight = display.getHeight();
	}

}