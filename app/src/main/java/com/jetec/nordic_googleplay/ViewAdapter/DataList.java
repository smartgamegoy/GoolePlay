package com.jetec.nordic_googleplay.ViewAdapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jetec.nordic_googleplay.R;
import com.jetec.nordic_googleplay.Value;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataList extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private ArrayList<HashMap<String, String>> list;
    private double all_Width;
    private List<String> name, list_value;

    public DataList(Context context, ArrayList<HashMap<String, String>> list, double all_Width) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.list = list;
        this.all_Width = all_Width;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public String getItem(int position) {
        return name.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewGroup view;

        name = new ArrayList<String>();
        list_value = new ArrayList<String>();

        name.clear();
        list_value.clear();

        if (convertView != null) {
            view = (ViewGroup) convertView;
        } else {
            view = (ViewGroup) inflater.inflate(R.layout.show_datalist, null);
        }

        TextView show_name = (TextView) view.findViewById(R.id.show_name);

        for (HashMap<String, String> map : list) {
            name.add(map.get("num"));
        }

        String show = name.get(position);
        show_name.setPadding((int) (all_Width / 8), 0, 0, 0);
        show_name.setVisibility(View.VISIBLE);
        show_name.setText(show);
        return view;
    }
}
