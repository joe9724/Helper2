package com.bitekun.helper;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bitekun.helper.bean.HelpPeopleListItem;
import com.bitekun.helper.bean.MyServerItem;
import com.bitekun.helper.bean.Sign;
import com.bitekun.helper.util.CommonConst;
import com.bitekun.helper.util.JsonUtil;
import com.bitekun.helper.util.Urls;
import com.bitekun.helper.util.Utils;
import com.bumptech.glide.Glide;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class SingListActvitity extends Activity {

	//private ArrayList<HelpPeopleListItem> datalist = new ArrayList<HelpPeopleListItem>();

	//private AdapterHelpPeople adapter;

	private RecyclerView mRv;
	private SmartRefreshLayout mRefresh;
	private LayoutAdapter adapter;
	private ArrayList <Sign> lists;
	private static int pageNo = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.signlist_activity);
		ImageView iv_back;
		iv_back = (ImageView)findViewById(R.id.iv_back);
		iv_back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();

			}
		});
		//先存入数据
		lists = new ArrayList<Sign>();



		mRv = (RecyclerView) findViewById(R.id.recyclerview);
		mRefresh = (SmartRefreshLayout) findViewById(R.id.refreshLayout);
//设置 Header 为 Material风格
        mRefresh.setRefreshHeader(new ClassicsHeader(this));
        //设置 Footer 为 球脉冲
        mRefresh.setRefreshFooter(new ClassicsFooter(this));
		mRv.setLayoutManager(new LinearLayoutManager(this));
		adapter = new LayoutAdapter();
		mRv.setAdapter(adapter);

		initData(0);

		//下拉刷新的监听
		mRefresh.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh(RefreshLayout refreshlayout) {
				//lists.add(" start ");
                pageNo = 0;
                lists.clear();
                initData(0);
				//adapter.notifyDataSetChanged();
				mRefresh.finishRefresh(2000);
			}
		});

		//上拉加载更多的监听
		mRefresh.setOnLoadmoreListener(new OnRefreshLoadmoreListener() {
			@Override
			public void onLoadmore(RefreshLayout refreshlayout) {
				//lists.add(" end ");
                pageNo++;
                initData(pageNo*20);
				//adapter.notifyDataSetChanged();
				mRefresh.finishLoadmore(2000);
			}

			@Override
			public void onRefresh(RefreshLayout refreshlayout) {

			}
		});

	}

	private void initData(int pageIndex) {
		AsyncHttpClient client = new AsyncHttpClient();
		client.get(Urls.signlist+MyApplication.workerId+"&pageIndex="+pageIndex, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
				if(Utils.byteArrayToStr(responseBody)!=null)
				{
					try {
						JSONArray arr = new JSONArray(Utils.byteArrayToStr(responseBody));
						try {
							for (int i=0;i<arr.length();i++)
							{
								JSONObject obj = arr.getJSONObject(i);
								Sign item = new Sign();
								item.setId(obj.getString("id"));
								item.setWorkerId(obj.getString("workerId"));
								item.setWorkerName(obj.getString("workerName"));
								item.setLatitude(obj.getString("latitude"));
								item.setLongitude(obj.getString("longitude"));
								item.setWorkTime(obj.getString("workTime"));
								item.setLocation(obj.getString("location"));



                                lists.add(item);
							}
							//
							adapter.notifyDataSetChanged();


						} catch (JSONException e) {
							Toast.makeText(SingListActvitity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
						}
					}catch (Exception e)
					{
						//Toast.makeText(SingListActvitity.this,"Json解析失败",Toast.LENGTH_SHORT).show();
					}

				}
			}

			@Override
			public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
				Toast.makeText(SingListActvitity.this,"请求失败",Toast.LENGTH_SHORT).show();
			}
		});

	}


	//数据适配器
	class LayoutAdapter extends RecyclerView.Adapter<LayoutAdapter.myViewholder> {
		myViewholder myViewholder;
		@Override
		public LayoutAdapter.myViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
			myViewholder = new myViewholder(LayoutInflater.from(SingListActvitity.this).inflate(R.layout.signitem, parent, false));
			return myViewholder;
		}

		@Override
		public void onBindViewHolder(LayoutAdapter.myViewholder holder, final int position) {
			holder.tv_man.setText(lists.get(position).getWorkerName());
			holder.tv_addr.setText(lists.get(position).getLocation());
			holder.tv_date.setText(lists.get(position).getWorkTime());

		}

		@Override
		public int getItemCount() {
			return lists == null ? 0 : lists.size();
		}

		class myViewholder extends RecyclerView.ViewHolder {

			public TextView tv_man,tv_addr,tv_date;

			public myViewholder(View itemView) {
				super(itemView);

				tv_man = (TextView) itemView.findViewById(R.id.tv_man);
				tv_addr = (TextView) itemView.findViewById(R.id.tv_addr);
				tv_date = (TextView) itemView.findViewById(R.id.tv_date);


			}
		}
	}





}
