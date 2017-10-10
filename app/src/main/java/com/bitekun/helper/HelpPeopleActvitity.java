package com.bitekun.helper;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bitekun.helper.R;
import com.bitekun.helper.adapter.AdapterHelpPeople;
import com.bitekun.helper.bean.HelpPeopleListItem;
import com.bitekun.helper.util.CommonConst;
import com.bitekun.helper.util.DensityUtil;
import com.bitekun.helper.util.JsonUtil;
import com.bitekun.helper.util.NetGet;
import com.bitekun.helper.util.Urls;
import com.bitekun.helper.util.Utils;
import com.bumptech.glide.Glide;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.footer.FalsifyFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

/*
import scut.carson_ho.searchview.ICallBack;
import scut.carson_ho.searchview.SearchView;
import scut.carson_ho.searchview.bCallBack;
*/


public class HelpPeopleActvitity extends AppCompatActivity {

	//private ArrayList<HelpPeopleListItem> datalist = new ArrayList<HelpPeopleListItem>();

	//private AdapterHelpPeople adapter;


	//private SearchView searchView;
	private RecyclerView mRv;
	private SmartRefreshLayout mRefresh;
	private LayoutAdapter adapter;
	private ArrayList <HelpPeopleListItem> lists;
	private  int pageNo = 0;
	private  String searchstr="ppppp";
	EditText et_search;
	TextView tv_cancel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.help_people_activity);
		ImageView iv_back;
		iv_back = (ImageView)findViewById(R.id.iv_back);
		iv_back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();

			}
		});
		//先存入数据
		lists = new ArrayList<HelpPeopleListItem>();
		//search
		// 3. 绑定组件
		//searchView = (SearchView) findViewById(R.id.search_view);

		// 4. 设置点击搜索按键后的操作（通过回调接口）
		// 参数 = 搜索框输入的内容
		/*searchView.setOnClickSearch(new ICallBack() {
			@Override
			public void SearchAciton(String string) {
				System.out.println("我收到了" + string);
				searchstr = string;
				Toast.makeText(HelpPeopleActvitity.this,"开始搜索",Toast.LENGTH_SHORT).show();
				lists.clear();
				initData(0);
			}
		});*/

		// 5. 设置点击返回按键后的操作（通过回调接口）
		/*searchView.setOnClickBack(new bCallBack() {
			@Override
			public void BackAciton() {
				finish();
			}
		});*/

		mRv = (RecyclerView) findViewById(R.id.recyclerview);
		mRefresh = (SmartRefreshLayout) findViewById(R.id.refreshLayout);
		//设置 Header 为 Material风格
		mRefresh.setRefreshHeader(new ClassicsHeader(this));
        //设置 Footer 为 球脉冲
		mRefresh.setRefreshFooter(new ClassicsFooter(this));

		mRv.setLayoutManager(new LinearLayoutManager(this));
		adapter = new LayoutAdapter();
		mRv.setAdapter(adapter);
        searchstr="ppppp";
		initData(0);
		Log.v("get","4");

		//下拉刷新的监听
		mRefresh.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh(RefreshLayout refreshlayout) {
				pageNo = 0;
				lists = new ArrayList<HelpPeopleListItem>();
				initData(0);
				Log.v("get","5");
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
				Log.v("get","6");
				mRefresh.finishLoadmore(2000);
			}

			@Override
			public void onRefresh(RefreshLayout refreshlayout) {

			}
		});

		//
        tv_cancel = (TextView)findViewById(R.id.tv_cancel);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchstr="ppppp";
                tv_cancel.setVisibility(View.INVISIBLE);
				lists = new ArrayList<HelpPeopleListItem>();
                initData(0);
            }
        });
        et_search = (EditText)findViewById(R.id.search_view);
        Drawable drawable1 = getResources().getDrawable(R.drawable.searchicon);
        drawable1.setBounds(0, 0, DensityUtil.dip2px(HelpPeopleActvitity.this,15), DensityUtil.dip2px(HelpPeopleActvitity.this,15));//第一0是距左边距离，第二0是距上边距离，40分别是长宽
        et_search.setCompoundDrawables(drawable1, null, null, null);//只放左边

		et_search.setOnKeyListener(new View.OnKeyListener() {

			@Override

			public boolean onKey(View v, int keyCode, KeyEvent event) {

				if (keyCode == KeyEvent.KEYCODE_ENTER&&event.getAction()==KeyEvent.ACTION_UP) {
					// 先隐藏键盘
					((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
							.hideSoftInputFromWindow(HelpPeopleActvitity.this.getCurrentFocus()
									.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
					//进行搜索操作的方法，在该方法中可以加入mEditSearchUser的非空判断
					//search();
					if(et_search.getText()!=null&&et_search.getText().toString().length()>0)
					{
					    tv_cancel.setVisibility(View.VISIBLE);
						searchstr = et_search.getText().toString().trim();
						lists = new ArrayList<HelpPeopleListItem>();
						Log.v("get","1");
						initData(0);
						Log.v("get","2");
					}
				}
				return false;
			}
		});

	}

	private void initData(int pageIndex) {
		AsyncHttpClient client = new AsyncHttpClient();
		//lists.clear();
        String query = Urls.people+"pageIndex="+pageIndex+"&keyword="+searchstr;
        Log.v("get",query);
		client.get(query, new AsyncHttpResponseHandler() {
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
								HelpPeopleListItem item = new HelpPeopleListItem();
								item.setId(obj.getString("id"));
								item.setIdCard(obj.getString("idCard"));
								item.setCardNo(obj.getString("cardNo"));
								item.setCardStateId(obj.getString("cardstateid"));
								item.setDisableType(obj.getString("disableType"));
								item.setDisableModeId(obj.getString("disableModeId"));
								item.setDisableLevel(obj.getString("disableLevel"));
								item.setIsChild(obj.getString("isChild"));
								item.setName(obj.getString("name"));
								item.setGender(obj.getString("gender"));
								item.setBirth(obj.getString("birth"));
								item.setNation(obj.getString("nation"));
								item.setPhone(obj.getString("phone"));
								item.setAddress(obj.getString("address"));
								item.setGuardian(obj.getString("guardian"));
								item.setGuardPhone(obj.getString("guardPhone"));
								item.setRelation(obj.getString("relation"));
								item.setCoordinator(obj.getString("coordinator"));
								item.setPickTime(obj.getString("pickTime"));
								item.setAvatar(obj.getString("avatar"));
								item.setSzxq(obj.getString("szxq"));
								item.setSzxz(obj.getString("szxz"));
								item.setSzc(obj.getString("szc"));
								item.setKfxm(obj.getString("kfxm"));
								item.setKfjg(obj.getString("kfjg"));
								item.setPickerId(obj.getString("pickerId"));


								//
								SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
								try {
									Date date = format.parse(obj.getString("birth"));
									item.setAge(String.valueOf(Utils.getAge(date)));
									//System.out.println(date);
								} catch (ParseException e) {
									//e.printStackTrace();
								}


								lists.add(item);
							}
							//
							adapter.notifyDataSetChanged();


						} catch (JSONException e) {
							//Toast.makeText(HelpPeopleActvitity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
						}
					}catch (Exception e)
					{
						//Toast.makeText(HelpPeopleActvitity.this,"Json解析失败",Toast.LENGTH_SHORT).show();
					}

				}
			}

			@Override
			public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
				Toast.makeText(HelpPeopleActvitity.this,"请求失败",Toast.LENGTH_SHORT).show();
			}
		});

	}


	//数据适配器
	class LayoutAdapter extends RecyclerView.Adapter<LayoutAdapter.myViewholder> {
		myViewholder myViewholder;
		@Override
		public LayoutAdapter.myViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
			 myViewholder = new myViewholder(LayoutInflater.from(HelpPeopleActvitity.this).inflate(R.layout.item_layout, parent, false));
			return myViewholder;
		}

		@Override
		public void onBindViewHolder(LayoutAdapter.myViewholder holder, final int position) {
			holder.tv_name.setText(lists.get(position).getName());
			holder.tv_sex.setText(lists.get(position).getGender().equals("1")?"男":"女");
			holder.tv_age.setText(lists.get(position).getAge()+"岁");
			holder.tv_level.setText(lists.get(position).getDisableLevel());
			holder.tv_tel.setText(lists.get(position).getPhone());
			holder.tv_disableModel.setText("残疾类型:"+lists.get(position).getDisableModeId());
			holder.tv_picktime.setText(lists.get(position).getPickTime().replace(".000000",""));
			Glide.with(HelpPeopleActvitity.this).load(Urls.prefix+"img/"+lists.get(position).getAvatar()).placeholder(R.drawable.defaultavatar).error(R.drawable.defaultavatar).centerCrop().crossFade().into(holder.iv_avatar);
			holder.btn_pick.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(HelpPeopleActvitity.this,HealthPicker.class);
					intent.putExtra("item", (Serializable)lists.get(position));
					HelpPeopleActvitity.this.startActivity(intent);
				}
			});

			holder.item.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(HelpPeopleActvitity.this,HelpPeopleDetail.class);
					intent.putExtra("item", (Serializable)lists.get(position));
					HelpPeopleActvitity.this.startActivity(intent);
				}
			});
		}

		@Override
		public int getItemCount() {
			return lists == null ? 0 : lists.size();
		}

		class myViewholder extends RecyclerView.ViewHolder {

			public TextView tv_name,tv_sex,tv_age,tv_level,tv_tel,tv_picktime,tv_disableModel;
			public Button btn_pick;
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
				btn_pick = (Button)itemView.findViewById(R.id.btn_pick);
				iv_avatar = (ImageView)itemView.findViewById(R.id.avatar);
				tv_picktime = (TextView)itemView.findViewById(R.id.tv_picktime);
				tv_disableModel = (TextView)itemView.findViewById(R.id.tv_disableModel);
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



}
