package com.bitekun.helper.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bitekun.helper.R;
import com.bitekun.helper.bean.HelpPeopleListItem;


public class AdapterHelpPeople extends BaseAdapter {

	private Context mContext;
	private ArrayList<HelpPeopleListItem> mGridData = new ArrayList<HelpPeopleListItem>();

	public AdapterHelpPeople(Context context) {
		mContext = context;
	}

	public void setDataList(ArrayList<HelpPeopleListItem> dataList) {
		this.mGridData = dataList;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;

		if (convertView == null) {
			LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
			convertView = inflater.inflate(R.layout.item_helppeople, parent,
					false);
			holder = new ViewHolder();
			holder.textView = (TextView) convertView.findViewById(R.id.tv_name);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		HelpPeopleListItem item = mGridData.get(position);
		holder.textView.setText(item.getName());
		convertView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});

		return convertView;
	}

	private class ViewHolder {
		TextView textView;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

}
