package com.bitekun.helper;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bitekun.helper.R;
import com.bitekun.helper.util.DensityUtil;
import com.bitekun.helper.util.Urls;
import com.bitekun.helper.util.Utils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

public class LoginActivity extends Activity {

	EditText et_username, et_password;
	Button btn_login;
	 int errortimes = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		et_username = (EditText) findViewById(R.id.et_username);
		et_password = (EditText) findViewById(R.id.et_password);
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
		JSONArray arr = new JSONArray(Utils.byteArrayToStr(responseBody));
		if(arr!=null&&arr.length()>0)
		{
			JSONObject obj = arr.getJSONObject(0);
			if(obj.has("name")) {
				MyApplication.currentUserName = obj.getString("name");

			}
			else {
				JSONObject account = obj.getJSONObject("account");
				MyApplication.currentUserName = account.getString("String");
				JSONObject uid = obj.getJSONObject("u_id");
				MyApplication.workerId = uid.getString("String");
				//
                JSONObject rolename = obj.getJSONObject("password");
                MyApplication.rolename = rolename.getString("String");

			}

			//跳转

			startActivity(new Intent(LoginActivity.this,MainActivity.class));
            finish();

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
