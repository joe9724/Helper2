package com.bitekun.helper;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.GridView;
import android.widget.RelativeLayout;

import com.bitekun.helper.R;
import com.bitekun.helper.adapter.AdapterGV;
import com.bitekun.helper.bean.GridItem;

import scut.carson_ho.searchview.ICallBack;
import scut.carson_ho.searchview.SearchView;
import scut.carson_ho.searchview.bCallBack;


public class MainActivity extends Activity implements View.OnClickListener
{

 RelativeLayout layout_xcqd,layout_kfcj,layout_kfdx,layout_wfwd,layout_yjfk,layout_zx;
 Intent i;
	private SearchView searchView;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
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

		//
		// 3. 绑定组件
		searchView = (SearchView) findViewById(R.id.search_view);

		// 4. 设置点击键盘上的搜索按键后的操作（通过回调接口）
		// 参数 = 搜索框输入的内容
		searchView.setOnClickSearch(new ICallBack() {
			@Override
			public void SearchAciton(String string) {
				System.out.println("我收到了" + string);
			}
		});

		// 5. 设置点击返回按键后的操作（通过回调接口）
		searchView.setOnClickBack(new bCallBack() {
			@Override
			public void BackAciton() {
				finish();
			}
		});


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
				i = new Intent(this,HealthPicker.class);
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

				break;
		}
	}
}
