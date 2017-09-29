package com.bitekun.helper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import me.nereo.multi_image_selector.MultiImageSelector;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

public class SplashActivity extends AppCompatActivity  implements  View.OnClickListener{

    int REQUEST_IMAGE =1;
    ArrayList<String> defaultDataArray;
    Button btn,btn2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        btn = (Button)findViewById(R.id.button);
        btn.setOnClickListener(this);

        btn2 = (Button)findViewById(R.id.button2);
        btn2.setOnClickListener(this);

        defaultDataArray = new ArrayList<String>();

        startActivity(new Intent(SplashActivity.this,LoginActivity.class));

    }

    //
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_IMAGE){
            if(resultCode == RESULT_OK){
                // 获取返回的图片列表
                List<String> path = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                // 处理你自己的逻辑 ....
            }
        }
    }

    @Override
    public void onClick(View view) {
            switch (view.getId())
        {
            case R.id.button:
                Intent intent = new Intent(this, MultiImageSelectorActivity.class);
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
            case R.id.button2:
                Intent i = new Intent(this,HelpPeopleActvitity.class);
            startActivity(i);
                break;

        }
    }
}
