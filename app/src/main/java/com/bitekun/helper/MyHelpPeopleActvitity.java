package com.bitekun.helper;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bitekun.helper.bean.HelpPeopleListItem;
import com.bitekun.helper.util.CommonConst;
import com.bitekun.helper.util.JsonUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;

import java.util.ArrayList;
import java.util.List;


public class MyHelpPeopleActvitity extends Activity {

	private ArrayList<HelpPeopleListItem> datalist = new ArrayList<HelpPeopleListItem>();

	//private AdapterHelpPeople adapter;

	private int pageNo = 1;

	private RecyclerView mRv;
	private SmartRefreshLayout mRefresh;
	private LayoutAdapter adapter;
	private List lists;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help_people_activity);

		//先存入数据
		initData();

		mRv = (RecyclerView) findViewById(R.id.recyclerview);
		mRefresh = (SmartRefreshLayout) findViewById(R.id.refreshLayout);

		mRv.setLayoutManager(new LinearLayoutManager(this));
		adapter = new LayoutAdapter();
		mRv.setAdapter(adapter);

		//下拉刷新的监听
		mRefresh.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh(RefreshLayout refreshlayout) {
				lists.add(" start ");

				adapter.notifyDataSetChanged();
				mRefresh.finishRefresh(2000);
			}
		});

		//上拉加载更多的监听
		mRefresh.setOnLoadmoreListener(new OnRefreshLoadmoreListener() {
			@Override
			public void onLoadmore(RefreshLayout refreshlayout) {
				lists.add(" end ");

				adapter.notifyDataSetChanged();
				mRefresh.finishLoadmore(2000);
			}

			@Override
			public void onRefresh(RefreshLayout refreshlayout) {

			}
		});

	}

	private void initData() {
		lists = new ArrayList();
		for (int i = 0; i < 30; i++) {
			lists.add("Please go " + i);
		}
	}

	class LayoutAdapter extends RecyclerView.Adapter<LayoutAdapter.myViewholder> {

		@Override
		public LayoutAdapter.myViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
			myViewholder myViewholder = new myViewholder(LayoutInflater.from(MyHelpPeopleActvitity.this).inflate(R.layout.item_layout, parent, false));
			return myViewholder;
		}

		@Override
		public void onBindViewHolder(LayoutAdapter.myViewholder holder, final int position) {
			holder.mItemData.setText(lists.get(position) + "");
			holder.mItemData.setOnClickListener(new View.OnClickListener() {

				@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
				@Override
				public void onClick(View v) {

					MyHelpPeopleActvitity.this.startActivity(new Intent(MyHelpPeopleActvitity.this,HelpPeopleDetail.class));
				}
			});
		}

		@Override
		public int getItemCount() {
			return lists == null ? 0 : lists.size();
		}

		class myViewholder extends RecyclerView.ViewHolder {

			public TextView mItemData;
			public myViewholder(View itemView) {
				super(itemView);
				mItemData = (TextView) itemView.findViewById(R.id.textView7);
			}
		}
	}


	private void initData1(final String refreshMode) {
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
				//handler.obtainMessage(1).sendToTarget();

				// progress.setVisibility(View.GONE);
				pageNo = tempNo;
			}
		}.execute("");
	}



}
