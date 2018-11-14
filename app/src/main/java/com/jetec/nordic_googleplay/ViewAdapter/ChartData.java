package com.jetec.nordic_googleplay.ViewAdapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.jetec.nordic_googleplay.R;
import java.util.ArrayList;

public class ChartData extends BaseAdapter {

    private ArrayList<String> timelist, Tlist, Hlist, Clist;
    private Context context;
    private LayoutInflater inflater;
    private String getdate, getsize, gett, geth, getc;

    public ChartData(Context context, ArrayList<String> timelist, ArrayList<String> Tlist, ArrayList<String> Hlist,
                     ArrayList<String> Clist, String getdate, String getsize, String gett,
                     String geth, String getc) {

        this.Tlist = new ArrayList<String>();
        this.Hlist = new ArrayList<String>();
        this.Clist = new ArrayList<String>();

        this.Tlist.clear();
        this.Hlist.clear();
        this.Clist.clear();

        this.context = context;
        inflater = LayoutInflater.from(context);
        this.timelist = timelist;
        this.Tlist = Tlist;
        this.Hlist = Hlist;
        this.Clist = Clist;
        this.getdate = getdate;
        this.getsize = getsize;
        this.gett = gett;
        this.geth = geth;
        this.getc = getc;
    }

    @Override
    public int getCount() {
        return timelist.size();
    }

    @Override
    public Object getItem(int position) {
        return timelist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewGroup view;

        if (convertView != null) {
            view = (ViewGroup) convertView;
        } else {
            view = (ViewGroup) inflater.inflate(R.layout.showdata, null);
        }

        TextView time = ((TextView) view.findViewById(R.id.textView1));
        TextView value = ((TextView) view.findViewById(R.id.textView2));
        TextView T = ((TextView) view.findViewById(R.id.textView3));
        TextView H = ((TextView) view.findViewById(R.id.textView4));
        TextView C = ((TextView) view.findViewById(R.id.textView5));

        time.setText(getdate + " : " + timelist.get(position));
        value.setText(getsize + " : " + String.valueOf((position + 1)));
        if(Tlist.size() > 0){
            T.setVisibility(View.VISIBLE);
            T.setText(gett + " : " + String.valueOf(Float.valueOf(Tlist.get(position)) / 10) + " ˚C");
        }
        else
            T.setVisibility(View.GONE);
        if(Hlist.size() > 0){
            H.setVisibility(View.VISIBLE);
            H.setText(geth + " : " + String.valueOf(Float.valueOf(Hlist.get(position)) / 10) + " %");
        }
        else
            H.setVisibility(View.GONE);
        if(Clist.size() > 0){
            C.setVisibility(View.VISIBLE);
            C.setText(String.valueOf(Float.valueOf(Clist.get(position)) / 10) + " %");
        }
        else
            C.setVisibility(View.GONE);

        return view;
    }
}
