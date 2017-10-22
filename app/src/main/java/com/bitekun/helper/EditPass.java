package com.bitekun.helper;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

import es.dmoral.toasty.Toasty;

public class EditPass extends Activity {

	EditText et_oldpass, et_newpass;
	Button btn_edit;

	ImageView iv_back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_editpass);
		iv_back = (ImageView)findViewById(R.id.iv_back);
		iv_back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();

			}
		});


        et_oldpass = (EditText) findViewById(R.id.et_oldpass);
        et_newpass = (EditText) findViewById(R.id.et_newpass);


        btn_edit = (Button) findViewById(R.id.btn_edit);

        btn_edit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				AsyncHttpClient client = new AsyncHttpClient();
				String query = Urls.edit+"uid="+MyApplication.workerId+"&newpass="+et_newpass.getText().toString().trim()+"&oldpass="+et_oldpass.getText().toString().trim();
				client.get(query, new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
if(Utils.byteArrayToStr(responseBody)!=null) {
    if (Utils.byteArrayToStr(responseBody).equals("ok"))
    {
        Toasty.success(EditPass.this, "修改成功,下次可使用新密码重新登录!", Toast.LENGTH_SHORT, true).show();
        finish();

    }else if((Utils.byteArrayToStr(responseBody).equals("error")))
    {
        Toasty.error(EditPass.this, "修改失败!", Toast.LENGTH_SHORT, true).show();
    }
}

}


					@Override
					public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
						Toast.makeText(EditPass.this,"请求失败",Toast.LENGTH_SHORT).show();
					}
				});

			}
		});

	}

	//


}
