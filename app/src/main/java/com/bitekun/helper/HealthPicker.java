package com.bitekun.helper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import me.nereo.multi_image_selector.MultiImageSelectorActivity;
import me.nereo.multi_image_selector.bean.Image;


public class HealthPicker extends Activity implements View.OnClickListener {

    ImageView iv_livephoto,iv_recordphoto;
	int REQUEST_IMAGE =1;
	ArrayList<String> defaultDataArray;
	Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.health_picker);

		iv_livephoto = (ImageView)findViewById(R.id.iv_livephoto);
		iv_recordphoto = (ImageView)findViewById(R.id.iv_recordphoto);

		iv_livephoto.setOnClickListener(this);
		iv_recordphoto.setOnClickListener(this);

		defaultDataArray = new ArrayList<String>();

	}

	private void initData() {

	}


	@Override
	public void onClick(View view) {
		switch (view.getId())
		{
			case R.id.iv_livephoto:
				 intent = new Intent(this, MultiImageSelectorActivity.class);
// 是否显示调用相机拍照
				intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
// 最大图片选择数量
				intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 9);
// 设置模式 (支持 单选/MultiImageSelectorActivity.MODE_SINGLE 或者 多选/MultiImageSelectorActivity.MODE_MULTI)
				intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_SINGLE);
// 默认选择图片,回填选项(支持String ArrayList)
				intent.putStringArrayListExtra(MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST, defaultDataArray);
				startActivityForResult(intent, REQUEST_IMAGE);
				break;

			case R.id.iv_recordphoto:
				 intent = new Intent(this, MultiImageSelectorActivity.class);
// 是否显示调用相机拍照
				intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
// 最大图片选择数量
				intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 9);
// 设置模式 (支持 单选/MultiImageSelectorActivity.MODE_SINGLE 或者 多选/MultiImageSelectorActivity.MODE_MULTI)
				intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_MULTI);
// 默认选择图片,回填选项(支持String ArrayList)
				intent.putStringArrayListExtra(MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST, defaultDataArray);
				startActivityForResult(intent, REQUEST_IMAGE);
				break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == REQUEST_IMAGE){
			if(resultCode == RESULT_OK){
				// 获取返回的图片列表
				List<String> path = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
				// 处理你自己的逻辑 ....
				Toast.makeText(HealthPicker.this,path.get(0),Toast.LENGTH_SHORT).show();
			}
		}
	}
}
