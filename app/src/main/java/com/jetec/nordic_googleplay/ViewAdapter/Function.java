package com.jetec.nordic_googleplay.ViewAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.jetec.nordic_googleplay.R;

import java.util.List;

public class Function extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private List<String> list_d_function;
    private List<String> list_d_num;
    private List<String> selectItem;
    private double all_Width;

    public Function(Context context, List<String> list_d_function, List<String> list_d_num,
                    List<String> selectItem, double all_Width) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.list_d_function = list_d_function;
        this.list_d_num = list_d_num;
        this.selectItem = selectItem;
        this.all_Width = all_Width;
    }

    @Override
    public int getCount() {
        return selectItem.size();
    }

    @Override
    public Object getItem(int position) {
        return selectItem.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewGroup view;

        if (convertView != null) {
            view = (ViewGroup) convertView;
        } else {
            view = (ViewGroup) inflater.inflate(R.layout.show_device_funtion, null);
        }

        String name = list_d_function.get(position);
        String num = list_d_num.get(position);
        final TextView device_name = ((TextView) view.findViewById(R.id.device_name));
        final TextView device_num = ((TextView) view.findViewById(R.id.device_num));
        //device_name.setPadding((int)(all_Width/8),0,0,0);
        //device_num.setPadding(0,0,(int)(all_Width/8),0);
        device_name.setVisibility(View.VISIBLE);
        device_num.setVisibility(View.VISIBLE);
        device_name.setText(name);
        device_num.setText(num);
        return view;
    }
}
