package com.bitekun.helper;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.bitekun.helper.R;

public class UploadActivity extends Activity {

	EditText et_username, et_password;
	Button btn_login;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_upload);

		et_username = (EditText) findViewById(R.id.et_username);
		et_password = (EditText) findViewById(R.id.et_password);

		btn_login = (Button) findViewById(R.id.btn_login);

		btn_login.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				

			}
		});

	}
	


}
