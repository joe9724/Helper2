package com.bitekun.helper;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class UploadActivity extends Activity {

	EditText et_username, et_password;
	Button btn_login;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_upload);

		et_username = (EditText) findViewById(R.id.et_oldpass);
		et_password = (EditText) findViewById(R.id.et_newpass);

		btn_login = (Button) findViewById(R.id.btn_login);

		btn_login.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				

			}
		});

	}
	


}
