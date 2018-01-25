package com.alibaba.alink.opensdksample.debugpage;

import com.alibaba.alink.opensdksample.R;
import com.alibaba.fastjson.JSONArray;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by huanyu.zhy on 17/5/25.
 */
public class ListViewAdapter extends BaseAdapter {
    LayoutInflater inflater;
    private JSONArray listData;

    public ListViewAdapter(Context context, JSONArray listData) {
        inflater = ((Activity) context).getLayoutInflater();
        this.listData = listData;
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView cateNameTextView;
        if (null == convertView) {
            convertView = inflater.inflate(R.layout.listitem, parent, false);
        }
        cateNameTextView = (TextView) convertView.findViewById(R.id.textview_name);
        cateNameTextView.setText(listData.getJSONObject(position).getString("title"));

        return convertView;
    }

}
