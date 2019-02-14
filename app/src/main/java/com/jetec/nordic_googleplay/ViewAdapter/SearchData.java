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

public class SearchData extends BaseAdapter {

    private ArrayList<String> timelist, Tlist, Hlist, Clist, idlist, new_I1, new_I2, new_I3;
    private Context context;
    private LayoutInflater inflater;
    private String getdate, getsize, gett, geth, getc, input1, input2, input3;

    public SearchData(Context context, ArrayList<String> timelist, ArrayList<String> Tlist, ArrayList<String> Hlist,
                      ArrayList<String> Clist, ArrayList<String> new_I1, ArrayList<String> new_I2,
                      ArrayList<String> new_I3, ArrayList<String> idlist, String getdate, String getsize, String gett,
                      String geth, String getc, String input1, String input2, String input3) {

        this.timelist = new ArrayList<String>();
        this.Tlist = new ArrayList<String>();
        this.Hlist = new ArrayList<String>();
        this.Clist = new ArrayList<String>();
        this.idlist = new ArrayList<String>();
        this.new_I1= new ArrayList<>();
        this.new_I2= new ArrayList<>();
        this.new_I3= new ArrayList<>();

        this.Tlist.clear();
        this.Hlist.clear();
        this.Clist.clear();
        this.new_I1.clear();
        this.new_I2.clear();
        this.new_I3.clear();
        this.timelist.clear();
        this.idlist.clear();

        this.context = context;
        inflater = LayoutInflater.from(context);
        this.timelist = timelist;
        this.Tlist = Tlist;
        this.Hlist = Hlist;
        this.Clist = Clist;
        this.new_I1 = new_I1;
        this.new_I2 = new_I2;
        this.new_I3 = new_I3;
        this.idlist = idlist;
        this.getdate = getdate;
        this.getsize = getsize;
        this.gett = gett;
        this.geth = geth;
        this.getc = getc;
        this.input1 = input1;
        this.input2 = input2;
        this.input3 = input3;
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
        TextView I1 = ((TextView) view.findViewById(R.id.textView6));
        TextView I2 = ((TextView) view.findViewById(R.id.textView7));
        TextView I3 = ((TextView) view.findViewById(R.id.textView8));

        T.setVisibility(View.GONE);
        H.setVisibility(View.GONE);
        C.setVisibility(View.GONE);
        I1.setVisibility(View.GONE);
        I2.setVisibility(View.GONE);
        I3.setVisibility(View.GONE);

        time.setText(getdate + " : " + timelist.get(position));
        value.setText(getsize + " : " + idlist.get(position));
        if(Tlist.size() > 0){
            T.setVisibility(View.VISIBLE);
            T.setText(gett + " : " + String.valueOf(Float.valueOf(Tlist.get(position)) / 10) + " ˚C");
        }
        if(Hlist.size() > 0){
            H.setVisibility(View.VISIBLE);
            H.setText(geth + " : " + String.valueOf(Float.valueOf(Hlist.get(position)) / 10) + " %");
        }
        if(Clist.size() > 0){
            C.setVisibility(View.VISIBLE);
            C.setText(getc + " : " + String.valueOf(Float.valueOf(Clist.get(position)) / 10) + " %");
        }if(new_I1.size() > 0){
            I1.setVisibility(View.VISIBLE);
            I1.setText(input1 + " : " + String.valueOf(Float.valueOf(new_I1.get(position))));
        }if(new_I2.size() > 0){
            I2.setVisibility(View.VISIBLE);
            I2.setText(input2 + " : " + String.valueOf(Float.valueOf(new_I2.get(position))));
        }if(new_I3.size() > 0){
            I3.setVisibility(View.VISIBLE);
            I3.setText(input3 + " : " + String.valueOf(Float.valueOf(new_I3.get(position))));
        }

        return view;
    }
}
