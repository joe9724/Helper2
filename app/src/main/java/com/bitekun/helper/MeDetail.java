package com.bitekun.helper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bitekun.helper.bean.HelpPeopleListItem;
import com.bitekun.helper.util.Urls;
import com.bitekun.helper.util.Utils;
import com.bumptech.glide.Glide;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import es.dmoral.toasty.Toasty;


public class MeDetail extends Activity {

    HelpPeopleListItem hp;
    TextView tv_name,tv_sex,tv_age,tv_birth,tv_tel,tv_id,tv_no,tv_addr,tv_type,tv_class,tv_level,tv_guardname,tv_guardphone,tv_relation;
    ImageView iv_avatar;
    Button btn_picker;

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

        
        btn_picker = (Button)findViewById(R.id.btn_picker);
        btn_picker.setVisibility(View.VISIBLE);
        btn_picker.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //从服务器请求得到数据后推页到采集页面

                AsyncHttpClient client = new AsyncHttpClient();

                client.get(Urls.peopleDetail+hp.getIdCard(), new AsyncHttpResponseHandler() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        Toasty.success(MeDetail.this, "请求数据中，请稍候", Toast.LENGTH_SHORT, true).show();
                    }

                    @Override
                    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                        if (Utils.byteArrayToStr(responseBody) != null) {
                            try {
                                JSONObject obj = new JSONObject(Utils.byteArrayToStr(responseBody));
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
                                //item.setPickerId(obj.getString("pickerId"));

                                //
                                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                                try {
                                    Date date = format.parse(obj.getString("birth"));
                                    item.setAge(String.valueOf(Utils.getAge(date)));
                                    //System.out.println(date);
                                } catch (ParseException e) {
                                    //e.printStackTrace();
                                }

                                //
                                Intent intent = new Intent(MeDetail.this,HealthPicker.class);
                                intent.putExtra("item", (Serializable)item);
                                MeDetail.this.startActivity(intent);


                            } catch (Exception e) {
                                Toasty.error(MeDetail.this, "err"+e.getMessage(), Toast.LENGTH_SHORT, true).show();
                            }



                        }
                    }

                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                        Toasty.error(MeDetail.this, "failure"+error.getMessage(), Toast.LENGTH_SHORT, true).show();

                    }
                });


            }
        });
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

        Glide.with(MeDetail.this).load(Urls.prefix+"img/"+hp.getAvatar()).placeholder(R.drawable.defaultavatar).error(R.drawable.defaultavatar).centerCrop().crossFade().into(iv_avatar);





    }

	private void initData() {

	}



}
