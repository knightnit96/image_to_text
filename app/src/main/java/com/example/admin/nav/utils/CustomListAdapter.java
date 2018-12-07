package com.example.admin.nav.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.admin.nav.R;
import com.example.admin.nav.model.ReceiveImageData;

import java.util.List;

public class CustomListAdapter extends BaseAdapter {

    private List<ReceiveImageData> listData;
    private LayoutInflater layoutInflater;
    private Context context;

    public CustomListAdapter(Context aContext, List<ReceiveImageData> listData) {
        this.context = aContext;
        this.listData = listData;
        layoutInflater = LayoutInflater.from(aContext);
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_item_layout, null);
            holder = new ViewHolder();
            holder.textNameView = convertView.findViewById(R.id.text_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ReceiveImageData receiveImageData = this.listData.get(position);
        holder.textNameView.setText(receiveImageData.getName());
        return convertView;
    }

    static class ViewHolder {
        TextView textNameView;
    }

}