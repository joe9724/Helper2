package com.bitekun.helper;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.GridView;

import com.bitekun.helper.R;
import com.bitekun.helper.adapter.AdapterGV;
import com.bitekun.helper.bean.GridItem;


public class MainActivity extends Activity
{
	private String[] localCartoonText = {"��ƶ����", "�ֳ�ǩ��", "����ɼ�", "�Ұ����","������¼","�������"};
    private GridView mGridView = null;
    private AdapterGV mGridViewAdapter = null;
    private ArrayList<GridItem> mGridData = null;



	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//
		mGridView = (GridView) findViewById(R.id.gv);
        mGridData = new ArrayList<GridItem>();
        for (int i=0; i<localCartoonText.length; i++) {
            GridItem item = new GridItem();
            item.setTitle(localCartoonText[i]);
            mGridData.add(item);
        }
        mGridViewAdapter = new AdapterGV(this, R.layout.grid_item, mGridData);
        mGridView.setAdapter(mGridViewAdapter);


	}



}
