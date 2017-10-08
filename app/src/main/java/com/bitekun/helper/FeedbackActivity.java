package com.bitekun.helper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
				client.post(Urls.feedback,rp, new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
						if(Utils.byteArrayToStr(responseBody)!=null)
						{
							try {
								if(Utils.byteArrayToStr(responseBody).equals("ok"))
								{
									Toast.makeText(FeedbackActivity.this,"提交成功",Toast.LENGTH_SHORT).show();
									finish();
								}




							}catch (Exception e)
							{
								Toast.makeText(FeedbackActivity.this,"Json解析失败",Toast.LENGTH_SHORT).show();
							}

						}
					}

					@Override
					public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
						Toast.makeText(FeedbackActivity.this,"请求失败",Toast.LENGTH_SHORT).show();
					}
				});
			}
		});


	}

}
