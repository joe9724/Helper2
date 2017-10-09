package com.bitekun.helper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.MapView;
import com.bitekun.helper.util.Urls;
import com.bitekun.helper.util.Utils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import es.dmoral.toasty.Toasty;

public class FeedbackActivity extends Activity {

	EditText et_title, et_content;
	Button btn_submit;
	ImageView iv_back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feedback);

		et_title = (EditText) findViewById(R.id.et_title);
		et_content = (EditText) findViewById(R.id.et_content);
		iv_back = (ImageView)findViewById(R.id.iv_back);
		iv_back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
        finish();

			}
		});

		btn_submit = (Button) findViewById(R.id.btn_submit);

		btn_submit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {


				AsyncHttpClient client = new AsyncHttpClient();
				RequestParams rp = new RequestParams();
				rp.put("title",et_title.getText().toString().trim());
				rp.put("content",et_content.getText().toString().trim());
				rp.put("idCardNo",MyApplication.workerId);
				rp.put("name",MyApplication.currentUserName);

				client.post(Urls.feedback,rp, new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
						if(Utils.byteArrayToStr(responseBody)!=null)
						{
							try {
								if(Utils.byteArrayToStr(responseBody).equals("ok"))
								{
									Toasty.success(FeedbackActivity.this, "提交成功!", Toast.LENGTH_SHORT, true).show();
									new Handler().postDelayed(new Runnable(){
										public void run() {
											finish();
										}
									}, 2000);
								}




							}catch (Exception e)
							{
								Toasty.error(FeedbackActivity.this, "提交失败1!"+e.getMessage(), Toast.LENGTH_SHORT, true).show();
							}

						}
					}

					@Override
					public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
						Toasty.error(FeedbackActivity.this, "提交失败2!"+ error.getMessage(), Toast.LENGTH_SHORT, true).show();
					}
				});
			}
		});


	}

}
