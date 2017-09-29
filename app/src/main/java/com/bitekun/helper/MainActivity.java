package com.bitekun.helper;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.GridView;
import android.widget.RelativeLayout;

import com.bitekun.helper.R;
import com.bitekun.helper.adapter.AdapterGV;
import com.bitekun.helper.bean.GridItem;


public class MainActivity extends Activity implements View.OnClickListener
{

 RelativeLayout layout_xcqd,layout_kfcj,layout_kfdx,layout_wfwd,layout_yjfk,layout_zx;
 Intent i;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		layout_xcqd = (RelativeLayout)findViewById(R.id.layout_xcqd);
				layout_kfcj = (RelativeLayout)findViewById(R.id.layout_kfcj);
		layout_kfdx = (RelativeLayout)findViewById(R.id.layout_kfdx);
				layout_wfwd = (RelativeLayout)findViewById(R.id.layout_wfwd);
		layout_yjfk = (RelativeLayout)findViewById(R.id.layout_yjfk);
				layout_zx = (RelativeLayout)findViewById(R.id.layout_zx);

		layout_xcqd.setOnClickListener(this);
		layout_kfcj.setOnClickListener(this);
		layout_kfdx.setOnClickListener(this);
		layout_wfwd.setOnClickListener(this);
		layout_yjfk.setOnClickListener(this);
		layout_zx.setOnClickListener(this);


	}


	@Override
	public void onClick(View view) {
		switch (view.getId())
		{
			case R.id.layout_xcqd:
                i = new Intent(this,SignActivity.class);
				startActivity(i);
				break;

			case R.id.layout_kfcj:
				i = new Intent(this,HealthPicker.class);
				startActivity(i);
				break;

			case R.id.layout_kfdx:
				i = new Intent(this,HelpPeopleActvitity.class);
				startActivity(i);
				break;

			case R.id.layout_wfwd:
				i = new Intent(this,MyHelpPeopleActvitity.class);
				startActivity(i);
				break;

			case R.id.layout_yjfk:
				i = new Intent(this,FeedbackActivity.class);
				startActivity(i);
				break;

			case R.id.layout_zx:

				break;
		}
	}
}
