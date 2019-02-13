package com.jetec.nordic_googleplay.Activity;


import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.jetec.nordic_googleplay.R;
import com.jetec.nordic_googleplay.Value;
import com.jetec.nordic_googleplay.ViewAdapter.ChartData;

import java.util.ArrayList;

public class ChartActivity extends AppCompatActivity {

    private Vibrator vibrator;
    private Menu menu;
    private ArrayList<String> timelist, charttime, Logdata, Firstlist, Secondlist, Thirdlist;
    private String TAG = "Chartlog", getdate, getsize, gett = "", geth = "", getc = "";
    private int thread;
    private ChartData chartData;
    private double all_Width, all_Height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vibrator = (Vibrator) this.getSystemService(VIBRATOR_SERVICE);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        }

        chartlistview();
    }

    private void chartlistview() {
        setContentView(R.layout.chart_listview);

        thread = 0;

        ListView chart_list = (ListView) findViewById(R.id.datalist1);

        timelist = new ArrayList<String>();
        charttime = new ArrayList<String>();
        Logdata = new ArrayList<String>();
        Firstlist = new ArrayList<String>();
        Secondlist = new ArrayList<String>();
        Thirdlist = new ArrayList<String>();

        timelist.clear();
        charttime.clear();
        Logdata.clear();
        Firstlist.clear();
        Secondlist.clear();
        Thirdlist.clear();

        Intent intent = getIntent();

        timelist = Value.timelist;
        charttime = Value.charttime;
        Firstlist = Value.Firstlist;
        Secondlist = Value.Secondlist;
        Thirdlist = Value.Thirdlist;
        all_Width = Value.all_Width;
        all_Height = Value.all_Height;

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        getdate = getString(R.string.datetime);
        getsize = getString(R.string.size);

        if(Firstlist.size() != 0 && Secondlist.size() != 0 && Thirdlist.size() != 0) {
            if (Value.name.get(2).toString().matches("C")) {
                getc = getString(R.string.Co2);
            } else if (Value.name.get(2).toString().matches("T")) {
                getc = getString(R.string.Temperature);
            } else if (Value.name.get(2).toString().matches("H")) {
                getc = getString(R.string.Humidity);
            } else if (Value.name.get(2).toString().matches("I")) {
                getc = getString(R.string.I3row);
            }

            if (Value.name.get(1).toString().matches("C")) {
                geth = getString(R.string.Co2);
            } else if (Value.name.get(1).toString().matches("T")) {
                geth = getString(R.string.Temperature);
            } else if (Value.name.get(1).toString().matches("H")) {
                geth = getString(R.string.Humidity);
            } else if (Value.name.get(1).toString().matches("I")) {
                geth = getString(R.string.I2row);
            }

            if (Value.name.get(0).toString().matches("C")) {
                gett = getString(R.string.Co2);
            } else if (Value.name.get(0).toString().matches("T")) {
                gett = getString(R.string.Temperature);
            } else if (Value.name.get(0).toString().matches("H")) {
                gett = getString(R.string.Humidity);
            } else if (Value.name.get(0).toString().matches("I")) {
                gett = getString(R.string.I1row);
            }
        }
        else if(Firstlist.size() != 0 && Secondlist.size() != 0){
            if (Value.name.get(1).toString().matches("C")) {
                geth = getString(R.string.Co2);
            } else if (Value.name.get(1).toString().matches("T")) {
                geth = getString(R.string.Temperature);
            } else if (Value.name.get(1).toString().matches("H")) {
                geth = getString(R.string.Humidity);
            } else if (Value.name.get(1).toString().matches("I")) {
                geth = getString(R.string.I2row);
            }

            if (Value.name.get(0).toString().matches("C")) {
                gett = getString(R.string.Co2);
            } else if (Value.name.get(0).toString().matches("T")) {
                gett = getString(R.string.Temperature);
            } else if (Value.name.get(0).toString().matches("H")) {
                gett = getString(R.string.Humidity);
            } else if (Value.name.get(0).toString().matches("I")) {
                gett = getString(R.string.I1row);
            }
        }
        else if(Firstlist.size() != 0){
            if (Value.name.get(0).toString().matches("C")) {
                gett = getString(R.string.Co2);
            } else if (Value.name.get(0).toString().matches("T")) {
                gett = getString(R.string.Temperature);
            } else if (Value.name.get(0).toString().matches("H")) {
                gett = getString(R.string.Humidity);
            } else if (Value.name.get(0).toString().matches("I")) {
                gett = getString(R.string.I1row);
            }
        }

        chartData = new ChartData(this, charttime, Firstlist, Secondlist, Thirdlist, getdate, getsize,
                gett, geth, getc);
        chart_list.setAdapter(chartData);
    }

    private void searchdata() {

        Intent intent = new Intent(ChartActivity.this, SearchActivity.class);

        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) { //toolbar menu
        super.onCreateOptionsMenu(menu);
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.chart, menu);
        //updateMenuTitles();
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {   //toolbar menu item
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            vibrator.vibrate(100);
            searchdata();
            return true;
        }

        return true;
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

    private void back() {
        Intent result = new Intent();
        setResult(LogChartView.RESULT_OK, result);
        finish();
    }

    public boolean onKeyDown(int key, KeyEvent event) {
        switch (key) {
            case KeyEvent.KEYCODE_SEARCH:
                break;
            case KeyEvent.KEYCODE_BACK:
                vibrator.vibrate(100);
                back();
                break;
            case KeyEvent.KEYCODE_DPAD_CENTER:
                break;
            default:
                return false;
        }
        return false;
    }
}
