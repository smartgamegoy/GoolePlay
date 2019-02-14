package com.jetec.nordic_googleplay.ViewAdapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.jetec.nordic_googleplay.R;
import com.jetec.nordic_googleplay.Value;

import java.util.ArrayList;

public class ChartData extends BaseAdapter {

    private ArrayList<String> timelist, Firstlist, Secondlist, Thirdlist;
    private Context context;
    private LayoutInflater inflater;
    private String getdate, getsize, gett, geth, getc;

    public ChartData(Context context, ArrayList<String> timelist, ArrayList<String> Firstlist, ArrayList<String> Secondlist,
                     ArrayList<String> Thirdlist, String getdate, String getsize, String gett,
                     String geth, String getc) {

        this.Firstlist = new ArrayList<String>();
        this.Secondlist = new ArrayList<String>();
        this.Thirdlist = new ArrayList<String>();

        this.Firstlist.clear();
        this.Secondlist.clear();
        this.Thirdlist.clear();

        this.context = context;
        inflater = LayoutInflater.from(context);
        this.timelist = timelist;
        this.Firstlist = Firstlist;
        this.Secondlist = Secondlist;
        this.Thirdlist = Thirdlist;
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
        TextView first = ((TextView) view.findViewById(R.id.textView3));
        TextView second = ((TextView) view.findViewById(R.id.textView4));
        TextView third = ((TextView) view.findViewById(R.id.textView5));
        TextView textView6 = ((TextView) view.findViewById(R.id.textView6));
        TextView textView7 = ((TextView) view.findViewById(R.id.textView7));
        TextView textView8 = ((TextView) view.findViewById(R.id.textView8));

        textView6.setVisibility(View.GONE);
        textView7.setVisibility(View.GONE);
        textView8.setVisibility(View.GONE);

        time.setText(getdate + " : " + timelist.get(position));
        value.setText(getsize + " : " + String.valueOf((position + 1)));
        if(Firstlist.size() > 0){
            first.setVisibility(View.VISIBLE);
            if(Value.name.get(0).toString().matches("I")){
                first.setText(gett + " : " + String.valueOf(Float.valueOf(Firstlist.get(position))));
            }
            else if(Value.name.get(0).toString().matches("T")){
                first.setText(gett + " : " + String.valueOf(Float.valueOf(Firstlist.get(position))) + " ˚C");
            }
            else if(Value.name.get(0).toString().matches("H")){
                first.setText(gett + " : " + String.valueOf(Float.valueOf(Firstlist.get(position))) + " %");
            }
            else if(Value.name.get(0).toString().matches("C")){

            }
        }
        else
            first.setVisibility(View.GONE);
        if(Secondlist.size() > 0){
            second.setVisibility(View.VISIBLE);
            if(Value.name.get(1).toString().matches("I")){
                second.setText(geth + " : " + String.valueOf(Float.valueOf(Secondlist.get(position))));
            }
            else if(Value.name.get(1).toString().matches("T")){
                second.setText(geth + " : " + String.valueOf(Float.valueOf(Secondlist.get(position))) + " ˚C");
            }
            else if(Value.name.get(1).toString().matches("H")){
                second.setText(geth + " : " + String.valueOf(Float.valueOf(Secondlist.get(position))) + " %");
            }
            else if(Value.name.get(1).toString().matches("C")){

            }
        }
        else
            second.setVisibility(View.GONE);
        if(Thirdlist.size() > 0){
            third.setVisibility(View.VISIBLE);
            if(Value.name.get(2).toString().matches("I")){
                third.setText(String.valueOf(Float.valueOf(Thirdlist.get(position))));
            }
            else if(Value.name.get(2).toString().matches("T")){
                third.setText(String.valueOf(Float.valueOf(Thirdlist.get(position))) + " ˚C");
            }
            else if(Value.name.get(2).toString().matches("H")){
                third.setText(String.valueOf(Float.valueOf(Thirdlist.get(position))) + " %");
            }
            else if(Value.name.get(2).toString().matches("C")){

            }
        }
        else
            third.setVisibility(View.GONE);

        return view;
    }
}
