package com.jetec.nordic_googleplay.Activity;


import android.content.Intent;
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
import com.jetec.nordic_googleplay.ViewAdapter.ChartData;
import java.util.ArrayList;

public class ChartActivity extends AppCompatActivity {

    private Vibrator vibrator;
    private Menu menu;
    private ArrayList<String> timelist, charttime, Logdata, Tlist, Hlist, Clist;
    private String TAG = "Chartlog", getdate, getsize, gett, geth, getc;
    private int thread;
    private ChartData chartData;
    private double all_Width, all_Height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vibrator = (Vibrator) this.getSystemService(VIBRATOR_SERVICE);
        chartlistview();
    }

    private void chartlistview(){
        setContentView(R.layout.chart_listview);

        thread = 0;

        ListView chart_list = (ListView)findViewById(R.id.datalist1);

        timelist = new ArrayList<String>();
        charttime = new ArrayList<String>();
        Logdata = new ArrayList<String>();
        Tlist = new ArrayList<String>();
        Hlist = new ArrayList<String>();
        Clist = new ArrayList<String>();

        timelist.clear();
        charttime.clear();
        Logdata.clear();
        Tlist.clear();
        Hlist.clear();
        Clist.clear();

        Intent intent = getIntent();
        timelist = intent.getStringArrayListExtra("timelist");
        charttime = intent.getStringArrayListExtra("charttime");
        Tlist = intent.getStringArrayListExtra("Tlist");
        Hlist = intent.getStringArrayListExtra("Hlist");
        Clist = intent.getStringArrayListExtra("Clist");
        all_Width = intent.getDoubleExtra("all_Width", all_Width);
        all_Height = intent.getDoubleExtra("all_Height", all_Height);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        getdate = getString(R.string.datetime);
        getsize = getString(R.string.size);
        gett = getString(R.string.Temperature);
        geth = getString(R.string.Humidity);
        getc = getString(R.string.Co2);

        chartData = new ChartData(this, charttime, Tlist, Hlist, Clist, getdate, getsize,
                gett, geth, getc);
        chart_list.setAdapter(chartData);
    }

    private void searchdata(){

        Intent intent = new Intent(ChartActivity.this, SearchActivity.class);
        intent.putStringArrayListExtra("charttime", charttime);
        intent.putStringArrayListExtra("timelist", timelist);
        intent.putStringArrayListExtra("Tlist",Tlist);
        intent.putStringArrayListExtra("Hlist",Hlist);
        intent.putStringArrayListExtra("Clist",Clist);
        intent.putExtra("all_Height", all_Height);
        intent.putExtra("all_Width", all_Width);

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

    private void back(){
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
