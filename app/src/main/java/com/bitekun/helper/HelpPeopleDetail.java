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
import android.widget.TextView;

import com.bitekun.helper.bean.HelpPeopleListItem;
import com.bitekun.helper.util.CommonConst;
import com.bitekun.helper.util.JsonUtil;
import com.bitekun.helper.util.Urls;
import com.bumptech.glide.Glide;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;


public class HelpPeopleDetail extends Activity {

    HelpPeopleListItem hp;
    TextView tv_name,tv_sex,tv_age,tv_birth,tv_tel,tv_id,tv_no,tv_addr,tv_type,tv_class,tv_level,tv_guardname,tv_guardphone,tv_relation;
    ImageView iv_avatar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help_people_detail);
		ImageView iv_back;
		iv_back = (ImageView)findViewById(R.id.iv_back);
		iv_back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();

			}
		});

		//
        hp = (HelpPeopleListItem) getIntent().getSerializableExtra("item");
        tv_name = (TextView)findViewById(R.id.tv_name);
        tv_sex = (TextView)findViewById(R.id.tv_sex);
        tv_age = (TextView)findViewById(R.id.tv_age);
        tv_birth = (TextView)findViewById(R.id.tv_birth);
        tv_tel = (TextView)findViewById(R.id.tv_tel);
        tv_id = (TextView)findViewById(R.id.tv_id);
        tv_no = (TextView)findViewById(R.id.tv_no);
        tv_addr = (TextView)findViewById(R.id.tv_addr);
        tv_type = (TextView)findViewById(R.id.tv_type);
        tv_class = (TextView)findViewById(R.id.tv_class);
        tv_level = (TextView)findViewById(R.id.tv_level);
        tv_guardname = (TextView)findViewById(R.id.tv_guardname);
        tv_guardphone = (TextView)findViewById(R.id.tv_guardphone);
        tv_relation = (TextView)findViewById(R.id.tv_relation);

        tv_name.setText(hp.getName());
        tv_sex.setText(hp.getGender().equals("1")?"男":"女");
        try{
            tv_age.setText(hp.getAge()+"岁");
        }catch (Exception e)
        {

        }
        tv_birth.setText(hp.getBirth());
        tv_tel.setText(hp.getPhone());
        tv_id.setText(hp.getIdCard());
        tv_no.setText(hp.getCardNo());
        tv_addr.setText(hp.getAddress());
        tv_type.setText(hp.getDisableType());
        tv_class.setText(hp.getDisableModeId());
        tv_level.setText(hp.getDisableLevel());
        tv_guardname.setText(hp.getGuardian());
        tv_guardphone.setText(hp.getGuardPhone());
        tv_relation.setText(hp.getRelation());

        iv_avatar = (ImageView)findViewById(R.id.iv_avatar);

        Glide.with(HelpPeopleDetail.this).load(Urls.prefix+"img/"+hp.getAvatar()).placeholder(R.drawable.defaultavatar).error(R.drawable.defaultavatar).centerCrop().crossFade().into(iv_avatar);





    }

	private void initData() {

	}



}
