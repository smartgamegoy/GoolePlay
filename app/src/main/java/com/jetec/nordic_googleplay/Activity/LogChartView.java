package com.jetec.nordic_googleplay.Activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.jetec.nordic_googleplay.CreatPDF.CreatTable;
import com.jetec.nordic_googleplay.CreatPDF.FooterHandler;
import com.jetec.nordic_googleplay.CreatPDF.HeaderHandler;
import com.jetec.nordic_googleplay.R;
import com.jetec.nordic_googleplay.Value;
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
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LogChartView extends AppCompatActivity {

    private int orientation, flag = 0;
    private LineChart lc;
    private Vibrator vibrator;
    private Dialog chartdialog = null, running = null;
    private int dialogflag = 0, select_item = -1;
    private View view1;
    private Uri csvuri;
    private File file, pdffile;
    private ArrayList<String> Firstlist, Secondlist, Thirdlist, charttime, timelist, List_d_num;
    private double all_Width, all_Height;
    private String device, TAG = "Logview";
    private YAxis leftAxis;
    private LimitLine yLimitLinedown, yLimitLineup;
    private boolean setdpp = false;
    private FileOutputStream fOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.logview); //布局

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();

        orientation = getResources().getConfiguration().orientation;
        vibrator = (Vibrator) this.getSystemService(VIBRATOR_SERVICE);
        list();

        all_Width = Value.all_Width;
        all_Height = Value.all_Height;
        device = Value.BName;
        charttime = Value.charttime;
        timelist = Value.timelist;
        Thirdlist = Value.Thirdlist;
        Secondlist = Value.Secondlist;
        Firstlist = Value.Firstlist;
        List_d_num = Value.List_d_num;

        new Thread(packagecsv).start();
        new Thread(makepdf).start();

        logview();
    }

    private void list() {
        Firstlist = new ArrayList<>();
        Secondlist = new ArrayList<>();
        Thirdlist = new ArrayList<>();
        charttime = new ArrayList<>();
        timelist = new ArrayList<>();
        List_d_num = new ArrayList<>();

        Firstlist.clear();
        Secondlist.clear();
        Thirdlist.clear();
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
            if ((select_item == -1) || (select_item == position)) {
                view.setBackgroundColor(Color.YELLOW); //為View加上選取效果
            } else {
                //noinspection deprecation
                view1.setBackgroundDrawable(null); //將上一次點選的View保存在view1
                view.setBackgroundColor(Color.YELLOW); //為View加上選取效果
            }
            view1 = view; //保存點選的View
            select_item = position;//保存目前的View位置
            //======================
            dialogflag = select_item;
        }
    };

    private void logview() {

        dialogflag = 0;

        Button b1 = findViewById(R.id.button1);
        Button b2 = findViewById(R.id.button2);
        Button b3 = findViewById(R.id.button3);
        Button b4 = findViewById(R.id.button4);

        running = writeDialog(this, getString(R.string.process));

        b1.setOnClickListener(v -> {
            vibrator.vibrate(100);
            chartdialog = Dialogview(LogChartView.this);
            chartdialog.show();
            chartdialog.setCanceledOnTouchOutside(false);
        });

        b2.setOnClickListener(v -> {
            vibrator.vibrate(100);

            Intent intent = new Intent(LogChartView.this, ChartActivity.class);

            Value.charttime = charttime;
            Value.timelist = timelist;
            Value.Firstlist = Firstlist;
            Value.Secondlist = Secondlist;
            Value.Thirdlist = Thirdlist;
            Value.all_Height = all_Height;
            Value.all_Width = all_Width;

            startActivity(intent);
        });

        b3.setOnClickListener(v -> {
            vibrator.vibrate(100);
            new AlertDialog.Builder(this)
                    .setTitle(R.string.report)
                    .setMessage(R.string.choose)
                    .setPositiveButton(R.string.CSV, (dialog, which) -> {
                        vibrator.vibrate(100);
                        csvuri = Uri.fromFile(file);
                        Intent shareIntent = new Intent();
                        shareIntent.setAction(Intent.ACTION_SEND);
                        shareIntent.putExtra(Intent.EXTRA_STREAM, csvuri);
                        shareIntent.setType("text/*");
                        startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.send_to)));
                    })
                    .setNegativeButton(R.string.PDF, (dialog, which) -> {
                        vibrator.vibrate(100);
                        flag = 1;
                        if (!setdpp) {
                            running.show();
                            running.setCanceledOnTouchOutside(false);
                        } else {
                            Intent shareIntent = new Intent();
                            shareIntent.setAction(Intent.ACTION_SEND);
                            shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(pdffile));
                            shareIntent.setType("text/*");
                            startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.send_to)));
                        }
                    })
                    .setNeutralButton(R.string.mes_no, (dialog, which) -> vibrator.vibrate(100))
                    .show();
        });

        b4.setOnClickListener(v -> {
            vibrator.vibrate(100);
            flag = 2;
            if (!setdpp) {
                running.show();
                running.setCanceledOnTouchOutside(false);
            } else {
                Intent intent = new Intent(LogChartView.this, PDFView.class);
                startActivity(intent);
                finish();
            }
        });

        initView();
        initData();
    }

    private void initView() {
        lc = findViewById(R.id.chart1);
    }

    private void initData() {
        lc.clear();
        lc.setExtraOffsets((int) (2 * all_Width / 100), (int) (3 * all_Height / 100),
                (int) (4 * all_Width / 100), (int) (all_Height / 100));
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
        float x = (float) (all_Width - all_Width / 100);
        float y = (float) (2 * all_Height / 100);
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
        if (dialogflag == 0) {
            if (leftAxis != null) {
                leftAxis.removeLimitLine(yLimitLinedown);
                leftAxis.removeLimitLine(yLimitLineup);
            }
            ArrayList<String> Mm = new ArrayList<>();
            Mm.clear();
            Mm.addAll(Firstlist);
            Mm.addAll(Secondlist);
            Mm.addAll(Thirdlist);
            float maxIndex = Float.valueOf(Mm.get(0));
            float minIndex = Float.valueOf(Mm.get(0));
            for (int i = 0; i < Mm.size(); i++) {
                if (maxIndex < Float.valueOf(Mm.get(i))) {
                    maxIndex = Float.valueOf(Mm.get(i));
                }
                if (minIndex > Float.valueOf(Mm.get(i))) {
                    minIndex = Float.valueOf(Mm.get(i));
                }
            }
            maxIndex = maxIndex + 10;
            minIndex = minIndex - 10;

            Log.e(TAG, "maxIndex = " + maxIndex);
            Log.e(TAG, "minIndex = " + minIndex);

            yAxisLeft.setAxisMaximum(maxIndex);
            yAxisLeft.setAxisMinimum(minIndex);
            yAxisLeft.setGranularity(1);
            yAxisLeft.setTextSize(14);
            yAxisLeft.setTextColor(Color.BLACK);
            yAxisLeft.setValueFormatter(new IAxisValueFormatter() {
                @Override
                public String getFormattedValue(float value, AxisBase axis) {
                    return String.valueOf((int) value);
                }
            });
        } else if (dialogflag == 1) {   //第一排
            if (leftAxis != null) {
                Log.e(TAG,"開始清屏");
                leftAxis.removeLimitLine(yLimitLinedown);
                leftAxis.removeLimitLine(yLimitLineup);
                Log.e(TAG,"清屏null");
            }
            if (Value.name.get(0).toString().matches("T")) {
                yLimitLinedown = new LimitLine(Float.valueOf(List_d_num.get(Value.SelectItem.indexOf("EH1"))),
                        getString(R.string.Temperature) + getString(R.string.UL));  //上限線
            }
            if (Value.name.get(0).toString().matches("H")) {
                yLimitLinedown = new LimitLine(Float.valueOf(List_d_num.get(Value.SelectItem.indexOf("EH1"))),
                        getString(R.string.Humidity) + getString(R.string.UL));  //上限線
            }
            if (Value.name.get(0).toString().matches("C")) {
                yLimitLinedown = new LimitLine(Float.valueOf(List_d_num.get(Value.SelectItem.indexOf("EH1"))),
                        getString(R.string.Co2) + getString(R.string.UL));  //上限線
            }
            if (Value.name.get(0).toString().matches("I")) {
                yLimitLinedown = new LimitLine(Float.valueOf(List_d_num.get(Value.SelectItem.indexOf("EH1"))),
                        getString(R.string.I1) + getString(R.string.UL));  //上限線
            }
            yLimitLinedown.enableDashedLine((float) all_Width / 100, (float) all_Width / 100, 1);
            yLimitLinedown.setTextSize(14);
            yLimitLinedown.setLineColor(Color.RED);
            yLimitLinedown.setTextColor(Color.RED);
            leftAxis = lc.getAxisLeft();
            leftAxis.addLimitLine(yLimitLinedown);

            if (Value.name.get(0).toString().matches("T")) {
                yLimitLineup = new LimitLine(Float.valueOf(List_d_num.get(Value.SelectItem.indexOf("EL1"))),
                        getString(R.string.Temperature) + getString(R.string.LL));  //下限線
            }
            if (Value.name.get(0).toString().matches("H")) {
                yLimitLineup = new LimitLine(Float.valueOf(List_d_num.get(Value.SelectItem.indexOf("EL1"))),
                        getString(R.string.Humidity) + getString(R.string.LL));  //下限線
            }
            if (Value.name.get(0).toString().matches("C")) {
                yLimitLineup = new LimitLine(Float.valueOf(List_d_num.get(Value.SelectItem.indexOf("EL1"))),
                        getString(R.string.Co2) + getString(R.string.LL));  //下限線
            }
            if (Value.name.get(0).toString().matches("I")) {
                yLimitLineup = new LimitLine(Float.valueOf(List_d_num.get(Value.SelectItem.indexOf("EL1"))),
                        getString(R.string.I1) + getString(R.string.LL));  //下限線
            }
            yLimitLineup.enableDashedLine((float) all_Width / 100, (float) all_Width / 100, 1);
            yLimitLineup.setTextSize(14);
            yLimitLineup.setLineColor(Color.CYAN);
            yLimitLineup.setTextColor(Color.CYAN);
            YAxis leftAxis2 = lc.getAxisLeft();
            leftAxis2.addLimitLine(yLimitLineup);

            ArrayList<String> Mm = new ArrayList<>();
            Mm.clear();
            Mm.addAll(Firstlist);
            float maxIndex = Float.valueOf(Mm.get(0));
            float minIndex = Float.valueOf(Mm.get(0));
            for (int i = 0; i < Mm.size(); i++) {
                if (maxIndex < Float.valueOf(Mm.get(i))) {
                    maxIndex = Float.valueOf(Mm.get(i));
                }
                if (minIndex > Float.valueOf(Mm.get(i))) {
                    minIndex = Float.valueOf(Mm.get(i));
                }
            }

            maxIndex = maxIndex + 10;
            minIndex = minIndex - 10;

            Log.e(TAG, "maxIndex = " + maxIndex);
            Log.e(TAG, "minIndex = " + minIndex);

            yAxisLeft.setAxisMaximum(maxIndex);
            yAxisLeft.setAxisMinimum(minIndex);
            yAxisLeft.setGranularity(1);
            yAxisLeft.setTextSize(14);
            yAxisLeft.setTextColor(Color.BLACK);
            yAxisLeft.setValueFormatter(new IAxisValueFormatter() {
                @Override
                public String getFormattedValue(float value, AxisBase axis) {
                    return String.valueOf((int) value);
                }
            });
        } else if (dialogflag == 2) {   //第二排
            if (leftAxis != null) {
                Log.e(TAG,"開始清屏");
                leftAxis.removeLimitLine(yLimitLinedown);
                leftAxis.removeLimitLine(yLimitLineup);
                Log.e(TAG,"清屏null");
            }
            if (Value.name.get(1).toString().matches("T")) {
                yLimitLinedown = new LimitLine(Float.valueOf(List_d_num.get(Value.SelectItem.indexOf("EH2"))),
                        getString(R.string.Temperature) + getString(R.string.UL));  //上限線
            }
            if (Value.name.get(1).toString().matches("H")) {
                yLimitLinedown = new LimitLine(Float.valueOf(List_d_num.get(Value.SelectItem.indexOf("EH2"))),
                        getString(R.string.Humidity) + getString(R.string.UL));  //上限線
            }
            if (Value.name.get(1).toString().matches("C")) {
                yLimitLinedown = new LimitLine(Float.valueOf(List_d_num.get(Value.SelectItem.indexOf("EH2"))),
                        getString(R.string.Co2) + getString(R.string.UL));  //上限線
            }
            if (Value.name.get(1).toString().matches("I")) {
                yLimitLinedown = new LimitLine(Float.valueOf(List_d_num.get(Value.SelectItem.indexOf("EH2"))),
                        getString(R.string.I2) + getString(R.string.UL));  //上限線
            }
            yLimitLinedown.enableDashedLine((float) all_Width / 100, (float) all_Width / 100, 1);
            yLimitLinedown.setTextSize(14);
            yLimitLinedown.setLineColor(Color.RED);
            yLimitLinedown.setTextColor(Color.RED);
            leftAxis = lc.getAxisLeft();
            leftAxis.addLimitLine(yLimitLinedown);

            if (Value.name.get(1).toString().matches("T")) {
                yLimitLineup = new LimitLine(Float.valueOf(List_d_num.get(Value.SelectItem.indexOf("EL2"))),
                        getString(R.string.Temperature) + getString(R.string.LL));  //下限線
            }
            if (Value.name.get(1).toString().matches("H")) {
                yLimitLineup = new LimitLine(Float.valueOf(List_d_num.get(Value.SelectItem.indexOf("EL2"))),
                        getString(R.string.Humidity) + getString(R.string.LL));  //下限線
            }
            if (Value.name.get(1).toString().matches("C")) {
                yLimitLineup = new LimitLine(Float.valueOf(List_d_num.get(Value.SelectItem.indexOf("EL2"))),
                        getString(R.string.Co2) + getString(R.string.LL));  //下限線
            }
            if (Value.name.get(1).toString().matches("I")) {
                yLimitLineup = new LimitLine(Float.valueOf(List_d_num.get(Value.SelectItem.indexOf("EL2"))),
                        getString(R.string.I2) + getString(R.string.LL));  //下限線
            }

            yLimitLineup.enableDashedLine((float) all_Width / 100, (float) all_Width / 100, 1);
            yLimitLineup.setTextSize(14);
            yLimitLineup.setLineColor(Color.CYAN);
            yLimitLineup.setTextColor(Color.CYAN);
            YAxis leftAxis2 = lc.getAxisLeft();
            leftAxis2.addLimitLine(yLimitLineup);

            ArrayList<String> Mm = new ArrayList<>();
            Mm.clear();
            Mm.addAll(Secondlist);
            float maxIndex = Float.valueOf(Mm.get(0));
            float minIndex = Float.valueOf(Mm.get(0));
            for (int i = 0; i < Mm.size(); i++) {
                if (maxIndex < Float.valueOf(Mm.get(i))) {
                    maxIndex = Float.valueOf(Mm.get(i));
                }
                if (minIndex > Float.valueOf(Mm.get(i))) {
                    minIndex = Float.valueOf(Mm.get(i));
                }
            }

            maxIndex = maxIndex + 10;
            minIndex = minIndex - 10;

            Log.e(TAG, "maxIndex = " + maxIndex);
            Log.e(TAG, "minIndex = " + minIndex);

            yAxisLeft.setAxisMaximum(maxIndex);
            yAxisLeft.setAxisMinimum(minIndex);
            yAxisLeft.setGranularity(1);
            yAxisLeft.setTextSize(14);
            yAxisLeft.setTextColor(Color.BLACK);
            yAxisLeft.setValueFormatter(new IAxisValueFormatter() {
                @Override
                public String getFormattedValue(float value, AxisBase axis) {
                    return String.valueOf(value);
                }
            });
        } else {  //CO2
            if (leftAxis != null) {
                Log.e(TAG,"開始清屏");
                leftAxis.removeLimitLine(yLimitLinedown);
                leftAxis.removeLimitLine(yLimitLineup);
                Log.e(TAG,"清屏null");
            }
            if (Value.name.get(2).toString().matches("T")) {
                yLimitLinedown = new LimitLine(Float.valueOf(List_d_num.get(Value.SelectItem.indexOf("EH3"))),
                        getString(R.string.Temperature) + getString(R.string.UL));  //上限線
            }
            if (Value.name.get(2).toString().matches("H")) {
                yLimitLinedown = new LimitLine(Float.valueOf(List_d_num.get(Value.SelectItem.indexOf("EH3"))),
                        getString(R.string.Humidity) + getString(R.string.UL));  //上限線
            }
            if (Value.name.get(2).toString().matches("C")) {
                yLimitLinedown = new LimitLine(Float.valueOf(List_d_num.get(Value.SelectItem.indexOf("EH3"))),
                        getString(R.string.Co2) + getString(R.string.UL));  //上限線
            }
            if (Value.name.get(2).toString().matches("I")) {
                yLimitLinedown = new LimitLine(Float.valueOf(List_d_num.get(Value.SelectItem.indexOf("EH3"))),
                        getString(R.string.I2) + getString(R.string.UL));  //上限線
            }
            yLimitLinedown.enableDashedLine((float) all_Width / 100, (float) all_Width / 100, 1);
            yLimitLinedown.setTextSize(14);
            yLimitLinedown.setLineColor(Color.RED);
            yLimitLinedown.setTextColor(Color.RED);
            leftAxis = lc.getAxisLeft();
            leftAxis.addLimitLine(yLimitLinedown);

            if (Value.name.get(2).toString().matches("T")) {
                yLimitLineup = new LimitLine(Float.valueOf(List_d_num.get(Value.SelectItem.indexOf("EL3"))),
                        getString(R.string.Temperature) + getString(R.string.LL));  //下限線
            }
            if (Value.name.get(2).toString().matches("H")) {
                yLimitLineup = new LimitLine(Float.valueOf(List_d_num.get(Value.SelectItem.indexOf("EL3"))),
                        getString(R.string.Humidity) + getString(R.string.LL));  //下限線
            }
            if (Value.name.get(2).toString().matches("C")) {
                yLimitLineup = new LimitLine(Float.valueOf(List_d_num.get(Value.SelectItem.indexOf("EL3"))),
                        getString(R.string.Co2) + getString(R.string.LL));  //下限線
            }
            if (Value.name.get(2).toString().matches("I")) {
                yLimitLineup = new LimitLine(Float.valueOf(List_d_num.get(Value.SelectItem.indexOf("EL3"))),
                        getString(R.string.I2) + getString(R.string.LL));  //下限線
            }

            yLimitLineup.enableDashedLine((float) all_Width / 100, (float) all_Width / 100, 1);
            yLimitLineup.setTextSize(14);
            yLimitLineup.setLineColor(Color.CYAN);
            yLimitLineup.setTextColor(Color.CYAN);
            YAxis leftAxis2 = lc.getAxisLeft();
            leftAxis2.addLimitLine(yLimitLineup);

            ArrayList<String> Mm = new ArrayList<>();
            Mm.clear();
            Mm.addAll(Thirdlist);
            float maxIndex = Float.valueOf(Mm.get(0));
            float minIndex = Float.valueOf(Mm.get(0));
            for (int i = 0; i < Mm.size(); i++) {
                if (maxIndex < Float.valueOf(Mm.get(i))) {
                    maxIndex = Float.valueOf(Mm.get(i));
                }
                if (minIndex > Float.valueOf(Mm.get(i))) {
                    minIndex = Float.valueOf(Mm.get(i));
                }
            }

            maxIndex = maxIndex + 10;
            minIndex = minIndex - 10;

            Log.e(TAG, "maxIndex = " + maxIndex);
            Log.e(TAG, "minIndex = " + minIndex);

            yAxisLeft.setAxisMaximum(maxIndex);
            yAxisLeft.setAxisMinimum(minIndex);
            yAxisLeft.setGranularity(1);
            yAxisLeft.setTextSize(14);
            yAxisLeft.setTextColor(Color.BLACK);
            yAxisLeft.setValueFormatter(new IAxisValueFormatter() {
                @Override
                public String getFormattedValue(float value, AxisBase axis) {
                    return String.valueOf(value);
                }
            });
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
                if (value == 0) {
                    return "";
                } else {
                    return timelist.get(((int) value) - 1);
                }
            }
        });
    }

    public void setChartData() {

        CustomMarkerView mv = new CustomMarkerView(this, R.layout.custom_marker_view_layout, all_Width, all_Height);
        lc.setMarkerView(mv);

        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();

        if (dialogflag == 0) {
            if (Firstlist.size() != 0) {
                if (Value.name.get(0).toString().matches("I")) {
                    dataSets.add(lineDataSet(ChartData(Firstlist), getString(R.string.I1row), Color.GREEN));
                } else if (Value.name.get(0).toString().matches("T")) {
                    dataSets.add(lineDataSet(ChartData(Firstlist), getString(R.string.temperature), Color.GREEN));
                } else if (Value.name.get(0).toString().matches("H")) {
                    dataSets.add(lineDataSet(ChartData(Firstlist), getString(R.string.humidity), Color.BLUE));
                } else if (Value.name.get(0).toString().matches("C")) {
                    Log.e(TAG, "待增加");
                }
            }
            if (Secondlist.size() != 0) {
                if (Value.name.get(1).toString().matches("I")) {
                    dataSets.add(lineDataSet(ChartData(Secondlist), getString(R.string.I2row), Color.BLUE));
                } else if (Value.name.get(1).toString().matches("T")) {
                    dataSets.add(lineDataSet(ChartData(Secondlist), getString(R.string.temperature), Color.GREEN));
                } else if (Value.name.get(1).toString().matches("H")) {
                    dataSets.add(lineDataSet(ChartData(Secondlist), getString(R.string.humidity), Color.BLUE));
                } else if (Value.name.get(1).toString().matches("C")) {
                    Log.e(TAG, "待增加");
                }
            }
            if (Thirdlist.size() != 0) {
                if (Value.name.get(2).toString().matches("I")) {
                    dataSets.add(lineDataSet(ChartData(Thirdlist), getString(R.string.I3row), Color.MAGENTA));
                } else if (Value.name.get(2).toString().matches("T")) {
                    dataSets.add(lineDataSet(ChartData(Thirdlist), getString(R.string.temperature), Color.GREEN));
                } else if (Value.name.get(2).toString().matches("H")) {
                    dataSets.add(lineDataSet(ChartData(Thirdlist), getString(R.string.humidity), Color.BLUE));
                } else if (Value.name.get(2).toString().matches("C")) {
                    Log.e(TAG, "待增加");
                }
            }
        } else if (dialogflag == 1) {
            if (Firstlist.size() != 0) {
                if (Value.name.get(0).toString().matches("I")) {
                    dataSets.add(lineDataSet(ChartData(Firstlist), getString(R.string.I1row), Color.GREEN));
                } else if (Value.name.get(0).toString().matches("T")) {
                    dataSets.add(lineDataSet(ChartData(Firstlist), getString(R.string.temperature), Color.GREEN));
                } else if (Value.name.get(0).toString().matches("H")) {
                    dataSets.add(lineDataSet(ChartData(Firstlist), getString(R.string.humidity), Color.BLUE));
                } else if (Value.name.get(0).toString().matches("C")) {
                    Log.e(TAG, "待增加");
                }
            }
        } else if (dialogflag == 2) {
            if (Secondlist.size() != 0) {
                if (Value.name.get(1).toString().matches("I")) {
                    dataSets.add(lineDataSet(ChartData(Secondlist), getString(R.string.I2row), Color.BLUE));
                } else if (Value.name.get(1).toString().matches("T")) {
                    dataSets.add(lineDataSet(ChartData(Secondlist), getString(R.string.temperature), Color.GREEN));
                } else if (Value.name.get(1).toString().matches("H")) {
                    dataSets.add(lineDataSet(ChartData(Secondlist), getString(R.string.humidity), Color.BLUE));
                } else if (Value.name.get(1).toString().matches("C")) {
                    Log.e(TAG, "待增加");
                }
            }
        } else {
            if (Thirdlist.size() != 0) {
                if (Value.name.get(2).toString().matches("I")) {
                    dataSets.add(lineDataSet(ChartData(Thirdlist), getString(R.string.I3row), Color.MAGENTA));
                } else if (Value.name.get(2).toString().matches("T")) {
                    dataSets.add(lineDataSet(ChartData(Thirdlist), getString(R.string.temperature), Color.GREEN));
                } else if (Value.name.get(2).toString().matches("H")) {
                    dataSets.add(lineDataSet(ChartData(Thirdlist), getString(R.string.humidity), Color.BLUE));
                } else if (Value.name.get(2).toString().matches("C")) {
                    Log.e(TAG, "待增加");
                }
            }
        }

        LineData lineData = new LineData(dataSets);
        lineData.setDrawValues(false);

        lc.setVisibleXRangeMaximum(timelist.size());
        lc.setScaleXEnabled(true);
        lc.setData(lineData);
    }

    private LineDataSet lineDataSet(List<Entry> ChartData, String getname, int color) {
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

    private List<Entry> ChartData(List<String> list) {
        List<Entry> data = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            float getdata = (Float.valueOf(list.get(i)));
            data.add(new Entry((i + 1), getdata));
        }
        return data;
    }

    private Dialog Dialogview(Context context) {
        Dialog progressDialog = new Dialog(context);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        LayoutInflater inflater = LayoutInflater.from(LogChartView.this);
        View v = inflater.inflate(R.layout.chosedialog, null);
        LinearLayout chart = v.findViewById(R.id.chart);
        ListView chart_list = v.findViewById(R.id.datalist1);
        Button b1 = v.findViewById(R.id.button1);
        Button b2 = v.findViewById(R.id.button2);

        ArrayList<String> chartview = new ArrayList<>();
        chartview.clear();
        chartview.add(getString(R.string.Combine));

        if (Firstlist.size() != 0) {
            if (Value.name.get(0).toString().matches("I")) {
                chartview.add(getString(R.string.I1row));
            } else if (Value.name.get(0).toString().matches("T")) {
                chartview.add(getString(R.string.Temperature));
            } else if (Value.name.get(0).toString().matches("H")) {
                chartview.add(getString(R.string.Humidity));
            } else if (Value.name.get(0).toString().matches("C")) {
                chartview.add(getString(R.string.Co2));
            }
        }
        if (Secondlist.size() != 0) {
            if (Value.name.get(1).toString().matches("I")) {
                chartview.add(getString(R.string.I2row));
            } else if (Value.name.get(1).toString().matches("T")) {
                chartview.add(getString(R.string.Temperature));
            } else if (Value.name.get(1).toString().matches("H")) {
                chartview.add(getString(R.string.Humidity));
            } else if (Value.name.get(1).toString().matches("C")) {
                chartview.add(getString(R.string.Co2));
            }
        }
        if (Thirdlist.size() != 0) {
            if (Value.name.get(2).toString().matches("I")) {
                chartview.add(getString(R.string.I3row));
            } else if (Value.name.get(2).toString().matches("T")) {
                chartview.add(getString(R.string.Temperature));
            } else if (Value.name.get(2).toString().matches("H")) {
                chartview.add(getString(R.string.Humidity));
            } else if (Value.name.get(2).toString().matches("C")) {
                chartview.add(getString(R.string.Co2));
            }
        }

        ChartList chartList = new ChartList(this, all_Width, all_Height, chartview);
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
                Log.e(TAG,"開始");
                //initView();
                initData();
                chartdialog.dismiss();
            }
        });

        progressDialog.setContentView(chart, new LinearLayout.LayoutParams((int) (3 * all_Width / 5),
                (int) (2 * all_Height / 5)));

        return progressDialog;
    }

    private Dialog writeDialog(Context context, String message) {
        final Dialog progressDialog = new Dialog(context);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        progressDialog.dismiss();

        LayoutInflater inflater = LayoutInflater.from(context);
        @SuppressLint("InflateParams") View v = inflater.inflate(R.layout.running, null);
        LinearLayout layout = v.findViewById(R.id.ll_dialog);
        ProgressBar pb_progress_bar = v.findViewById(R.id.pb_progress_bar);
        pb_progress_bar.setVisibility(View.VISIBLE);
        TextView tv = v.findViewById(R.id.tv_loading);

        if (message == null || message.equals("")) {
            tv.setVisibility(View.GONE);
        } else {
            tv.setText(message);
            tv.setTextColor(context.getResources().getColor(R.color.colorDialog));
        }

        progressDialog.setContentView(layout, new LinearLayout.LayoutParams((int) (Value.all_Width / 2),
                (int) (Value.all_Height / 5)));

        progressDialog.setOnKeyListener((dialog, keyCode, event) -> {
            vibrator.vibrate(100);
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                //noinspection deprecation
                return false;
            } else {
                return false;
            }
        });
        return progressDialog;
    }

    private Runnable makepdf = new Runnable() {
        @Override
        public void run() {
            String baseDir = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
            // SD卡位置getApplicationContext().getFilesDir().getAbsolutePath();
            // 系統位置android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
            String fileName = "JetecRemote" + ".pdf";
            String filePath = baseDir + File.separator + fileName;
            pdffile = new File(filePath);
            Log.e(TAG, "file = " + pdffile);
            try {
                int alldata = Value.charttime.size(), page;

                if (alldata % 300 == 0) {
                    page = alldata / 300;
                } else {
                    page = (alldata / 300) + 1;
                }

                HeaderHandler head = new HeaderHandler();
                FooterHandler foot = new FooterHandler();
                CreatTable table = new CreatTable();
                Document document = new Document(PageSize.A4, -20, -20, 40, 40);
                FileOutputStream fOut = new FileOutputStream(pdffile);
                PdfWriter writer = PdfWriter.getInstance(document, fOut);
                BaseFont bfChinese = BaseFont.createFont("STSongStd-Light", "UniGB-UTF16-H", true);
                Font chineseFont = new Font(bfChinese, 12, Font.NORMAL);
                String gettitle = Value.BName;
                Paragraph title = new Paragraph(gettitle, chineseFont);
                head.setHeader(title);
                foot.setallpage(page);
                writer.setPageEvent(head);
                writer.setPageEvent(foot);

                document.open();
                table.setList(LogChartView.this, Firstlist, Secondlist, Thirdlist, charttime, page);
                document.add(table.createTable(document));
                document.close();
                Log.e(TAG, "已完成");
                setdpp = true;
                if (running.isShowing()) {
                    running.dismiss();
                    if (flag == 1) {
                        Intent shareIntent = new Intent();
                        shareIntent.setAction(Intent.ACTION_SEND);
                        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(pdffile));
                        shareIntent.setType("text/*");
                        startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.send_to)));
                    } else if (flag == 2) {
                        Intent intent = new Intent(LogChartView.this, PDFView.class);
                        startActivity(intent);
                        finish();
                    }
                }
            } catch (IOException | DocumentException e) {
                e.printStackTrace();
            }
        }
    };

    private Runnable packagecsv = new Runnable() {
        @Override
        public void run() {
            try {
                ArrayList<String> data = new ArrayList<>();
                data.clear();
                data.add("id");
                data.add("dateTime");
                if (Firstlist.size() != 0) {
                    if (Value.name.get(0).toString().matches("I")) {
                        data.add("Analog1");
                    } else if (Value.name.get(0).toString().matches("T")) {
                        data.add("Temperature/C");
                    } else if (Value.name.get(0).toString().matches("H")) {
                        data.add("Humidity/%");
                    } else if (Value.name.get(0).toString().matches("C")) {
                        Log.e(TAG, "待增加");
                    }
                }
                if (Secondlist.size() != 0) {
                    if (Value.name.get(1).toString().matches("I")) {
                        data.add("Analog2");
                    } else if (Value.name.get(1).toString().matches("T")) {
                        data.add("Temperature/C");
                    } else if (Value.name.get(1).toString().matches("H")) {
                        data.add("Humidity/%");
                    } else if (Value.name.get(1).toString().matches("C")) {
                        Log.e(TAG, "待增加");
                    }
                }
                if (Thirdlist.size() != 0) {
                    if (Value.name.get(2).toString().matches("I")) {
                        data.add("Analog3");
                    } else if (Value.name.get(2).toString().matches("T")) {
                        data.add("Temperature/C");
                    } else if (Value.name.get(2).toString().matches("H")) {
                        data.add("Humidity/%");
                    } else if (Value.name.get(2).toString().matches("C")) {
                        Log.e(TAG, "待增加");
                    }
                }
                String[] data_array = new String[data.size()];
                for (int i = 0; i < data.size(); i++) {
                    data_array[i] = data.get(i);
                    Log.e(TAG, "data_array[i] = " + data_array[i]);
                }
                Log.e(TAG, "data_array = " + data_array);
                Log.e(TAG, "Firstlist = " + Firstlist);
                Log.e(TAG, "Secondlist = " + Secondlist);
                Log.e(TAG, "Thirdlist = " + Thirdlist);
                String[] data2;
                String baseDir = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
                // SD卡位置getApplicationContext().getFilesDir().getAbsolutePath();
                // 系統位置android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
                Log.e(TAG, "baseDir = " + baseDir);
                String fileName = "JetecRemote" + ".csv";
                String filePath = baseDir + File.separator + fileName;
                Log.e(TAG, "filePath = " + filePath);
                file = new File(filePath);
                Log.e(TAG, "overthere?");
                CSVWriter writer;
                if (file.exists() && !file.isDirectory()) {
                    FileWriter mFileWriter = new FileWriter(filePath, false);
                    writer = new CSVWriter(mFileWriter);
                    writer.writeNext(data_array);
                    for (int i = 0; i < charttime.size(); i++) {
                        if(Firstlist.size() !=0 && Secondlist.size() != 0 && Thirdlist.size() != 0){
                            data2 = new String[]{String.valueOf(i), charttime.get(i),
                                    String.valueOf(Float.valueOf(Firstlist.get(i))),
                                    String.valueOf(Float.valueOf(Secondlist.get(i))),
                                    String.valueOf(Float.valueOf(Thirdlist.get(i)))};
                            writer.writeNext(data2);
                        }
                        else if(Firstlist.size() !=0 && Secondlist.size() != 0){
                            data2 = new String[]{String.valueOf(i), charttime.get(i),
                                    String.valueOf(Float.valueOf(Firstlist.get(i))),
                                    String.valueOf(Float.valueOf(Secondlist.get(i)))};
                            writer.writeNext(data2);
                        }
                        else if(Firstlist.size() !=0){
                            data2 = new String[]{String.valueOf(i), charttime.get(i),
                                    String.valueOf(Float.valueOf(Firstlist.get(i)))};
                            writer.writeNext(data2);
                        }
                    }
                    Log.e(TAG, "writer = " + writer);
                    writer.close();
                    Log.e(TAG, "there?");
                } else {
                    writer = new CSVWriter(new FileWriter(filePath));
                    writer.writeNext(data_array);
                    for (int i = 0; i < charttime.size(); i++) {
                        if(Firstlist.size() !=0 && Secondlist.size() != 0 && Thirdlist.size() != 0){
                            data2 = new String[]{String.valueOf(i), charttime.get(i),
                                    String.valueOf(Float.valueOf(Firstlist.get(i))),
                                    String.valueOf(Float.valueOf(Secondlist.get(i))),
                                    String.valueOf(Float.valueOf(Thirdlist.get(i)))};
                            writer.writeNext(data2);
                        }
                        else if(Firstlist.size() !=0 && Secondlist.size() != 0){
                            data2 = new String[]{String.valueOf(i), charttime.get(i),
                                    String.valueOf(Float.valueOf(Firstlist.get(i))),
                                    String.valueOf(Float.valueOf(Secondlist.get(i)))};
                            writer.writeNext(data2);
                        }
                        else if(Firstlist.size() !=0){
                            data2 = new String[]{String.valueOf(i), charttime.get(i),
                                    String.valueOf(Float.valueOf(Firstlist.get(i)))};
                            writer.writeNext(data2);
                        }
                    }
                    writer.close();
                    Log.e(TAG, "here?");
                }
            } catch (IOException e) {
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

    private void back() {
        //Intent result = new Intent();
        Intent intent = new Intent(LogChartView.this, DeviceFunction.class);
        startActivity(intent);
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
        Log.e(TAG, "onDestroy()");
    }

    @Override
    protected void onStop() {
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
