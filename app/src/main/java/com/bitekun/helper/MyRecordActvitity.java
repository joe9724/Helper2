package com.bitekun.helper;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import es.dmoral.toasty.Toasty;


public class MyRecordActvitity extends Activity {

	//private ArrayList<HelpPeopleListItem> datalist = new ArrayList<HelpPeopleListItem>();

	//private AdapterHelpPeople adapter;



	private RecyclerView mRv;
	private SmartRefreshLayout mRefresh;
	private LayoutAdapter adapter;
	private ArrayList <MyServerItem> lists;
    private  int pageNo = 0;
    public static Handler handler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.myrecord_activity);
		ImageView iv_back;
		iv_back = (ImageView)findViewById(R.id.iv_back);
		iv_back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();

			}
		});
		//先存入数据
		lists = new ArrayList<MyServerItem>();



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
                pageNo = 0;
                lists = new ArrayList<MyServerItem>();
                initData(0);
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

		//
        initHandler();

	}

	private void initHandler()
    {
        handler = new Handler() {
            @Override
            public void handleMessage(final Message msg) {
                switch (msg.what) {
                    case -1:
                        pageNo = 0;
                        lists = new ArrayList<MyServerItem>();
                        initData(0);
                        mRefresh.finishRefresh(2000);
                        break;


                }
                super.handleMessage(msg);
            }
        };
    }

	private void initData(int pageIndex) {
		AsyncHttpClient client = new AsyncHttpClient();
		client.get(Urls.record+MyApplication.workerId+"&pageIndex="+pageIndex, new AsyncHttpResponseHandler() {
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
								MyServerItem item = new MyServerItem();
								item.setId(obj.getInt("id"));
								item.setDispeopleId(obj.getString("dispeopleId"));
								item.setIdcardNo(obj.getString("idcardNo"));
								item.setDiscardNo(obj.getString("discardNo"));
								item.setServiceId(obj.getString("serviceId"));
								item.setAgencyId(obj.getString("agencyId"));
								item.setAccessRecord(obj.getString("accessRecord"));
								//item.setLatitude(obj.getString("latitude"));
								item.setLocation(obj.getString("location"));
								item.setAccessTime(obj.getString("accessTime"));
								item.setLivePhoto(obj.getString("livePhoto"));
								item.setRecordPhoto(obj.getString("recordPhoto"));
								item.setGender(obj.getString("gender"));
								item.setPhone(obj.getString("phone"));
								item.setLevel(obj.getString("level"));
								item.setName(obj.getString("name"));
								item.setAvatar(obj.getString("avatar"));
								item.setDisableModeId(obj.getString("disableModeId"));
								item.setPickerName(obj.getString("pickerName"));
								item.setPickerId(obj.getString("pickerId"));
                                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                                try {
                                    Date date = format.parse(obj.getString("birth"));
                                    item.setAge(String.valueOf(getAge(date)));
                                    //System.out.println(date);
                                } catch (ParseException e) {
                                    //e.printStackTrace();
                                }


                                lists.add(item);
							}
							//
							adapter.notifyDataSetChanged();


						} catch (JSONException e) {
							Toast.makeText(MyRecordActvitity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
						}
					}catch (Exception e)
					{
                        lists = new ArrayList<MyServerItem>();
                        adapter.notifyDataSetChanged();
					}

				}
			}

			@Override
			public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                Toasty.success(MyRecordActvitity.this, "请求失败!", Toast.LENGTH_SHORT, true).show();
			}
		});

	}


	//数据适配器
	class LayoutAdapter extends RecyclerView.Adapter<LayoutAdapter.myViewholder> {
		myViewholder myViewholder;
		@Override
		public LayoutAdapter.myViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
			myViewholder = new myViewholder(LayoutInflater.from(MyRecordActvitity.this).inflate(R.layout.item_layout_myrecord, parent, false));
			return myViewholder;
		}

		@Override
		public void onBindViewHolder(LayoutAdapter.myViewholder holder, final int position) {
			holder.tv_name.setText(lists.get(position).getName());
			holder.tv_sex.setText(lists.get(position).getGender().equals("1")?"男":"女");
			holder.tv_age.setText(lists.get(position).getAge()+"岁");
			holder.tv_level.setText(lists.get(position).getLevel());
			holder.tv_tel.setText(lists.get(position).getPickerName());
			holder.tv_no.setText(lists.get(position).getDiscardNo());
			holder.tv_lastdate.setText(lists.get(position).getAccessTime());
			holder.tv_disableModelId.setText(lists.get(position).getDisableModeId());

			Glide.with(MyRecordActvitity.this).load(Urls.prefix+"img/"+lists.get(position).getAvatar()).placeholder(R.drawable.defaultavatar).error(R.drawable.defaultavatar).centerCrop().crossFade().into(holder.iv_avatar);

			holder.item.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(MyRecordActvitity.this,HealthPickerEdit.class);
					intent.putExtra("item", (Serializable)lists.get(position));
					MyRecordActvitity.this.startActivity(intent);
				}
			});
		}

		@Override
		public int getItemCount() {
			return lists == null ? 0 : lists.size();
		}

		class myViewholder extends RecyclerView.ViewHolder {

			public TextView tv_name,tv_sex,tv_age,tv_level,tv_tel,tv_no,tv_lastdate,tv_disableModelId;
			public LinearLayout item;
			public ImageView iv_avatar;
			public myViewholder(View itemView) {
				super(itemView);
				item = (LinearLayout)  itemView.findViewById(R.id.item);
				tv_name = (TextView) itemView.findViewById(R.id.tv_name);
				tv_sex = (TextView) itemView.findViewById(R.id.tv_sex);
				tv_age = (TextView) itemView.findViewById(R.id.tv_age);
				tv_level = (TextView) itemView.findViewById(R.id.tv_level);
				tv_tel = (TextView) itemView.findViewById(R.id.tv_tel);
				tv_no = (TextView)itemView.findViewById(R.id.tv_no);
				tv_lastdate = (TextView)itemView.findViewById(R.id.tv_lastdate);
				iv_avatar = (ImageView)itemView.findViewById(R.id.avatar);
				tv_disableModelId = (TextView)itemView.findViewById(R.id.tv_disableModelId);

			}
		}
	}

	ArrayList<HelpPeopleListItem> datalist;

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

	public static int getAge(Date birthDay) throws Exception
	{
		Calendar cal = Calendar.getInstance();

		if (cal.before(birthDay))
		{
			throw new IllegalArgumentException(
					"The birthDay is before Now.It's unbelievable!");
		}
		int yearNow = cal.get(Calendar.YEAR);
		int monthNow = cal.get(Calendar.MONTH);
		int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
		cal.setTime(birthDay);

		int yearBirth = cal.get(Calendar.YEAR);
		int monthBirth = cal.get(Calendar.MONTH);
		int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

		int age = yearNow - yearBirth;

		if (monthNow <= monthBirth)
		{
			if (monthNow == monthBirth)
			{
				if (dayOfMonthNow < dayOfMonthBirth)
					age--;
			}
			else
			{
				age--;
			}
		}
		return age;
	}

}
