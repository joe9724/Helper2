package com.bitekun.helper;

import java.util.ArrayList;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.view.View;
import com.bitekun.helper.R;
import com.bitekun.helper.adapter.AdapterHelpPeople;
import com.bitekun.helper.bean.HelpPeopleListItem;
import com.bitekun.helper.util.CommonConst;
import com.bitekun.helper.util.JsonUtil;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;


public class HelpPeopleActvitity extends Activity {

	private ArrayList<HelpPeopleListItem> datalist = new ArrayList<HelpPeopleListItem>();

	private AdapterHelpPeople adapter;

	private int pageNo = 1;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help_people_activity);
		//
		RefreshLayout refreshLayout = (RefreshLayout)findViewById(R.id.refreshLayout);
		refreshLayout.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh(RefreshLayout refreshlayout) {
				refreshlayout.finishRefresh(2000);
			}
		});
		refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
			@Override
			public void onLoadmore(RefreshLayout refreshlayout) {
				refreshlayout.finishLoadmore(2000);
			}
		});

	}

	private void initData(final String refreshMode) {
		new AsyncTask<String, Integer, String>() {
			int tempNo = pageNo;

			@Override
			protected String doInBackground(String... params) {
				if (CommonConst.REFRESH_MODE_1.equals(refreshMode)) {
					tempNo = 1;
					// MyApplication.FAT_DATAMAP.clear();
					datalist = JsonUtil.getInstance().getHelpPeople("",
							tempNo + "", MyApplication.FAT_DATAMAP);

				} else if (CommonConst.REFRESH_MODE_2.equals(refreshMode)) {
					tempNo++;
					if (null == datalist) {
						datalist = new ArrayList<HelpPeopleListItem>();
					}
					datalist.addAll(JsonUtil.getInstance().getHelpPeople("",
							tempNo + "", MyApplication.FAT_DATAMAP));
				}

				return null;
			}

			@Override
			protected void onPostExecute(String result) {
				super.onPostExecute(result);

				// news_fat_listview.setVisibility(View.GONE);
				// newsListviewAdapter.notifyDataSetChanged();
				// news_fat_listview.setVisibility(View.VISIBLE);
				handler.obtainMessage(1).sendToTarget();

				// progress.setVisibility(View.GONE);
				pageNo = tempNo;
			}
		}.execute("");
	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case 1:
				if (adapter != null) {
					// newsListviewAdapter.clearData();
					if (datalist != null) {
						adapter.setDataList(datalist);
						//listview.setAdapter(adapter);
						// adapter.notifyDataSetChanged();
						// listview.onRefreshComplete();
					}
				}
				break;

			}
			super.handleMessage(msg);
		}
	};

}
