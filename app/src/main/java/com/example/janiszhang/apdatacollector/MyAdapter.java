package com.example.janiszhang.apdatacollector;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by janiszhang on 2016/3/8.
 */
public class MyAdapter extends ArrayAdapter<APData>{

    private Context mContext;
    private List<APData> mData;


    public MyAdapter(Context context, int resource, List<APData> objects) {
        super(context, resource, objects);
        mContext = context;
        mData = objects;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        APData item = getItem(position);
        ViewHolder viewHolder;
        if(convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.itemlayout, null);
            viewHolder = new ViewHolder();
            viewHolder.bssidAndLevel = (TextView) convertView.findViewById(R.id.item);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.bssidAndLevel.setText(item.getName() + " | " + item.getMacAddr() + " | " + item.getLevel());
        return convertView;
    }


    class ViewHolder {
        TextView bssidAndLevel;
    }
}
