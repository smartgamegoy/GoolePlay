package com.jetec.nordic_googleplay.Activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.jetec.nordic_googleplay.R;
import com.jetec.nordic_googleplay.Value;
import com.jetec.nordic_googleplay.ViewAdapter.SearchData;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static java.lang.Thread.sleep;

public class SearchActivity extends AppCompatActivity {

    private Vibrator vibrator;
    private SearchData searchData;
    private Dialog datedialog = null, timedialog = null;
    private int page, state;
    private ArrayList<String> charttime, Firstlist, Secondlist, Thirdlist, condition1, condition2, condition3;
    private ArrayList<String> new_time, new_T, new_H, new_C, idlist;
    private String date, CO2, Humidity, Temperature, record, getdate = "yyyy-mm-dd", gettime = "00:00",
            TAG = "searchLog", chose1, chose2, intput;
    private double all_Width, all_Height;
    private TextView t2, t3;
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;
    private boolean c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vibrator = (Vibrator) this.getSystemService(VIBRATOR_SERVICE);
        searchmenu();
    }

    private static String Fix(int c){
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }

    private void searchmenu(){

        setContentView(R.layout.searchdatalist);

        Spinner s1 = (Spinner)findViewById(R.id.spinner1);
        Spinner s2 = (Spinner)findViewById(R.id.spinner2);
        EditText e1 = (EditText)findViewById(R.id.editText1);
        Button b1 = (Button)findViewById(R.id.button1);
        Button b2 = (Button)findViewById(R.id.button2);
        Button b3 = (Button)findViewById(R.id.button3);
        TextView t1 = (TextView)findViewById(R.id.textView5);
        t2 = (TextView)findViewById(R.id.textView6);
        t3 = (TextView)findViewById(R.id.textView7);
        LinearLayout l1 = (LinearLayout)findViewById(R.id.textlinear);

        page = 0;
        state = 0;

        charttime = new ArrayList<String>();
        Firstlist = new ArrayList<String>();
        Secondlist = new ArrayList<String>();
        Thirdlist = new ArrayList<String>();
        condition1 = new ArrayList<String>();
        condition2 = new ArrayList<String>();
        condition3 = new ArrayList<String>();
        new_time = new ArrayList<String>();
        new_T = new ArrayList<String>();
        new_H = new ArrayList<String>();
        new_C = new ArrayList<String>();
        idlist = new ArrayList<String>();

        charttime.clear();
        Firstlist.clear();
        Secondlist.clear();
        Thirdlist.clear();
        idlist.clear();
        condition1.clear();
        condition2.clear();
        condition3.clear();
        new_time.clear();
        new_T.clear();
        new_H.clear();
        new_C.clear();

        charttime = Value.charttime;
        Firstlist = Value.Firstlist;
        Secondlist = Value.Secondlist;
        Thirdlist = Value.Thirdlist;
        all_Width = Value.all_Width;
        all_Height = Value.all_Height;

        date = getString(R.string.datetime);    //項目，條件，數值
        if(Value.name.get(2).toString().matches("C")) {
            CO2 = getString(R.string.Co2);
        }
        else if(Value.name.get(2).toString().matches("T")){
            CO2 = getString(R.string.Temperature);
        }
        else if(Value.name.get(2).toString().matches("H")){
            CO2 = getString(R.string.Humidity);
        }
        else if(Value.name.get(2).toString().matches("I")){
            CO2 = getString(R.string.I3row);
        }

        if(Value.name.get(1).toString().matches("C")) {
            Humidity = getString(R.string.Co2);
        }
        else if(Value.name.get(1).toString().matches("T")){
            Humidity = getString(R.string.Temperature);
        }
        else if(Value.name.get(1).toString().matches("H")){
            Humidity = getString(R.string.Humidity);
        }
        else if(Value.name.get(1).toString().matches("I")){
            Humidity = getString(R.string.I2row);
        }

        if(Value.name.get(0).toString().matches("C")) {
            Temperature = getString(R.string.Co2);
        }
        else if(Value.name.get(0).toString().matches("T")){
            Temperature = getString(R.string.Temperature);
        }
        else if(Value.name.get(0).toString().matches("H")){
            Temperature = getString(R.string.Humidity);
        }
        else if(Value.name.get(0).toString().matches("I")){
            Temperature = getString(R.string.I1row);
        }

        record = getString(R.string.size);

        new Thread(timecheck).start();
        new Thread(arrayadd).start();
        for(;state == 0;){
            try {
                sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        s2.setEnabled(false);
        t3.setVisibility(View.GONE);
        e1.setVisibility(View.GONE);
        b1.setVisibility(View.GONE);
        b2.setVisibility(View.GONE);

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this, R.layout.spinner_style, condition1){    //android.R.layout.simple_spinner_item
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                }
                else
                {
                    return true;
                }
            }
        };

        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_style);    //R.layout.spinner_style
        s1.setAdapter(spinnerArrayAdapter);
        s1.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.e("myLog","position = " + position);
                if(position == 0){

                }
                else if(position == 1){
                    s2.setEnabled(true);
                    e1.setVisibility(View.GONE);
                    t3.setVisibility(View.VISIBLE);
                    b1.setVisibility(View.VISIBLE);
                    b2.setVisibility(View.VISIBLE);
                    l1.setVisibility(View.VISIBLE);
                    chose1 = date;
                }
                else if(position == 2){
                    s2.setEnabled(true);
                    e1.setVisibility(View.VISIBLE);
                    e1.setInputType(InputType.TYPE_CLASS_NUMBER);
                    e1.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
                    e1.setHint("1 ~ " + charttime.size());
                    t3.setVisibility(View.GONE);
                    b1.setVisibility(View.GONE);
                    b2.setVisibility(View.GONE);
                    l1.setVisibility(View.GONE);
                    chose1 = condition1.get(position);
                }
                else {
                    s2.setEnabled(true);
                    e1.setVisibility(View.VISIBLE);
                    e1.setInputType(InputType.TYPE_CLASS_TEXT);
                    if(position == 3){
                        e1.setHint(" - 10 ~ 65");
                        e1.addTextChangedListener(new TextWatcher() {
                            int l = 0;    //記錄字串删除字元之前，字串的長度
                            int location = 0; //記錄光標位置
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                l = s.length();
                                location = e1.getSelectionStart();
                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                Pattern p = Pattern.compile("^[-]?\\d{1,4}([.]\\d)?$");    //^\-?[0-5](.[0-9])?$
                                Matcher m = p.matcher(s.toString());
                                if(m.find() || ("").equals(s.toString())){
                                    c = true;
                                }else{
                                    c = false;
                                }
                            }

                            @Override
                            public void afterTextChanged(Editable s) {

                            }
                        });
                    }
                    else if(position == 4){
                        e1.setKeyListener(DigitsKeyListener.getInstance(".0123456789"));
                        e1.setHint(" 0 ~ 99");
                        e1.addTextChangedListener(new TextWatcher() {
                            int l = 0;    //記錄字串删除字元之前，字串的長度
                            int location = 0; //記錄光標位置
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                l = s.length();
                                location = e1.getSelectionStart();
                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                Pattern p = Pattern.compile("^?\\d{1,4}([.]\\d)?$");    //^\-?[0-5](.[0-9])?$
                                Matcher m = p.matcher(s.toString());
                                if(m.find() || ("").equals(s.toString())){
                                    c = true;
                                }else{
                                    c = false;
                                }
                            }

                            @Override
                            public void afterTextChanged(Editable s) {

                            }
                        });
                    }
                    else {

                    }
                    t3.setVisibility(View.GONE);
                    b1.setVisibility(View.GONE);
                    b2.setVisibility(View.GONE);
                    l1.setVisibility(View.GONE);
                    chose1 = condition1.get(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        ArrayAdapter<String> spinnerArrayAdapter2 = new ArrayAdapter<String>(
                this, R.layout.spinner_style, condition2){    //android.R.layout.simple_spinner_item
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                }
                else
                {
                    return true;
                }
            }
        };

        spinnerArrayAdapter2.setDropDownViewResource(R.layout.spinner_style);    //R.layout.spinner_style
        s2.setAdapter(spinnerArrayAdapter2);
        s2.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.e("myLog","position2 = " + position);
                chose2 = condition2.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datechose();
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timechose();
            }
        });

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(chose1.matches(date) && !chose2.matches("") && !getdate.matches("yyyy-mm-dd")){
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String timecomparison = getdate + " " + gettime + ":00";
                    try {
                        if(chose2.matches("＞")) {
                            if (sdf.parse(timecomparison).after(sdf.parse(charttime.get(charttime.size() - 1)))
                                    || sdf.parse(timecomparison).before(sdf.parse(charttime.get(0)))) {
                                Toast.makeText(SearchActivity.this, getString(R.string.wrong), Toast.LENGTH_SHORT).show();
                            }
                            else{
                                for(int i = 0; i < charttime.size(); i++){
                                    if(sdf.parse(timecomparison).before(sdf.parse(charttime.get(i)))){
                                        new_time.add(charttime.get(i));
                                        idlist.add(String.valueOf(i + 1));
                                        if (Firstlist.size() > 0)
                                            new_T.add(Firstlist.get(i));
                                        if (Secondlist.size() > 0)
                                            new_H.add(Secondlist.get(i));
                                        if (Thirdlist.size() > 0)
                                            new_T.add(Thirdlist.get(i));
                                    }
                                }
                                showpage();
                            }
                        }
                        else if(chose2.matches("≧")){
                            if (sdf.parse(timecomparison).after(sdf.parse(charttime.get(charttime.size() - 1)))
                                    || sdf.parse(timecomparison).before(sdf.parse(charttime.get(0)))) {
                                Toast.makeText(SearchActivity.this, getString(R.string.wrong), Toast.LENGTH_SHORT).show();
                            }
                            else {
                                for(int i = 0; i < charttime.size(); i++){
                                    if(sdf.parse(timecomparison).before(sdf.parse(charttime.get(i)))
                                            || sdf.parse(timecomparison).equals(sdf.parse(charttime.get(i)))){
                                        new_time.add(charttime.get(i));
                                        idlist.add(String.valueOf(i + 1));
                                        if(Firstlist.size() > 0)
                                            new_T.add(Firstlist.get(i));
                                        if(Secondlist.size() > 0)
                                            new_H.add(Secondlist.get(i));
                                        if(Thirdlist.size() > 0)
                                            new_T.add(Thirdlist.get(i));
                                    }
                                }
                                showpage();
                            }
                        }
                        else if(chose2.matches("＝")){
                            if (sdf.parse(timecomparison).after(sdf.parse(charttime.get(charttime.size() - 1)))
                                    || sdf.parse(timecomparison).before(sdf.parse(charttime.get(0)))) {
                                Toast.makeText(SearchActivity.this, getString(R.string.wrong), Toast.LENGTH_SHORT).show();
                            }
                            else {
                                for(int i = 0; i < charttime.size(); i++){
                                    if(sdf.parse(timecomparison).equals(sdf.parse(charttime.get(i)))){
                                        new_time.add(charttime.get(i));
                                        idlist.add(String.valueOf(i + 1));
                                        if(Firstlist.size() > 0)
                                            new_T.add(Firstlist.get(i));
                                        if(Secondlist.size() > 0)
                                            new_H.add(Secondlist.get(i));
                                        if(Thirdlist.size() > 0)
                                            new_T.add(Thirdlist.get(i));
                                    }
                                }
                                showpage();
                            }
                        }
                        else if(chose2.matches("≦")){
                            if (sdf.parse(timecomparison).after(sdf.parse(charttime.get(charttime.size() - 1)))
                                    || sdf.parse(timecomparison).before(sdf.parse(charttime.get(0)))) {
                                Toast.makeText(SearchActivity.this, getString(R.string.wrong), Toast.LENGTH_SHORT).show();
                            }
                            else{
                                for(int i = 0; i < charttime.size(); i++){
                                    if(sdf.parse(timecomparison).after(sdf.parse(charttime.get(i)))
                                            || sdf.parse(timecomparison).equals(sdf.parse(charttime.get(i)))){
                                        new_time.add(charttime.get(i));
                                        idlist.add(String.valueOf(i + 1));
                                        if(Firstlist.size() > 0)
                                            new_T.add(Firstlist.get(i));
                                        if(Secondlist.size() > 0)
                                            new_H.add(Secondlist.get(i));
                                        if(Thirdlist.size() > 0)
                                            new_T.add(Thirdlist.get(i));
                                    }
                                }
                                showpage();
                            }
                        }
                        else if(chose2.matches("＜")){
                            if (sdf.parse(timecomparison).after(sdf.parse(charttime.get(charttime.size() - 1)))
                                    || sdf.parse(timecomparison).before(sdf.parse(charttime.get(0)))) {
                                Toast.makeText(SearchActivity.this, getString(R.string.wrong), Toast.LENGTH_SHORT).show();
                            }
                            else {
                                for(int i = 0; i < charttime.size(); i++){
                                    if(sdf.parse(timecomparison).after(sdf.parse(charttime.get(i)))){
                                        new_time.add(charttime.get(i));
                                        idlist.add(String.valueOf(i + 1));
                                        if(Firstlist.size() > 0)
                                            new_T.add(Firstlist.get(i));
                                        if(Secondlist.size() > 0)
                                            new_H.add(Secondlist.get(i));
                                        if(Thirdlist.size() > 0)
                                            new_T.add(Thirdlist.get(i));
                                    }
                                }
                                showpage();
                            }
                        }
                        else
                            Toast.makeText(SearchActivity.this, getString(R.string.wrong), Toast.LENGTH_SHORT).show();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                else if(chose1.matches(record) && !chose2.matches(getString(R.string.condition2)) && !e1.getText().toString().trim().matches("")){
                    calculate(chose1, chose2, e1.getText().toString().trim());
                }
                else if(chose1.matches(Temperature) && !chose2.matches(getString(R.string.condition2)) && !e1.getText().toString().trim().matches("") && c){
                    if(Float.valueOf(e1.getText().toString().trim()) > -10 && Float.valueOf(e1.getText().toString().trim()) < 65)
                        calculate(chose1, chose2, e1.getText().toString().trim());
                    else
                        Toast.makeText(SearchActivity.this, getString(R.string.wrong), Toast.LENGTH_SHORT).show();
                }
                else if(chose1.matches(Humidity) && !chose2.matches(getString(R.string.condition2)) && !e1.getText().toString().trim().matches("") && c){
                    if(Float.valueOf(e1.getText().toString().trim()) > 0 && Float.valueOf(e1.getText().toString().trim()) < 99)
                        calculate(chose1, chose2, e1.getText().toString().trim());
                    else
                        Toast.makeText(SearchActivity.this, getString(R.string.wrong), Toast.LENGTH_SHORT).show();
                }
                else if(chose1.matches(CO2) && !chose2.matches(getString(R.string.condition2)) && !e1.getText().toString().trim().matches("")){
                    calculate(chose1, chose2, e1.getText().toString().trim());
                }
                else {
                    Toast.makeText(SearchActivity.this, getString(R.string.wrong), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void datechose(){
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR); // current year
        int mMonth = c.get(Calendar.MONTH); // current month
        int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
        getdate = " " + mYear + "-" + (mMonth + 1) + "-" + mDay + " ";
        // date picker dialog
        datePickerDialog = new DatePickerDialog(SearchActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        // set day of month , month and year value in the edit text
                        getdate = Fix(year) + "-" + Fix((monthOfYear + 1)) + "-" + Fix(dayOfMonth);
                        t2.setText(" " + getdate + "  " + gettime);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private void timechose(){
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minutes = c.get(Calendar.MINUTE);
        // date picker dialog
        timePickerDialog = new TimePickerDialog(SearchActivity.this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        gettime = Fix(hourOfDay) + ":" + Fix(minute);
                        t2.setText(" " + getdate + "  " + gettime);
                    }
                }, hour, minutes, false);
        timePickerDialog.show();
    }

    private Runnable timecheck = new Runnable() {
        @Override
        public void run() {
            String f_time = charttime.get(0).substring(0, charttime.get(0).length() - 3);
            String e_time = charttime.get(charttime.size() - 1).substring(0, charttime.get(charttime.size() - 1).length() - 3);
            t3.setText(getString(R.string.timerange) + ": " + f_time + " ~ " + e_time);
        }
    };

    private void calculate(String s1, String s2, String s3){

        if(s1.matches(record)){
            if(s2.matches("＞")){
                int i = Integer.valueOf(s3) - 1;
                int j = charttime.size() - 1;
                for(; i < j; i++){
                    new_time.add(charttime.get(i));
                    idlist.add(String.valueOf(i + 2));
                    if(Firstlist.size() > 0) {
                        new_T.add(Firstlist.get(i));
                    }
                    if(Secondlist.size() > 0){
                        new_H.add(Secondlist.get(i));
                    }
                    if(Thirdlist.size() > 0){
                        new_C.add(Thirdlist.get(i));
                    }
                }
                showpage();
            }
            else if(s2.matches("≧")){
                int i = Integer.valueOf(s3) - 1;
                int j = charttime.size() - 1;
                for(; i <= j; i++){
                    new_time.add(charttime.get(i));
                    idlist.add(String.valueOf(i + 1));
                    if(Firstlist.size() > 0) {
                        new_T.add(Firstlist.get(i));
                    }
                    if(Secondlist.size() > 0){
                        new_H.add(Secondlist.get(i));
                    }
                    if(Thirdlist.size() > 0){
                        new_C.add(Thirdlist.get(i));
                    }
                }
                showpage();
            }
            else if(s2.matches("＝")){
                int i = Integer.valueOf(s3) - 1;
                int j = charttime.size() - 1;
                new_time.add(charttime.get(i));
                idlist.add(String.valueOf(i + 1));
                if(Firstlist.size() > 0) {
                    new_T.add(Firstlist.get(j));
                }
                if(Secondlist.size() > 0){
                    new_H.add(Secondlist.get(j));
                }
                if(Thirdlist.size() > 0){
                    new_C.add(Thirdlist.get(j));
                }
                showpage();
            }
            else if(s2.matches("≦")){
                int i = Integer.valueOf(s3) - 1;
                for(int j = 0; j <= i; j++){
                    new_time.add(charttime.get(j));
                    idlist.add(String.valueOf(j + 1));
                    if(Firstlist.size() > 0) {
                        new_T.add(Firstlist.get(j));
                    }
                    if(Secondlist.size() > 0){
                        new_H.add(Secondlist.get(j));
                    }
                    if(Thirdlist.size() > 0){
                        new_C.add(Thirdlist.get(j));
                    }
                }
                showpage();
            }
            else if(s2.matches("＜")){
                int i = Integer.valueOf(s3) - 1;
                for(int j = 0; j < i; j++){
                    new_time.add(charttime.get(j));
                    idlist.add(String.valueOf(j + 1));
                    if(Firstlist.size() > 0) {
                        new_T.add(Firstlist.get(j));
                    }
                    if(Secondlist.size() > 0){
                        new_H.add(Secondlist.get(j));
                    }
                    if(Thirdlist.size() > 0){
                        new_C.add(Thirdlist.get(j));
                    }
                }
                showpage();
            }
            else{
                Toast.makeText(SearchActivity.this, getString(R.string.wrong), Toast.LENGTH_SHORT).show();
            }
        }
        else if(s1.matches(Temperature)){
            if(s2.matches("＞")){
                float i = Float.valueOf(s3);
                Log.e(TAG, "i = " + i);
                for(int k = 0; k < charttime.size(); k++){
                    if(Firstlist.size() > 0) {
                        if((Float.valueOf(Firstlist.get(k)) / 10) > i) {
                            Log.e(TAG, "Firstlist = " + Firstlist.get(k));
                            new_time.add(charttime.get(k));
                            idlist.add(String.valueOf(k + 1));
                            new_T.add(Firstlist.get(k));
                            if(Secondlist.size() > 0)
                                new_H.add(Secondlist.get(k));
                            if(Thirdlist.size() > 0)
                                new_C.add(Thirdlist.get(k));
                        }
                    }
                }
                showpage();
            }
            else if(s2.matches("≧")){
                float i = Float.valueOf(s3);
                for(int k = 0; k < charttime.size(); k++){
                    if(Firstlist.size() > 0) {
                        if((Float.valueOf(Firstlist.get(k)) / 10) >= i) {
                            new_time.add(charttime.get(k));
                            idlist.add(String.valueOf(k + 1));
                            new_T.add(Firstlist.get(k));
                            if(Secondlist.size() > 0)
                                new_H.add(Secondlist.get(k));
                            if(Thirdlist.size() > 0)
                                new_C.add(Thirdlist.get(k));
                        }
                    }
                }
                showpage();
            }
            else if(s2.matches("＝")){
                float i = Float.valueOf(s3);
                for(int k = 0; k < charttime.size(); k++){
                    if(Firstlist.size() > 0) {
                        if((Float.valueOf(Firstlist.get(k)) / 10) == i) {
                            new_time.add(charttime.get(k));
                            idlist.add(String.valueOf(k + 1));
                            new_T.add(Firstlist.get(k));
                            if(Secondlist.size() > 0)
                                new_H.add(Secondlist.get(k));
                            if(Thirdlist.size() > 0)
                                new_C.add(Thirdlist.get(k));
                        }
                    }
                }
                showpage();
            }
            else if(s2.matches("≦")){
                float i = Float.valueOf(s3);
                for(int k = 0; k < charttime.size(); k++){
                    if(Firstlist.size() > 0) {
                        if((Float.valueOf(Firstlist.get(k)) / 10) <= i) {
                            new_time.add(charttime.get(k));
                            idlist.add(String.valueOf(k + 1));
                            new_T.add(Firstlist.get(k));
                            if(Secondlist.size() > 0)
                                new_H.add(Secondlist.get(k));
                            if(Thirdlist.size() > 0)
                                new_C.add(Thirdlist.get(k));
                        }
                    }
                }
                showpage();
            }
            else if(s2.matches("＜")){
                float i = Float.valueOf(s3);
                for(int k = 0; k < charttime.size(); k++){
                    if(Firstlist.size() > 0) {
                        if((Float.valueOf(Firstlist.get(k)) / 10) < i) {
                            new_time.add(charttime.get(k));
                            idlist.add(String.valueOf(k + 1));
                            new_T.add(Firstlist.get(k));
                            if(Secondlist.size() > 0)
                                new_H.add(Secondlist.get(k));
                            if(Thirdlist.size() > 0)
                                new_C.add(Thirdlist.get(k));
                        }
                    }
                }
                showpage();
            }
            else{
                Toast.makeText(SearchActivity.this, getString(R.string.wrong), Toast.LENGTH_SHORT).show();
            }
        }
        else if(s1.matches(Humidity)){
            if(s2.matches("＞")){
                float i = Float.valueOf(s3);
                for(int k = 0; k < charttime.size(); k++){
                    if(Secondlist.size() > 0) {
                        if((Float.valueOf(Secondlist.get(k)) / 10) > i) {
                            new_time.add(charttime.get(k));
                            idlist.add(String.valueOf(k + 1));
                            new_H.add(Secondlist.get(k));
                            if(Firstlist.size() > 0)
                                new_T.add(Firstlist.get(k));
                            if(Thirdlist.size() > 0)
                                new_C.add(Thirdlist.get(k));
                        }
                    }
                }
                showpage();
            }
            else if(s2.matches("≧")){
                float i = Float.valueOf(s3);
                for(int k = 0; k < charttime.size(); k++){
                    if(Secondlist.size() > 0) {
                        if((Float.valueOf(Secondlist.get(k)) / 10) >= i) {
                            new_time.add(charttime.get(k));
                            idlist.add(String.valueOf(k + 1));
                            new_H.add(Secondlist.get(k));
                            if(Firstlist.size() > 0)
                                new_T.add(Firstlist.get(k));
                            if(Thirdlist.size() > 0)
                                new_C.add(Thirdlist.get(k));
                        }
                    }
                }
                showpage();
            }
            else if(s2.matches("＝")){
                float i = Float.valueOf(s3);
                for(int k = 0; k < charttime.size(); k++){
                    if(Secondlist.size() > 0) {
                        if((Float.valueOf(Secondlist.get(k)) / 10) == i) {
                            new_time.add(charttime.get(k));
                            idlist.add(String.valueOf(k + 1));
                            new_H.add(Secondlist.get(k));
                            if(Firstlist.size() > 0)
                                new_T.add(Firstlist.get(k));
                            if(Thirdlist.size() > 0)
                                new_C.add(Thirdlist.get(k));
                        }
                    }
                }
                showpage();
            }
            else if(s2.matches("≦")){
                float i = Float.valueOf(s3);
                for(int k = 0; k < charttime.size(); k++){
                    if(Secondlist.size() > 0) {
                        if((Float.valueOf(Secondlist.get(k)) / 10) <= i) {
                            new_time.add(charttime.get(k));
                            idlist.add(String.valueOf(k + 1));
                            new_H.add(Secondlist.get(k));
                            if(Firstlist.size() > 0)
                                new_T.add(Firstlist.get(k));
                            if(Thirdlist.size() > 0)
                                new_C.add(Thirdlist.get(k));
                        }
                    }
                }
                showpage();
            }
            else if(s2.matches("＜")){
                float i = Float.valueOf(s3);
                for(int k = 0; k < charttime.size(); k++){
                    if(Secondlist.size() > 0) {
                        if((Float.valueOf(Secondlist.get(k)) / 10) < i) {
                            new_time.add(charttime.get(k));
                            idlist.add(String.valueOf(k + 1));
                            new_H.add(Secondlist.get(k));
                            if(Firstlist.size() > 0)
                                new_T.add(Firstlist.get(k));
                            if(Thirdlist.size() > 0)
                                new_C.add(Thirdlist.get(k));
                        }
                    }
                }
                showpage();
            }
            else{
                Toast.makeText(SearchActivity.this, getString(R.string.wrong), Toast.LENGTH_SHORT).show();
            }
        }
        else if(s1.matches(CO2)){
            if(s2.matches("＞")){

            }
            else if(s2.matches("≧")){

            }
            else if(s2.matches("＝")){

            }
            else if(s2.matches("≦")){

            }
            else if(s2.matches("＜")){

            }
            else{

            }
        }
    }

    private void showpage(){
        setContentView(R.layout.chart_listview);

        page = 1;

        ListView chart_list = (ListView)findViewById(R.id.datalist1);

        searchData = new SearchData(this, new_time, new_T, new_H, new_C, idlist, date, record,
                Temperature, Humidity, CO2);
        chart_list.setAdapter(searchData);
    }

    private Runnable arrayadd = new Runnable() {
        @Override
        public void run() {
            try {
                if(charttime.size() > 0){
                    condition1.add(getString(R.string.condition1));
                    sleep(30);
                    condition1.add(date);
                    sleep(30);
                    condition1.add(record);
                    sleep(30);
                    if(Firstlist.size() > 0){
                        condition1.add(Temperature);
                        sleep(30);
                    }
                    if(Secondlist.size() > 0){
                        condition1.add(Humidity);
                        sleep(30);
                    }
                    if(Thirdlist.size() > 0){
                        condition1.add(CO2);
                        sleep(30);
                    }
                }
                condition2.add(getString(R.string.condition2));
                sleep(30);
                condition2.add("＞");
                sleep(30);
                condition2.add("≧");
                sleep(30);
                condition2.add("＝");
                sleep(30);
                condition2.add("≦");
                sleep(30);
                condition2.add("＜");
                sleep(30);
                state = 1;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };

    public boolean onKeyDown(int key, KeyEvent event) {
        switch (key) {
            case KeyEvent.KEYCODE_SEARCH:
                break;
            case KeyEvent.KEYCODE_BACK:
                if(page == 0) {
                    finish();
                }
                else if(page == 1){
                    searchmenu();
                }
                else if(page == 2){

                }
                break;
            case KeyEvent.KEYCODE_DPAD_CENTER:
                break;
            default:
                return false;
        }
        return false;
    }
}
