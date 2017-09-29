package com.bitekun.helper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.MapView;

public class SignActivity extends Activity {

	EditText et_username, et_password;
	Button btn_login;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign);

		/**et_username = (EditText) findViewById(R.id.et_username);
		et_password = (EditText) findViewById(R.id.et_password);

		btn_login = (Button) findViewById(R.id.btn_login);

		btn_login.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(SignActivity.this, MainActivity.class));
				finish();

			}
		});**/
		MapView mapView = (MapView) findViewById(R.id.map);
		mapView.onCreate(savedInstanceState);// 此方法必须重写
		AMap aMap = mapView.getMap();

	}

}
