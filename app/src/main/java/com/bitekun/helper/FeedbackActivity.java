package com.bitekun.helper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.MapView;

public class FeedbackActivity extends Activity {

	EditText et_title, et_content;
	Button btn_submit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feedback);

		et_title = (EditText) findViewById(R.id.et_title);
		et_content = (EditText) findViewById(R.id.et_content);

		btn_submit = (Button) findViewById(R.id.btn_submit);

		btn_submit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {


			}
		});


	}

}
