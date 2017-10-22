package com.bitekun.helper;

import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bitekun.helper.bean.HelpPeopleListItem;
import com.bitekun.helper.util.DensityUtil;
import com.bitekun.helper.util.Urls;
import com.bitekun.helper.util.Utils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LoginActivity extends Activity {

	EditText et_username, et_password;
	Button btn_login;
	int errortimes = 0;
	private SharedPreferences pref;
    private SharedPreferences.Editor editor;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		et_username = (EditText) findViewById(R.id.et_oldpass);
		et_password = (EditText) findViewById(R.id.et_newpass);
        pref= PreferenceManager.getDefaultSharedPreferences(this);
		if(pref.getString("account","")!=null)
        {
            if(pref.getString("account","").length()>0)
            {
                et_username.setText(pref.getString("account",""));
            }
        }



		Drawable drawable1 = getResources().getDrawable(R.drawable.username);
		drawable1.setBounds(0, 0, DensityUtil.dip2px(this,22), DensityUtil.dip2px(this,22));//第一0是距左边距离，第二0是距上边距离，40分别是长宽
		et_username.setCompoundDrawables(drawable1, null, null, null);//只放左边
		Drawable drawable2 = getResources().getDrawable(R.drawable.password);
        drawable2.setBounds(0, 0, DensityUtil.dip2px(this,22), DensityUtil.dip2px(this,22));//第一0是距左边距离，第二0是距上边距离，40分别是长宽
		et_password.setCompoundDrawables(drawable2, null, null, null);//只放左边


		btn_login = (Button) findViewById(R.id.button5);

		btn_login.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if(errortimes==3)
				{
					Toast.makeText(LoginActivity.this,"账号或密码错误3次，系统将自动退出!",Toast.LENGTH_SHORT).show();
					finish();
					System.exit(0);
				}
				AsyncHttpClient client = new AsyncHttpClient();
				client.get(Urls.login+"username="+et_username.getText().toString().trim()+"&password="+et_password.getText().toString().trim(), new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
if(Utils.byteArrayToStr(responseBody)!=null)
{
	if (Utils.byteArrayToStr(responseBody).equals("error"))
	{
		errortimes++;
		Toast.makeText(LoginActivity.this,"账号或密码错误,累计错误"+errortimes+"次",Toast.LENGTH_SHORT).show();
		return;
	}
	try {
		//JSONArray arr = new JSONArray();
		Intent i;
		//if(arr!=null&&arr.length()>0)
		//{
			JSONObject obj = new JSONObject(Utils.byteArrayToStr(responseBody));
			if(obj.has("address")) {
				//病人或家属登录
				MyApplication.currentUserName = obj.getString("name");
				MyApplication.isadmin = false;
				HelpPeopleListItem item = new HelpPeopleListItem();
				item.setId(obj.getString("id"));
				item.setIdCard(obj.getString("idCard"));
				item.setCardNo(obj.getString("cardNo"));
                MyApplication.workerId = obj.getString("idCard");
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

				//
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				try {
					Date date = format.parse(obj.getString("birth"));
					item.setAge(String.valueOf(Utils.getAge(date)));
					//System.out.println(date);
				} catch (ParseException e) {
					//e.printStackTrace();
				}
				i = new Intent(LoginActivity.this,CommonMainActivity.class);
				i.putExtra("item", (Serializable)item);
				//
                editor=pref.edit();
                editor.putString("account",et_username.getText().toString().trim());
                editor.apply();

				startActivity(i);
				finish();

			}
			else if(obj.has("account") ){
				//管理员登录
				MyApplication.isadmin = true;

				MyApplication.currentUserName = obj.getString("account");

				MyApplication.area = obj.getString("area");

				MyApplication.workerId = obj.getString("u_id");

				MyApplication.qx = obj.getString("qx");
				//
                editor=pref.edit();
                editor.putString("account",et_username.getText().toString().trim());
                editor.apply();

                MyApplication.rolename = obj.getString("password");
				i = new Intent(LoginActivity.this,MainActivity.class);
				startActivity(i);
				finish();
			}
			else
            {
                errortimes++;
                Toast.makeText(LoginActivity.this,"账号或密码错误,累计错误"+errortimes+"次",Toast.LENGTH_SHORT).show();
                return;
            }



	}catch (Exception e)
	{
		Toast.makeText(LoginActivity.this,"Json解析失败",Toast.LENGTH_SHORT).show();
	}

}
					}

					@Override
					public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
						Toast.makeText(LoginActivity.this,"请求失败",Toast.LENGTH_SHORT).show();
					}
				});

			}
		});

	}

	//


}
