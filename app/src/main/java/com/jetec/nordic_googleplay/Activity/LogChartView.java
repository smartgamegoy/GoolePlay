package com.jetec.nordic_googleplay.Activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import com.jetec.nordic_googleplay.R;
import com.jetec.nordic_googleplay.ViewAdapter.ChartList;
import com.jetec.nordic_googleplay.ViewAdapter.CustomMarkerView;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.opencsv.CSVWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import static java.lang.Thread.sleep;

public class LogChartView extends AppCompatActivity {

    private int orientation;
    private LineChart lc;
    private Vibrator vibrator;
    private Dialog chartdialog = null;
    private int dialogflag, select_item = -1;
    private View view1;
    private Uri csvuri;
    private File file;
    private ArrayList<String> Tlist, Hlist, Clist, charttime, timelist, List_d_num;
    private double all_Width, all_Height;
    private String device, TAG = "Logview";
    private YAxis leftAxis;
    private LimitLine yLimitLinedown, yLimitLineup;
    private boolean setdpp;
    private ChartList chartList;
    private FileWriter mFileWriter;
    private CSVWriter writer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logview); //布局
        orientation = getResources().getConfiguration().orientation;
        vibrator = (Vibrator) this.getSystemService(VIBRATOR_SERVICE);
        list();
        Intent intent = getIntent();
        this.all_Width = intent.getDoubleExtra("all_Width", all_Width);
        this.all_Height = intent.getDoubleExtra("all_Height", all_Height);
        this.device = intent.getStringExtra("device");
        this.setdpp = intent.getBooleanExtra("setdpp", setdpp);
        this.charttime = intent.getStringArrayListExtra("charttime");
        this.timelist = intent.getStringArrayListExtra("timelist");
        this.Clist = intent.getStringArrayListExtra("Clist");
        this.Hlist = intent.getStringArrayListExtra("Hlist");
        this.Tlist = intent.getStringArrayListExtra("Tlist");
        this.List_d_num = intent.getStringArrayListExtra("List_d_num");
        for(int i = 0; i < timelist.size(); i++)
            Log.e(TAG, "888 = " + timelist.get(i));
        try {
            sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logview();
    }

    private void list(){
        Tlist = new ArrayList<String>();
        Hlist = new ArrayList<String>();
        Clist = new ArrayList<String>();
        charttime = new ArrayList<String>();
        timelist = new ArrayList<String>();
        List_d_num = new ArrayList<String>();

        Tlist.clear();
        Hlist.clear();
        Clist.clear();
        charttime.clear();
        timelist.clear();
        List_d_num.clear();
    }

    private AdapterView.OnItemClickListener mchosechart = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            vibrator.vibrate(100);
            //一開始未選擇任何一個item所以為-1
            //======================
            //點選某個item並呈現被選取的狀態
            if ((select_item == -1) || (select_item==position)){
                view.setBackgroundColor(Color.YELLOW); //為View加上選取效果
            }else{
                view1.setBackgroundDrawable(null); //將上一次點選的View保存在view1
                view.setBackgroundColor(Color.YELLOW); //為View加上選取效果
            }
            view1 = view; //保存點選的View
            select_item = position;//保存目前的View位置
            //======================
            view1 = view; //保存點選的View
            select_item = position;//保存目前的View位置
            dialogflag = select_item;
        }
    };

    private void logview(){

        dialogflag = 0;

        Button b1 = (Button)findViewById(R.id.button1);
        Button b2 = (Button)findViewById(R.id.button2);
        Button b3 = (Button)findViewById(R.id.button3);

        new Thread(packagecsv).start();

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(100);
                chartdialog = Dialogview(LogChartView.this);
                chartdialog.show();
                chartdialog.setCanceledOnTouchOutside(false);
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(100);

                Intent intent = new Intent(LogChartView.this, ChartActivity.class);

                intent.putStringArrayListExtra("charttime", charttime);
                intent.putStringArrayListExtra("timelist", timelist);
                intent.putStringArrayListExtra("Tlist",Tlist);
                intent.putStringArrayListExtra("Hlist",Hlist);
                intent.putStringArrayListExtra("Clist",Clist);
                intent.putExtra("all_Height", all_Height);
                intent.putExtra("all_Width", all_Width);

                startActivity(intent);
            }
        });

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(100);
                csvuri = Uri.fromFile(file);
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_STREAM, csvuri);
                shareIntent.setType("text/*");
                startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.send_to)));
            }
        });

        initView();
        initData();
    }

    private void initView() {
        lc = (LineChart) findViewById(R.id.chart1);
    }

    private void initData() {
        lc.setExtraOffsets((int)(2 * all_Width / 100),(int)(3 * all_Height / 100),(int)(4 * all_Width / 100),(int)(all_Height / 100));
        setDescription(device);
        lc.animateXY(800, 800);   //繪製延遲動畫
        setLegend();
        setYAxis();
        setXAxis();
        setChartData();
    }

    private void setDescription(String descriptionStr) {
        Description description = new Description();
        description.setText(descriptionStr);
        Paint paint = new Paint();
        paint.setTextSize(20);
        float x = (float)(all_Width - all_Width / 100);
        float y =  (float)(2 * all_Height / 100);
        description.setPosition(x, y);
        lc.setDescription(description);
    }

    private void setLegend() {
        Legend legend = lc.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setTextSize(14);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        legend.setTextColor(Color.BLACK);
    }

    private void setYAxis() {
        final YAxis yAxisLeft = lc.getAxisLeft();
        if(dialogflag == 0){
            if(leftAxis != null) {
                leftAxis.removeLimitLine(yLimitLinedown);
                leftAxis.removeLimitLine(yLimitLineup);
            }
            yAxisLeft.setAxisMaximum(100);
            yAxisLeft.setAxisMinimum(-20);
            yAxisLeft.setGranularity(1);
            yAxisLeft.setTextSize(14);
            yAxisLeft.setTextColor(Color.BLACK);
            yAxisLeft.setValueFormatter(new IAxisValueFormatter() {
                @Override
                public String getFormattedValue(float value, AxisBase axis) {
                    return String.valueOf((int)value);
                }
            });
        }
        else if(dialogflag == 1){   //溫度
            if(leftAxis != null) {
                leftAxis.removeLimitLine(yLimitLinedown);
                leftAxis.removeLimitLine(yLimitLineup);
            }
            yLimitLinedown = new LimitLine(Float.valueOf(List_d_num.get(3)),"溫度上限");  //上限線
            yLimitLinedown.enableDashedLine((float) all_Width / 100,(float) all_Width / 100,1);
            yLimitLinedown.setTextSize(14);
            yLimitLinedown.setLineColor(Color.RED);
            yLimitLinedown.setTextColor(Color.RED);
            leftAxis = lc.getAxisLeft();
            leftAxis.addLimitLine(yLimitLinedown);

            yLimitLineup = new LimitLine(Float.valueOf(List_d_num.get(4)),"溫度下限");    //下限線
            yLimitLineup.enableDashedLine((float) all_Width / 100,(float) all_Width / 100,1);
            yLimitLineup.setTextSize(14);
            yLimitLineup.setLineColor(Color.CYAN);
            yLimitLineup.setTextColor(Color.CYAN);
            YAxis leftAxis2 = lc.getAxisLeft();
            leftAxis2.addLimitLine(yLimitLineup);

            yAxisLeft.setAxisMaximum(100);
            yAxisLeft.setAxisMinimum(-10);
            yAxisLeft.setGranularity(1);
            yAxisLeft.setTextSize(14);
            yAxisLeft.setTextColor(Color.BLACK);
            yAxisLeft.setValueFormatter(new IAxisValueFormatter() {
                @Override
                public String getFormattedValue(float value, AxisBase axis) {
                    if(!setdpp)
                        return String.valueOf((int)value);
                    else
                        return String.valueOf(value);
                }
            });
        }
        else if(dialogflag == 2){   //濕度
            if(leftAxis != null) {
                leftAxis.removeLimitLine(yLimitLinedown);
                leftAxis.removeLimitLine(yLimitLineup);
            }
            yLimitLinedown = new LimitLine(Float.valueOf(List_d_num.get(5)),"濕度上限");  //上限線
            yLimitLinedown.enableDashedLine((float) all_Width / 100,(float) all_Width / 100,1);
            yLimitLinedown.setTextSize(14);
            yLimitLinedown.setLineColor(Color.RED);
            yLimitLinedown.setTextColor(Color.RED);
            leftAxis = lc.getAxisLeft();
            leftAxis.addLimitLine(yLimitLinedown);

            yLimitLineup = new LimitLine(Float.valueOf(List_d_num.get(6)),"濕度下限");    //下限線
            yLimitLineup.enableDashedLine((float) all_Width / 100,(float) all_Width / 100,1);
            yLimitLineup.setTextSize(14);
            yLimitLineup.setLineColor(Color.CYAN);
            yLimitLineup.setTextColor(Color.CYAN);
            YAxis leftAxis2 = lc.getAxisLeft();
            leftAxis2.addLimitLine(yLimitLineup);

            yAxisLeft.setAxisMaximum(100);
            yAxisLeft.setAxisMinimum(0);
            yAxisLeft.setGranularity(1);
            yAxisLeft.setTextSize(14);
            yAxisLeft.setTextColor(Color.BLACK);
            yAxisLeft.setValueFormatter(new IAxisValueFormatter() {
                @Override
                public String getFormattedValue(float value, AxisBase axis) {
                    if(!setdpp)
                        return String.valueOf((int)value);
                    else
                        return String.valueOf(value);
                }
            });
        }
        else {  //CO2

        }

        lc.getAxisRight().setEnabled(false);
    }

    private void setXAxis() {
        XAxis xAxis = lc.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);  //格線
        xAxis.setTextColor(Color.BLACK);
        xAxis.setTextSize(14);
        xAxis.setGranularity(1);    //間隔
        xAxis.setAxisMinimum(0);
        xAxis.setAxisMaximum(timelist.size());
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                if(value == 0) {
                    return "";
                }
                else {
                    return timelist.get(((int)value) - 1);
                }
            }
        });
    }

    public void setChartData() {

        CustomMarkerView mv = new CustomMarkerView(this, R.layout.custom_marker_view_layout, all_Width, all_Height);
        lc.setMarkerView(mv);

        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();

        if(dialogflag == 0) {
            dataSets.add(lineDataSet(ChartData(Tlist), getString(R.string.temperature), Color.GREEN));
            dataSets.add(lineDataSet(ChartData(Hlist), getString(R.string.humidity), Color.BLUE));
        }
        else if(dialogflag == 1){
            dataSets.add(lineDataSet(ChartData(Tlist), getString(R.string.temperature), Color.GREEN));
        }
        else if(dialogflag == 2){
            dataSets.add(lineDataSet(ChartData(Hlist), getString(R.string.humidity), Color.BLUE));
        }
        else {

        }

        LineData lineData = new LineData(dataSets);
        lineData.setDrawValues(false);

        lc.setVisibleXRangeMaximum(timelist.size());
        lc.setScaleXEnabled(true);
        lc.setData(lineData);
    }

    private LineDataSet lineDataSet(List<Entry> ChartData, String getname, int color){
        LineDataSet lineDataSet = new LineDataSet(ChartData, getname);
        lineDataSet.setDrawCircleHole(true);   //空心圓點
        lineDataSet.setColor(color); //線的顏色green
        lineDataSet.setMode(LineDataSet.Mode.LINEAR);
        lineDataSet.setDrawCircles(true);
        lineDataSet.setCubicIntensity(1);  //强度
        lineDataSet.setCircleColor(color);    //圓點顏色
        lineDataSet.setLineWidth(1);

        lineDataSet.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return String.valueOf(value);
            }
        });
        return lineDataSet;
    }

    private List<Entry> ChartData(List<String> list){
        List<Entry> data = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if(!setdpp) {
                float getdata = (Integer.valueOf(list.get(i)) / 10);
                data.add(new Entry((i + 1 ),getdata));
            }
            else{
                float getdata = (Float.valueOf(list.get(i)) / 10);
                data.add(new Entry((i + 1 ),getdata));
            }
        }
        return  data;
    }

    private Dialog Dialogview(Context context){
        Dialog progressDialog = new Dialog(context);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        LayoutInflater inflater = LayoutInflater.from(LogChartView.this);
        View v = inflater.inflate(R.layout.chosedialog,null);
        LinearLayout chart = (LinearLayout)v.findViewById(R.id.chart);
        ListView chart_list = (ListView)v.findViewById(R.id.datalist1);
        Button b1 = (Button)v.findViewById(R.id.button1);
        Button b2 = (Button)v.findViewById(R.id.button2);

        String[] chartview = {getString(R.string.Combine),getString(R.string.Temperature),getString(R.string.Humidity)};

        chartList = new ChartList(this, all_Width, all_Height, chartview);
        chart_list.setAdapter(chartList);
        chart_list.setOnItemClickListener(mchosechart);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(100);
                select_item = -1;
                chartdialog.dismiss();
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(100);
                select_item = -1;
                initView();
                initData();
                chartdialog.dismiss();
            }
        });

        progressDialog.setContentView(chart, new LinearLayout.LayoutParams((int)(3 * all_Width / 5),
                (int)(2 * all_Height / 5)));

        return progressDialog;
    }

    private Runnable packagecsv = new Runnable() {
        @Override
        public void run() {
            try {
                String[] data = {"id", "dateTime", "Temperature/C  ", "Humidity/%"};
                String[] data2;
                String baseDir = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
                // SD卡位置getApplicationContext().getFilesDir().getAbsolutePath();
                Log.e(TAG, "baseDir = " + baseDir);
                String fileName = "log.csv";
                String filePath = baseDir + File.separator + fileName;
                Log.e(TAG, "filePath = " + filePath);
                file = new File(filePath);
                Log.e(TAG, "overthere?");
                if (file.exists() && !file.isDirectory()) {
                    mFileWriter = new FileWriter(filePath, false);
                    writer = new CSVWriter(mFileWriter);
                    writer.writeNext(data);
                    for (int i = 0; i < charttime.size(); i++) {
                        data2 = new String[]{String.valueOf(i), charttime.get(i),
                                String.valueOf(Float.valueOf(Tlist.get(i)) / 10),
                                String.valueOf(Float.valueOf(Hlist.get(i)) / 10)};
                        Log.e(TAG, "data2 = " + data2);
                        writer.writeNext(data2);
                    }
                    Log.e(TAG,"writer = " + writer);
                    writer.close();
                    Log.e(TAG, "there?");
                } else {
                    writer = new CSVWriter(new FileWriter(filePath));
                    writer.writeNext(data);
                    for (int i = 0; i < charttime.size(); i++) {
                        data2 = new String[]{String.valueOf(i), charttime.get(i), Tlist.get(i), Hlist.get(i)};
                        Log.e(TAG, "data2 = " + data2);
                        writer.writeNext(data2);
                    }
                    writer.close();
                    Log.e(TAG, "here?");
                }
            }catch (IOException e){
                Log.e(TAG, "Wrong = " + e);
            }
        }
    };

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // land do nothing is ok
            setContentView(R.layout.logview);
            logview();
        } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            // port do nothing is ok
            setContentView(R.layout.logview);
            logview();
        }
    }

    private void back(){
        Intent result = new Intent();
        setResult(DeviceFunction.RESULT_OK, result);
        finish();
    }

    public boolean onKeyDown(int key, KeyEvent event) {
        switch (key) {
            case KeyEvent.KEYCODE_SEARCH:
                break;
            case KeyEvent.KEYCODE_BACK: {
                vibrator.vibrate(100);
                back();
            }
                break;
            case KeyEvent.KEYCODE_DPAD_CENTER:
                break;
            default:
                return false;
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG,"onDestroy()");
    }

    @Override
    protected void onStop(){
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
